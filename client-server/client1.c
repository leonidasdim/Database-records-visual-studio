#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>
#include <signal.h>
#include <time.h>
#define SHMKEY1 (key_t) 8821
#define PERM 0666
#define SHMKEY2 (key_t) 804
void up(int x,int num);
void down(int y,int num);


int main(int argc,char **argv){

struct timeval  first, second;
int N;
int lines;
pid_t pid,*pids;
char seira[120];
int shm_id1,shm_id2,sem_id,i,l,status;

int *count;
if(argc !=2){

printf("dwse swsta orismata \n");
	printf("dwse enan aritmo N \n");
	return -1;

}
	N=atoi(argv[1]);




typedef struct{			//same struct with server
	int b;
	char line[120];
	int c;
	}grammi;
grammi *grammi2;

shm_id1=shmget (SHMKEY1,sizeof(grammi),PERM|IPC_CREAT);		//shared memory with same SHMKEY1 with server
	if(shm_id1==-1)
                printf ("erorr shared memory 1 \\n");
        else{
                printf ("\nshared memory1 succesfull\n");
                grammi2= shmat(shm_id1, (grammi*)0, 0);	
        }

shm_id2=shmget (SHMKEY2,sizeof(int),PERM|IPC_CREAT);			// shared memory for Ci
	if(shm_id2==-1)
                printf ("erorr shared memory Ci count\n");
        else{
                printf ("\nshared memory Ci count succesfull\n");
                count= shmat(shm_id2, (int*)0, 0);	
        }
		*count=0;





union senum{
  int val;
  struct semid_ds *buff;
  unsigned short * array;
};


union senum arg0,arg1,arg2;


sem_id=semget(SHMKEY1,3,PERM | IPC_CREAT);		// array of 3 semaphores for memory 1
	
	arg0.val=0;
	arg1.val=1;
	arg2.val=0;
	

	if (semctl(sem_id,0, SETVAL, arg0) <0) {			//
                perror("semctl");
                return -1;
        }
	if (semctl(sem_id,1, SETVAL, arg1) <0) {			//
                perror("semctl");
                return -1;
        }
	if (semctl(sem_id,2, SETVAL, arg2) <0) {			//--------semaphores' initialization
                perror("semctl");
                return -1;
    }




if ((pids=calloc((N+1),sizeof(pid_t)))==NULL ){				// pid  array
		perror("calloc");
		return -1;
	}

	for(i=0;i<N;i++){			//create N Ci processes
	pid=fork();
	pids[i]=pid;
	if(!pid)			//if child because we want  N Ci with one father
	break;
	}
	
	if(!pid){	sleep(1);
			down(sem_id,1);			// down  2nd semaphore for block other  Ci
			sleep(10);
			*count=*count+1;	
			if(*count==1)			//critical district only one processes Ci 
			grammi2->c=N;
			printf("eimai to paidi %d \n",getpid());
			printf("please enter the line number you want ::::\n");
			scanf("%d",&grammi2->b);
						
   
			gettimeofday(&first, NULL);		//time count
			
			
			up(sem_id,0);				//unblock shared memory 1 for processes D
			down(sem_id,2);				//block C only D work on shared memory 1
			gettimeofday(&second, NULL);		
			
    			
			
			printf("%s \n and time responece %f \n",grammi2->line, (double) (second.tv_usec - first.tv_usec) / 1000000 + (double) (second.tv_sec - first.tv_sec));		//print messenger

			up(sem_id,1);		// unblock Ci
			
		sleep(1);



		
			
			

	}
	else if(pid!=0) {
	printf("eimai o pateras %d \n",getppid());
	for(l=0;l<N;l++){			//wait childrens
		pids[l]=wait(&status);
		if(pids[l]!=-1)
		printf("im procces %d and i finish \n",pids[l]);

			}
		free(pids);
		printf("teliwsameeeeeeee\n");

	// ----------shared memorys deleteee---------------------
	if ( shmctl(shm_id2,IPC_RMID,NULL) == -1  ){
               		perror("shmctl");
               		return -1;
        	}
	
	if ( shmctl(shm_id1,IPC_RMID,NULL) == -1  ){
               		perror("shmctl");
               		return -1;
        	}
		if ( shmdt(grammi2) == -1  ){
               		perror("shmdt");
               		return -1;
        	}
			if ( shmdt(count) == -1  ){
               		perror("shmdt");
               		return -1;
        	}
		//------------semaphores delete--------------------
		if ( semctl(sem_id,0, SETVAL,NULL) ==-1){			
                perror("semctl");
                	return -1;
		}

		if ( semctl(sem_id,1, SETVAL,NULL) ==-1){		
                perror("semctl");
                	return -1;
		}
		if ( semctl(sem_id,2, SETVAL,NULL)==-1) {			
		        perror("semctl");
		        return -1;
		}
	
		

		







}
}
void up(int x,int num){
  struct sembuf oper[1]={num,1,0};
  semop(x,&oper[0],1);
}

void down(int y,int num){
  struct sembuf oper[1]={num,-1,0};
  semop(y,&oper[0],1);
}
