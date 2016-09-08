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
#include <sys/time.h>
#define SHMKEY1 (key_t) 8821
#define SHMKEY2 (key_t) 88
#define PERM 0666

void up(int x,int num);
void down(int y,int num);


int main(int argc,char **argv){
if(argc !=2){

printf("dwse ena txt arxeio san orisma proteinomeno wonderwall.txt\n");
	
	return -1;

}
FILE *fp;
int shm_id1,shm_id2,sem_id,sem_id2,k,pid,status,lines,n,i,end=100;
char seira[120];

typedef struct{				//struct for memory
	int b;				//number line
	char line[120];			// save the line of text 
	int c;				//the number of clients (for end)
	
	}grammi;
grammi *grammi2,*grammi3;		


shm_id1=shmget (SHMKEY1,sizeof(grammi),PERM|IPC_CREAT);			//create shared memory size of struct
	if(shm_id1==-1)
                printf ("erorr shared memory 1 \n");
        else{
                printf ("\nshared memory1 succesfull\n");		//pointer gramme2 to shared memory 1
                grammi2= shmat(shm_id1, (grammi*)0, 0);	
        }
shm_id2=shmget (SHMKEY2,sizeof(grammi),PERM|IPC_CREAT);
	if(shm_id2==-1)
                printf ("\nerorr shared memory 2\n");
        else{
                printf ("\nshared memory2 succesfull\n");
                grammi3= shmat(shm_id1, (grammi*)0, 0);			//pointer grammi2 to other  memory
        }

	



union senum{					//struct semaphores
  int val;
  struct semid_ds *buff;
  unsigned short * array;
};


union senum arg0,arg1,arg2,arg3;


sem_id=semget(SHMKEY1,3,PERM | IPC_CREAT);		//create array of  3 semaphores -> shared memmory 1
	
	arg0.val=0;
	arg1.val=1;
	arg2.val=0;
	
	if (semctl(sem_id,0, SETVAL, arg0) <0) {			
                perror("semctl");
                return -1;
        }
	if (semctl(sem_id,1, SETVAL, arg1) <0) {			
                perror("semctl");
                return -1;
        }
	if (semctl(sem_id,2, SETVAL, arg2) <0) {			
                perror("semctl");
                return -1;
        }
	


sem_id2=semget(SHMKEY2,1,PERM | IPC_CREAT);		//create 1 semaphore -> shared memory 2nd
	arg3.val=0;
	
	



	if (semctl(sem_id2,0, SETVAL, arg3) <0) {			
                perror("semctl");
                return -1;
        }

        
	pid=fork();
	if(!pid){				//processes S and child D
		sleep(1);	
		for(k=1;k=-1;k++){			//"for" after Ci end  break 
		
			down(sem_id2,0);		//block S wait up D
			
		printf("eimai h diergasia S \n");
		printf("o arithmos einai %d \n",grammi3->b);

			fp=fopen(argv[1],"r");					//open file
			fseek(fp, 0, SEEK_SET);					//seek at start
			for(i = 0; !feof(fp) && i <= grammi3->b; i++)		// find the line  and copy to array line[]
        		fgets(seira,sizeof(seira), fp);

    			printf("%s", seira);
			strncpy(grammi3->line,seira,sizeof(seira));		//copy to the shared memory 2 
				fclose(fp);
									

			grammi3->c--;						//Ci--
			if(grammi3->c==0){
					up(sem_id2,0);
				break;}
				up(sem_id2,0);              //end with shared memoy 2nd then unblock D
		sleep(1);							
		
					}
		


	}
	else {
			for(n=1;n=-1;n++){					//"for" after Ci end  break 
			down(sem_id,0);				// block  D  wait  up from  Ci
			if(n==1){				//if first  copy number of Ci for shared memory 1st to shared memory 2nd
			end=grammi2->c;
			grammi3->c=end;}
			printf("eimai i diergasia D \n");
			lines=grammi2->b;				// copy line for shared memory 1 to shared memory 2
			grammi3->b=lines;
			up(sem_id2,0);					//unblock procese S 
			down(sem_id2,0);			// Block D  wait for end S  then can use shared memory 2
			printf("eimai pali i diergasia D \n");
			strncpy(grammi2->line,grammi3->line,sizeof(seira));		//copy line for shared memory 2 -> shared memory 1
			
			up(sem_id,2);						//up Ci
			
			if(grammi3->c==0){
				
				break;}
			sleep(1);
			
			
				


						}
		

			wait(&status);				//wait child   warning : zombie state
			printf("teliwsamee!!!!!\n");
			
	
//-----------------shared memory 2 delete  -------------
		if ( shmctl(shm_id2,IPC_RMID,NULL) == -1  ){	
               		perror("shmctl");
               		return -1;
        	}

		if ( shmdt(grammi3) == -1  ){			
               		perror("shmdt");
               		return -1;
        	}
		// shemaphore shared memory 2 delete--------

		if ( semctl(sem_id2,0, SETVAL,NULL)==-1) {		
		        perror("semctl");
		        return -1;
		}
		
				



				}


	

	


	
}

void up(int x,int num){				//function "up" for semaphores
  struct sembuf oper[1]={num,1,0};
  semop(x,&oper[0],1);
}

void down(int y,int num){			//functio "down" for semaphores
  struct sembuf oper[1]={num,-1,0};
  semop(y,&oper[0],1);
}








