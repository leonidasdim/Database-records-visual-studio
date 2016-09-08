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

typedef struct{				
	int b;				//number of line
	char line[120];			// array char for line 
	int c;				//number of Ci
	
	}grammi;
grammi *grammi2,*grammi3;		


shm_id1=shmget (SHMKEY1,sizeof(grammi),PERM|IPC_CREAT);			//shared memory 1  size of struct
	if(shm_id1==-1)
                printf ("erorr shared memory 1 \n");
        else{
                printf ("\nshared memory1 succesfull\n");		//copy adrres pointer "grammi2" to shared memory
                grammi2= shmat(shm_id1, (grammi*)0, 0);	
        }
shm_id2=shmget (SHMKEY2,sizeof(grammi),PERM|IPC_CREAT);
	if(shm_id2==-1)
                printf ("\nerorr shared memory 2\n");
        else{
                printf ("\nshared memory2 succesfull\n");
                grammi3= shmat(shm_id1, (grammi*)0, 0);			//copy addres pointer"grammi" to shared memory 2
        }

	



union senum{					//struct of shamaphores
  int val;
  struct semid_ds *buff;
  unsigned short * array;
};


union senum arg0,arg1,arg2,arg3;


sem_id=semget(SHMKEY1,3,PERM | IPC_CREAT);		//create array of 3 shamaphores for shared memory 1
	
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
	


sem_id2=semget(SHMKEY2,1,PERM | IPC_CREAT);		//create 1 semaphore for shared memory 2
	arg3.val=0;
	
	



	if (semctl(sem_id2,0, SETVAL, arg3) <0) {			
                perror("semctl");
                return -1;
        }

        
	pid=fork();
	if(!pid){				//processes S chlid  D
		sleep(1);	
		for(k=1;k=-1;k++){			//"for" break only if end all Ci
		
			down(sem_id2,0);		//block S wait D to up 
			
		printf("eimai h diergasia S \n");
		printf("o arithmos einai %d \n",grammi3->b);

			fp=fopen(argv[1],"r");					//open file
			fseek(fp, 0, SEEK_SET);					//seek to start file
			for(i = 0; !feof(fp) && i <= grammi3->b; i++)		// find the line
        		fgets(seira,sizeof(seira), fp);

    			printf("%s", seira);
			strncpy(grammi3->line,seira,sizeof(seira));		//copy to shared memory  2 
				fclose(fp);
									

			grammi3->c--;						//count Ci
			if(grammi3->c==0){
					up(sem_id2,0);
				break;}
				up(sem_id2,0);              //unblock D
		sleep(1);							
		
					}
		


	}
	else {
			for(n=1;n=-1;n++){					//break only if ends Ci
			down(sem_id,0);				// block D wait up Ci
			if(n==1){				//if first copy the number of Ci
			end=grammi2->c;
			grammi3->c=end;}
			printf("eimai i diergasia D \n");
			lines=grammi2->b;				
			grammi3->b=lines;
			up(sem_id2,0);					//up processes S 
			down(sem_id2,0);			// Blocked D wait  S 
			printf("eimai pali i diergasia D \n");
			strncpy(grammi2->line,grammi3->line,sizeof(seira));		
			
			up(sem_id,2);						//up Ci
			
			if(grammi3->c==0){
				
				break;}
			sleep(1);
			
			
				


						}
		

			wait(&status);				//wait childrens warning :zombie state
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

void up(int x,int num){				//function for  up semaphores
  struct sembuf oper[1]={num,1,0};
  semop(x,&oper[0],1);
}

void down(int y,int num){			//function for	down  semaphores
  struct sembuf oper[1]={num,-1,0};
  semop(y,&oper[0],1);
}








