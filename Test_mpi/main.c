/*
 * TODO:
 * - remove verbose stuff
 */

/*
 * example usage:
 * mpicc mpi-parallel-io-convolution-multichannel-pers.c -std=c99 -lm -O2
 * mpiexec -f machines -np 36 ./a.out -o out.raw -i psproj/photos/landscape_w5040xh3840.raw -w 5040 -h 3840 -c 3 -n 20
 */

#include <sys/stat.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <errno.h>
#define _GNU_SOURCE
#include <stdio.h>
#include <math.h>
#include "mpi.h"

#define MAX_PATH_LEN    (int) 256
#define master  0
#define true    1
#define false   0

/* Weighted 3x3 smoothing kernel with Gaussian blur */
const static int radius = 3;
const static int kernel[3][3] = {
    { 1, 1, 1 },
    { 1, 1, 1 },
    { 1, 1, 1 }
};
static int sum = 0;

struct args;
static void get_args(int argc, char **argv, struct args *args, int size);
static void broadcast_args(struct args *args);

struct args {
    int height, width, iterations, channels;
    char input[MAX_PATH_LEN], output[MAX_PATH_LEN];
};


static void get_args(int argc, char **argv, struct args *args, int size){
    if(argc != 13){
    abort_and_show_usage:
        fprintf(stderr, "usage:  mpiexec [-f machines] -np processes " \
                "%s -i input -o output -h height -w width " \
                "-n iterations -c channels\n", *argv);
        MPI_Abort(MPI_COMM_WORLD, 0);
    }
    int i;
    for(i=1; i<argc; i+=2)
        if(argv[i][0] != '-')
            goto abort_and_show_usage;
        else switch(argv[i][1]){
            case 'i': strncpy(args->input, argv[i+1], sizeof(args->input));
                break;
            case 'o': strncpy(args->output, argv[i+1], sizeof(args->output));
                break;
            case 'h': args->height = atoi(argv[i+1]); break;
            case 'w': args->width = atoi(argv[i+1]); break;
            case 'n': args->iterations = atoi(argv[i+1]); break;
            case 'c': args->channels = atoi(argv[i+1]); break;
            default: goto abort_and_show_usage;
        }
    char *p;
    if((p = strrchr(args->input,'.')) == NULL || strcmp(p+1,"raw") ||
       (p = strrchr(args->output,'.')) == NULL || strcmp(p+1,"raw")){
        fprintf(stderr,"only raw format is supported.\n");
        MPI_Abort(MPI_COMM_WORLD, 0);
    }
    struct stat st;
    if(stat(args->input, &st) < 0 || args->height < 0 || args->width < 0 ||
       args->iterations < 0 || (args->channels != 1 && args->channels != 3))
        goto abort_and_show_usage;
    if(args->height * args->width % size || sqrt(size) * sqrt(size) != size){
        fprintf(stderr,"image dimensions not divisible by process topology.\n");
        MPI_Abort(MPI_COMM_WORLD, 0);
    }
}

static void convolve(unsigned char *data,unsigned char *buffer,int index,int bheight,int bwidth, int channel, int channels){
    int value = data[(index-bwidth-3) * channels+ channel] * kernel[0][0] +    /* up left */
    data[(index-bwidth-2) * channels+ channel] * kernel[0][1] + /* up */
    data[(index-bwidth-1) * channels+ channel] * kernel[0][2] + /* up right */
    data[(index-1) * channels+ channel] * kernel[1][0] +       /* left */
    data[(index) * channels+ channel] * kernel[1][1] +         /* middle */
    data[(index+1) * channels+ channel] * kernel[1][2] +       /* right */
    data[(index+bwidth+1) * channels+ channel] * kernel[2][0] + /* down left */
    data[(index+bwidth+2) * channels+ channel] * kernel[2][1] + /* down */
    data[(index+bwidth+3) * channels+ channel] * kernel[2][2];  /* down right */
    value /= sum;
    buffer[index* channels+ channel] = value > UCHAR_MAX ? UCHAR_MAX : value < 0 ? 0 : value;
}

static void broadcast_args(struct args *args){
    MPI_Datatype struct_datatype;
    int blocklengths[6] = { 1, 1, 1, 1, MAX_PATH_LEN, MAX_PATH_LEN };
    MPI_Aint displacements[6];
    /* compute displacements of structure components */
    MPI_Address(&args->height, &displacements[0]);
    MPI_Address(&args->width, &displacements[1]);
    MPI_Address(&args->iterations, &displacements[2]);
    MPI_Address(&args->channels, &displacements[3]);
    MPI_Address(&args->input, &displacements[4]);
    MPI_Address(&args->output, &displacements[5]);
    MPI_Aint base = displacements[0];
    int i;
    for(i=0; i<6; ++i)
        displacements[i] -= base;
    MPI_Datatype types[6] = { MPI_INT, MPI_INT, MPI_INT, MPI_INT, MPI_CHAR,
        MPI_CHAR };
    MPI_Type_struct(6, blocklengths, displacements, types, &struct_datatype);
    MPI_Type_commit(&struct_datatype);
    MPI_Bcast(args, 1, struct_datatype, master, MPI_COMM_WORLD);
    MPI_Type_free(&struct_datatype);
}


int main(int argc,char **argv){
    MPI_Init(&argc, &argv);
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    
    struct args args;
    if(rank == master)
        get_args(argc, argv, &args, size);
    /* broadcast arguments */
    broadcast_args(&args);
    
    int height = args.height,
    width = args.width,
    iterations = args.iterations,
    channels = args.channels;
    if(rank == master)
        printf("info: input = %s output = %s height = %4d width = %4d " \
               "iterations = %4d channels = %s\n", args.input, args.output,
               height, width, iterations, channels != 1 ? "RGB" : "Grayscale");
    
    /* Calculate the kernel sum. If the sum of coefficients is 0 set it to 1 */
    int j;
    int i;
    for(i=0; i<radius; ++i)
        for(j=0; j<radius; ++j)
            sum += kernel[i][j];
    sum = !sum ? 1:sum;
    
    /*** cartesian grid topology ***/
    int ndims = 2, *dims;
    if((dims = calloc(ndims, sizeof(int))) == NULL)
        MPI_Abort(MPI_COMM_WORLD, 0);
    MPI_Dims_create(size, ndims, dims);  /* get cartesian grid dimensions */
    /* create the cartesian communicator */
    MPI_Comm comm;
    int periods[2] = {0, 0};    /* periodicity set to false on x,y dimensions */
    int reorder = 1;            /* ranking may be reordered by the topology */
    MPI_Cart_create(MPI_COMM_WORLD, ndims, dims, periods, reorder, &comm);
    /* get process rank on the cartesian grid */
    MPI_Comm_rank(comm, &rank);
    /* get process coordinates in the cartesian grid */
    int coords[2], ncoords[2];
    MPI_Cart_coords(comm, rank, ndims, coords);
    int neighbor[8];    /* array of neighbor ranks */
    for(i=0; i<8; ++i)
        neighbor[i] = MPI_PROC_NULL;    /* initialize to MPI_PROC_NULL */
    /* up-left neighbor [0] */
    if((ncoords[0]=coords[0]-1) >= 0 && (ncoords[1]=coords[1]-1) >= 0)
        MPI_Cart_rank(comm, ncoords, &neighbor[0]);
    /* up-right neighbor [2] */
    if((ncoords[0]=coords[0]-1) >= 0 && (ncoords[1]=coords[1]+1) < dims[1])
        MPI_Cart_rank(comm, ncoords, &neighbor[2]);
    /* down-left neighbor [5] */
    if((ncoords[0]=coords[0]+1) < dims[0] && (ncoords[1]=coords[1]-1) >= 0)
        MPI_Cart_rank(comm, ncoords, &neighbor[5]);
    /* down-right neighbor [7] */
    if((ncoords[0]=coords[0]+1) < dims[0] && (ncoords[1]=coords[1]+1) < dims[1])
        MPI_Cart_rank(comm, ncoords, &neighbor[7]);
    /* upper [1] and lower [6] neighbor */
    MPI_Cart_shift(comm, 0, 1, &neighbor[1], &neighbor[6]);
    /* left [3] and right [4] neighbor */
    MPI_Cart_shift(comm, 1, 1, &neighbor[3], &neighbor[4]);
    
    /*** cartesian topology info ***/
    if(rank == master)
        printf("info: cartesian grid dimensions = %dx%d\n", dims[0], dims[1]);
    
    /*** block-size assigned to each process ***/
    int bheight = height/dims[0]; /* block height */
    int bwidth = width/dims[1];   /* block width */
    
    /*** custom datatypes ***/
    /** subarray datatype (inner frame) **/
    MPI_Datatype subarray_datatype;
    int subarray_size[2];
    subarray_size[0] = height;  /* image height in MPI_UNSIGNED_CHAR */
    subarray_size[1] = width * channels;   /* image width in MPI_UNSIGNED_CHAR */
    int subarray_subsize[2];
    subarray_subsize[0] = bheight;  /* subarray height in MPI_UNSIGNED_CHAR */
    subarray_subsize[1] = bwidth * channels;   /* subarray width in MPI_UNSIGNED_CHAR */
    int subarray_displacement[2];
    /* height in which subarray starts in MPI_UNSIGNED_CHAR */
    subarray_displacement[0] = coords[0] * subarray_subsize[0];
    /* width in which subarray starts in MPI_UNSIGNED_CHAR */
    subarray_displacement[1] = coords[1] * subarray_subsize[1];
    MPI_Type_create_subarray(2, subarray_size, subarray_subsize,
                             subarray_displacement, MPI_ORDER_C, MPI_UNSIGNED_CHAR,
                             &subarray_datatype);
    MPI_Type_commit(&subarray_datatype);
    
    /** row datatype (upper and lower margin) **/
    /* count = bwidth, blocklength = 1, stride = 1 */
    MPI_Datatype row_datatype;
    MPI_Type_vector(bwidth, channels, channels, MPI_UNSIGNED_CHAR, &row_datatype);
    MPI_Type_commit(&row_datatype);
    
    /** column datatype (right and left margin) **/
    /* count = bheight, blocklength = 1, stride = bwidth+2 */
    MPI_Datatype col_datatype;
    MPI_Type_vector(bheight, channels, (bwidth+2)*channels, MPI_UNSIGNED_CHAR, &col_datatype);
    MPI_Type_commit(&col_datatype);
    
    /*** t, t+1 generation buffers ***/
    unsigned char *data = calloc((bheight+2) * (bwidth+2) * channels,
                                 sizeof(unsigned char));
    unsigned char *buffer = calloc((bheight+2) * (bwidth+2) * channels,
                                   sizeof(unsigned char));
    
    /*** MPI-IO parallel read ***/
    MPI_File fp;
    MPI_File_open(comm, args.input, MPI_MODE_RDONLY, MPI_INFO_NULL, &fp);
    MPI_File_set_view(fp, 0, MPI_UNSIGNED_CHAR, subarray_datatype, "native",
                      MPI_INFO_NULL);
    MPI_File_read_all(fp, data, subarray_subsize[0] * subarray_subsize[1], MPI_UNSIGNED_CHAR,
                      MPI_STATUS_IGNORE);
    MPI_File_close(&fp);
    
    // -----------------------------------------------------------------------------
    
    /* rearrange buffer (add halo cells) */
    for(i=bheight-1; i>=0; --i)
        memmove(&data[((i+1)*(bwidth+2)+1)*channels], &data[i*bwidth*channels],
                bwidth*channels*sizeof(unsigned char));
    
    /* for process that have no neighbor, replicate their margins */
    /* replicate upper margin */
    if(neighbor[1] != MPI_PROC_NULL)
        memmove(&data[channels], &data[(bwidth+3)*channels],
                bwidth*channels*sizeof(unsigned char));
    /* replicate lower margin */
    if(neighbor[6] != MPI_PROC_NULL)
        memmove(&data[((bheight+1)*(bwidth+2)+1)*channels],
                &data[(bheight*(bwidth+2)+1)*channels],
                bwidth*channels*sizeof(unsigned char));
    /* replicate upper left corner */
    if(neighbor[0] != MPI_PROC_NULL)
        memmove(&data[channels], &data[((bwidth+2)+1)*channels],
                channels*sizeof(unsigned char));
    /* replicate upper right corner */
    if(neighbor[2] != MPI_PROC_NULL)
        memmove(&data[(bwidth+1)*channels], &data[(2*(bwidth+2)-2)*channels],
                channels*sizeof(unsigned char));
    /* replicate lower left corner */
    if(neighbor[5] != MPI_PROC_NULL)
        memmove(&data[(bheight+1)*(bwidth+2)*channels],
                &data[(bheight*(bwidth+2)+1)*channels], channels*sizeof(unsigned char));
    /* replicate lower right corner */
    if(neighbor[7] != MPI_PROC_NULL)
        memmove(&data[((bheight+2)*(bwidth+2)-1)*channels],
                &data[((bheight+1)*(bwidth+2)-2)*channels],
                channels*sizeof(unsigned char));
    /* replicate left margin */
    if(neighbor[3] != MPI_PROC_NULL)
        for(i=0; i<bheight; ++i)
            memmove(&data[(i+1)*(bwidth+2)*channels],
                    &data[((i+1)*(bwidth+2)+1)*channels], channels*sizeof(unsigned char));
    /* replicate right margin */
    if(neighbor[4] != MPI_PROC_NULL)
        for(i=0; i<bheight; ++i)
            memmove(&data[((i+1)*(bwidth+2)+bwidth+1)*channels],
                    &data[((i+1)*(bwidth+2)+bwidth)*channels],
                    channels*sizeof(unsigned char));
    
    MPI_Request request[32];
    /** persistent communication **/
    if(neighbor[0] != MPI_PROC_NULL){
        MPI_Send_init(&data[(bwidth+3)*channels], channels, MPI_UNSIGNED_CHAR,
                      neighbor[0], 0, comm, &request[0]);
        MPI_Recv_init(&data[0], channels, MPI_UNSIGNED_CHAR, neighbor[0], 7,
                      comm, &request[1]);
        MPI_Send_init(&buffer[(bwidth+3)*channels], channels, MPI_UNSIGNED_CHAR,
                      neighbor[0], 0, comm, &request[16]);
        MPI_Recv_init(&buffer[0], channels, MPI_UNSIGNED_CHAR, neighbor[0], 7,
                      comm, &request[17]);
    }
    if(neighbor[1] != MPI_PROC_NULL){
        MPI_Send_init(&data[(bwidth+3)*channels], 1, row_datatype, neighbor[1],
                      1, comm, &request[2]);
        MPI_Recv_init(&data[1 * channels], 1, row_datatype, neighbor[1], 6,
                      comm, &request[3]);
        MPI_Send_init(&buffer[(bwidth+3)*channels], 1, row_datatype, neighbor[1],
                      1, comm, &request[18]);
        MPI_Recv_init(&buffer[1 * channels], 1, row_datatype, neighbor[1], 6,
                      comm, &request[19]);
    }
    if(neighbor[2] != MPI_PROC_NULL){
        MPI_Send_init(&data[(2*(bwidth+2)-2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[2], 2, comm, &request[4]);
        MPI_Recv_init(&data[(bwidth+1)*channels], channels, MPI_UNSIGNED_CHAR,
                      neighbor[2], 5, comm, &request[5]);
        MPI_Send_init(&buffer[(2*(bwidth+2)-2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[2], 2, comm, &request[20]);
        MPI_Recv_init(&buffer[(bwidth+1)*channels], channels, MPI_UNSIGNED_CHAR,
                      neighbor[2], 5, comm, &request[21]);
    }
    if(neighbor[3] != MPI_PROC_NULL){
        MPI_Send_init(&data[(bwidth+3)*channels], 1, col_datatype, neighbor[3],
                      3, comm, &request[6]);
        MPI_Recv_init(&data[(bwidth+2)*channels], 1, col_datatype, neighbor[3],
                      4, comm, &request[7]);
        MPI_Send_init(&buffer[(bwidth+3)*channels], 1, col_datatype, neighbor[3],
                      3, comm, &request[22]);
        MPI_Recv_init(&buffer[(bwidth+2)*channels], 1, col_datatype, neighbor[3],
                      4, comm, &request[23]);
    }
    if(neighbor[4] != MPI_PROC_NULL){
        MPI_Send_init(&data[(2*(bwidth+2)-2)*channels], 1, col_datatype,
                      neighbor[4], 4, comm, &request[8]);
        MPI_Recv_init(&data[(2*(bwidth+2)-1)*channels], 1, col_datatype,
                      neighbor[4], 3, comm, &request[9]);
        MPI_Send_init(&buffer[(2*(bwidth+2)-2)*channels], 1, col_datatype,
                      neighbor[4], 4, comm, &request[24]);
        MPI_Recv_init(&buffer[(2*(bwidth+2)-1)*channels], 1, col_datatype,
                      neighbor[4], 3, comm, &request[25]);
    }
    if(neighbor[5] != MPI_PROC_NULL){
        MPI_Send_init(&data[(bheight*(bwidth+2)+1)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[5], 5, comm, &request[10]);
        MPI_Recv_init(&data[(bheight+1)*(bwidth+2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[5], 2, comm, &request[11]);
        MPI_Send_init(&buffer[(bheight*(bwidth+2)+1)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[5], 5, comm, &request[26]);
        MPI_Recv_init(&buffer[(bheight+1)*(bwidth+2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[5], 2, comm, &request[27]);
    }
    if(neighbor[6] != MPI_PROC_NULL){
        MPI_Send_init(&data[(bheight*(bwidth+2)+1)*channels], 1, row_datatype,
                      neighbor[6], 6, comm, &request[12]);
        MPI_Recv_init(&data[((bheight+1)*(bwidth+2)+1) * channels], 1,
                      row_datatype, neighbor[6], 1, comm, &request[13]);
        MPI_Send_init(&buffer[(bheight*(bwidth+2)+1)*channels], 1, row_datatype,
                      neighbor[6], 6, comm, &request[28]);
        MPI_Recv_init(&buffer[((bheight+1)*(bwidth+2)+1) * channels], 1,
                      row_datatype, neighbor[6], 1, comm, &request[29]);
    }
    if(neighbor[7] != MPI_PROC_NULL){
        MPI_Send_init(&data[((bheight+1)*(bwidth+2)-2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[7], 7, comm, &request[14]);
        MPI_Recv_init(&data[((bheight+2)*(bwidth+2)-1)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[7], 0, comm, &request[15]);
        MPI_Send_init(&buffer[((bheight+1)*(bwidth+2)-2)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[7], 7, comm, &request[30]);
        MPI_Recv_init(&buffer[((bheight+2)*(bwidth+2)-1)*channels], channels,
                      MPI_UNSIGNED_CHAR, neighbor[7], 0, comm, &request[31]);
    }
    
    double ts,te,tes,tee;
    double local_computation_time = 0.0, local_communication_time = 0.0;
    int iter,offset,global_convergence = false;
    
    int check_freq = (int) sqrt(iterations);
    if(rank == master)
        printf("info: checking for convergence every %d iterations\n", check_freq);
    
    MPI_Barrier(comm);
    tes = MPI_Wtime();
    /* #pragma omp parallel */
    for(iter=0; iter<iterations && !global_convergence ; ++iter){
        offset = iter % 2 ? 16 : 0; /* iter %2 = 0:buffer, 1:data */
        ts = MPI_Wtime();
        if(neighbor[0] != MPI_PROC_NULL){
            MPI_Start(&request[0 + offset]);
            MPI_Start(&request[1 + offset]);
        }
        if(neighbor[1] != MPI_PROC_NULL){
            MPI_Start(&request[2 + offset]);
            MPI_Start(&request[3 + offset]);
        }
        if(neighbor[2] != MPI_PROC_NULL){
            MPI_Start(&request[4 + offset]);
            MPI_Start(&request[5 + offset]);
        }
        if(neighbor[3] != MPI_PROC_NULL){
            MPI_Start(&request[6 + offset]);
            MPI_Start(&request[7 + offset]);
        }
        if(neighbor[4] != MPI_PROC_NULL){
            MPI_Start(&request[8 + offset]);
            MPI_Start(&request[9 + offset]);
        }
        if(neighbor[5] != MPI_PROC_NULL){
            MPI_Start(&request[10 + offset]);
            MPI_Start(&request[11 + offset]);
        }
        if(neighbor[6] != MPI_PROC_NULL){
            MPI_Start(&request[12 + offset]);
            MPI_Start(&request[13 + offset]);
        }
        if(neighbor[7] != MPI_PROC_NULL){
            MPI_Start(&request[14 + offset]);
            MPI_Start(&request[15 + offset]);
            
        }
        te = MPI_Wtime();
        local_communication_time += te-ts;
        ts = te;
        int channel;
        for(channel=0; channel<channels; ++channel)
        /* #pragma omp for collapse(2) */
            for(i=0; i<bheight-2; ++i)
                for(j=0; j<bwidth-2; ++j)
                    convolve(data, buffer, (i+2)*(bwidth+2)+j+2, bheight,
                             bwidth, channel, channels);
        
        /* http://stackoverflow.com/questions/10540760/openmp-nested-parallel-for-loops-vs-inner-parallel-for */
        // #pragma omp parallel for default(none) shared(data, buffer, bheight, bwidth) schedule(static)
        // for(int k=0; k<(bheight-2)*(bwidth-2); ++k){
        //     int i=k/(bwidth-2);
        //     int j=k%(bwidth-2);
        //     convolve(data,buffer,(i+2)*(bwidth+2)+j+2,bheight,bwidth);
        // }
        
        te = MPI_Wtime();
        local_computation_time += te-ts;
        ts = te;
        for(i=0; i<8; ++i)
            if(neighbor[i] != MPI_PROC_NULL){
                MPI_Wait(&request[2*i + offset],MPI_STATUS_IGNORE);
                MPI_Wait(&request[2*i+1 + offset],MPI_STATUS_IGNORE);
            }
        te = MPI_Wtime();
        local_communication_time += te-ts;
        ts = te;
        /** convolve outer matrix **/
        /* convolve upper and lower margin elements */
        for(channel=0; channel<channels; ++channel)
            for(i=0; i<bwidth; ++i){
                convolve(data, buffer, bwidth+i+3, bheight, bwidth, channel, channels);    /* upper margin */
                convolve(data, buffer, bheight*(bwidth+2)+i+1, bheight, bwidth, channel, channels);    /* lower margin */
            }
        /* convolve left and right margin elements */
        for(channel=0; channel<channels; ++channel)
            for(i=0; i<bheight-2; ++i){
                convolve(data,buffer,(i+2)*(bwidth+2)+1,bheight,bwidth, channel, channels);    /* left margin */
                convolve(data,buffer,(i+3)*(bwidth+2)-2,bheight,bwidth, channel, channels);    /* right margin */
            }
        if(iter % check_freq == 0){
            /** convergence check **/
            int local_convergence = true;
            for(i=0; i<bheight+2 && local_convergence; ++i)
                for(j=0; j<(bwidth+2) && local_convergence; ++j)
                    for(channel=0; channel<channels; ++channel)
                        if(data[(i*(bwidth+2)+j) * channels + channel] != buffer[(i*(bwidth+2)+j) * channels + channel])
                            local_convergence = false;
            MPI_Allreduce(&local_convergence, &global_convergence, 1, MPI_INT, MPI_MIN, comm);
        }
        
        /* swap buffers */
        unsigned char *temp = data;
        data = buffer;
        buffer = temp;
        
        local_computation_time += te-ts;
    }
    tee = MPI_Wtime();
    double local_execution_time = tee-tes;
    
    /* reduce */
    double global_execution_time;
    MPI_Reduce(&local_execution_time, &global_execution_time, 1, MPI_DOUBLE, MPI_MAX, master, comm);
    double global_communication_time;
    MPI_Reduce(&local_communication_time, &global_communication_time, 1, MPI_DOUBLE, MPI_MAX, master, comm);
    double global_computation_time;
    MPI_Reduce(&local_computation_time, &global_computation_time, 1, MPI_DOUBLE, MPI_MAX, master, comm);
    
    /* rearrange buffer (remove halo cells) */
    for(i=0; i<bheight; ++i)
        memmove(&data[i*bwidth*channels], &data[((i+1)*(bwidth+2)+1)*channels],
                bwidth*channels*sizeof(unsigned char));
    
    /*** MPI-IO parallel write ***/
    MPI_File_open(comm, args.output, MPI_MODE_WRONLY | MPI_MODE_CREATE,
                  MPI_INFO_NULL, &fp);
    MPI_File_set_view(fp, 0, MPI_UNSIGNED_CHAR, subarray_datatype, "native",
                      MPI_INFO_NULL);
    MPI_File_write_all(fp, data, subarray_subsize[0]*subarray_subsize[1],
                       MPI_UNSIGNED_CHAR, MPI_STATUS_IGNORE);
    MPI_File_close(&fp);
    
    if(rank == master){
        printf("\e[32m***\e[0m %-20s %d/%d \n", "iterations:", iter, iterations);
        printf("\e[34m***\e[0m %-20s %lf sec\n", "execution time:", global_execution_time);
        printf("\e[31m***\e[0m %-20s %lf sec\n", "communication time:", global_communication_time);
        printf("\e[33m***\e[0m %-20s %lf sec\n", "computation time:", global_computation_time);
    }
    
    /* deallocate the resources */
    for(i=0; i<8; ++i)
        if(neighbor[i] != MPI_PROC_NULL){
            MPI_Request_free(&request[2*i]);
            MPI_Request_free(&request[2*i+1]);
            MPI_Request_free(&request[2*i+16]);
            MPI_Request_free(&request[2*i+17]);
        }
    MPI_Type_free(&subarray_datatype);
    MPI_Type_free(&row_datatype);
    MPI_Type_free(&col_datatype);
    MPI_Comm_free(&comm);
    free(buffer);
    free(data);
    free(dims);
    MPI_Finalize();
    
    exit(EXIT_SUCCESS);
}


