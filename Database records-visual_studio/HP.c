#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "BF.h"
#include "HP.h"
#include "Record.h"

int HP_CreateFile(char *fileName)
{
	int fd;
	char ch;
	void* block;

	if(BF_CreateFile(fileName)<0)
		return -1;
	else
	{
		fd=BF_OpenFile(fileName);//to fd einai to anagnwristiko apo to BF epipedo
		if(fd<0)
			return -1;
		else
		{
			if(BF_AllocateBlock(fd)<0)//desmeusi tou 1ou block sto file gia to header
				return -1;

			if(BF_ReadBlock(fd, 0, &block) < 0) //diabazetai to 1o block (block 0) sth metablhth block
				return -1;
			ch = 'H';//heap file
			strncpy((char*)block, &ch, sizeof(char));
			if(BF_WriteBlock(fd, 0) < 0)
				return -1;
		}
	}
	return 0;
}

int HP_OpenFile(char *fileName)
{
	void* block;
	char* ch;
	int fd;

	fd=BF_OpenFile(fileName);
	if(fd<0)
		return -1;
	else
	{
		if (BF_ReadBlock(fd, 0, &block) < 0) //diabazetai to header block (block 0) sth metablhth block
			return -1;
		ch=(char*)block;
		if(*ch=='H')
			return fd;
		else
			return -1;//an den einai heap file, epistrefoume lathos
	}
}

int HP_CloseFile(int fileDesc)
{
	if (BF_CloseFile(fileDesc) < 0)
		return -1;
	else
		return 0;
}

int HP_InsertEntry(int fileDesc, Record record)
{
	int cnt, isNull, numRecsInBlock, i, found, blockNumToWrite, curBlockNum;
	void *block;
	Record *rec;

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	cnt = BF_GetBlockCounter(fileDesc);
	if(cnt==0)
		return -1;
	else if(cnt==1)//uparxei mono to header block
	{
		if (BF_AllocateBlock(fileDesc) < 0) 
			return -1;
		if (BF_ReadBlock(fileDesc, 1, &block) < 0) //2o block (block 1), pou einai to prwto block pou tha periexei dedomena
			return -1;
		rec=(Record*)block;
		rec->id=record.id;
		strcpy(rec->name,record.name);
		strcpy(rec->surname,record.surname);
		strcpy(rec->city,record.city);
		blockNumToWrite=1;
	}
	else if(cnt>1)//ektos apo to header block uparxoun kai block dedomenwn
	{
		curBlockNum=cnt-1;
		if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0) //diabazoume to teleutaio block
			return -1;
		found=0;
		rec=(Record*)block;
		for(i=0;i<numRecsInBlock;i++)
		{
			isNull=recordIsNull(*rec);
			if(isNull==1)
			{
				rec->id=record.id;
				strcpy(rec->name,record.name);
				strcpy(rec->surname,record.surname);
				strcpy(rec->city,record.city);
				found=1;
				blockNumToWrite=curBlockNum;
				break;
			}
			rec++;
		}
		if(found==0)//to teleutaio block einai gemato
		{
			if (BF_AllocateBlock(fileDesc) < 0) 
				return -1;
			if (BF_ReadBlock(fileDesc, curBlockNum+1, &block) < 0) //to neo teleutaio block, pou einai adeio
				return -1;
			rec=(Record*)block;
			rec->id=record.id;
			strcpy(rec->name,record.name);
			strcpy(rec->surname,record.surname);
			strcpy(rec->city,record.city);
			blockNumToWrite=curBlockNum+1;
		}
	}
	if(blockNumToWrite>0)
	{
		if (BF_WriteBlock(fileDesc, blockNumToWrite) < 0)
			return -1;
		else
			return 0;
	}
	else
		return -1;
}

int HP_DeleteEntry(int fileDesc, char* fieldName, void *value)
{
	int totblockNums, isNull, numRecsInBlock, curRecOffset, lastRecOffset, equal, found, i, j, curBlockNum, lastBlockNum, changed;
	void *block, *blockLast;
	Record *curRec, *lastRec;
	int *deletedRecsTable;

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	totblockNums = BF_GetBlockCounter(fileDesc);

	deletedRecsTable = (int*) malloc(totblockNums*sizeof(int));//se auton ton pinaka kratame gia kathe block dedomenwn, to plithos twn eggrafwn pou diagrapsame.
													  //me eksairesh to teleutaio block dedomenwn, gia to opoio kratame to plithos twn mh NULL eggrafwn
													  //pou emeinan meta th diagrafh!!

	if(totblockNums<=1)//den exei block dedomenwn
		return 0;
	else//exei block dedomenwn
	{
		curBlockNum=1;//to 0 to agnooume giati einai to header block
		deletedRecsTable[curBlockNum]=0;
		while(curBlockNum<totblockNums-1)//ola ta block dedomenwn, ektos apo to teleutaio
		{
			if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
				return -1;
			curRec=(Record*)block;

			deletedRecsTable[curBlockNum]=0;
			changed=0;
			for(i=0;i<numRecsInBlock;i++)
			{
				equal=checkRecord(*curRec,fieldName,value);
				if(equal==1)
				{
					curRec->id=0;
					strcpy(curRec->name,"");
					strcpy(curRec->surname,"");
					strcpy(curRec->city,"");
					deletedRecsTable[curBlockNum]++;
					changed=1;
				}
				curRec++;
			}
			if(changed==1)
			{
				if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
					return -1;
			}
			curBlockNum++;
		}

		//diabazoume to teleutaio block dedomenwn
		if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
			return -1;
		
		curRec=(Record*)block;

		changed=0;
		for(i=0;i<numRecsInBlock;i++)
		{
			isNull=recordIsNull(*curRec);
			if(isNull!=1)
			{
				equal=checkRecord(*curRec,fieldName,value);
				if(equal==1)//isxuei h sinthiki, opote diagrafetai h eggrafh
				{
					curRec->id=0;
					strcpy(curRec->name,"");
					strcpy(curRec->surname,"");
					strcpy(curRec->city,"");
					changed=1;
				}
			}
			curRec++;
		}
		if(changed==1)
		{
			if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
				return -1;
		}

//se auto to shmeio exoun diagrafei apola ta block dedomenwn oi apaitoumenes eggrafes, alla exoun dhmiourgithei "kena" apo NULL eggrafes
//kai tha prepei na tis "kalupsoume" me mh NULL eggrafes ksekinwntas apo to telos tou arxeiou
		curBlockNum=1;
		while(curBlockNum<totblockNums-1)//ola ta block dedomenwn, ektos apo to teleutaio
		{			
			if(deletedRecsTable[curBlockNum]>0)
			{
				if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
					return -1;
				curRec=(Record*)block;
				curRecOffset=0;

				changed=0;
				
				lastBlockNum=totblockNums-1;
				if (BF_ReadBlock(fileDesc, lastBlockNum, &blockLast) < 0)
					return -1;
				lastRecOffset=numRecsInBlock-1;
				lastRec=(Record*)blockLast;
				lastRec += lastRecOffset;//pame sthn teleutaia eggrafh tou teleutaiou block

				for(j=0;j<deletedRecsTable[curBlockNum];j++)
				{
					//psaxnoume gia thn epomenh "kenh" eggrafh
					while(curRecOffset<numRecsInBlock)
					{
						isNull=recordIsNull(*curRec);
						if(isNull==1)
						{
							break;
						}
						curRecOffset++;
						curRec++;
					}
					if(curRecOffset==numRecsInBlock)
						break;

					found=0;
					while(1)
					{
						while(lastRecOffset>=0)
						{
							isNull=recordIsNull(*lastRec);
							if(isNull!=1)
							{
								curRec->id=lastRec->id;
								strcpy(curRec->name,lastRec->name);
								strcpy(curRec->surname,lastRec->surname);
								strcpy(curRec->city,lastRec->city);

								lastRec->id=0;
								strcpy(lastRec->name,"");
								strcpy(lastRec->surname,"");
								strcpy(lastRec->city,"");

								found=1;
								changed=1;
								deletedRecsTable[lastBlockNum]--;
								break;
							}
							else
							{
								lastRec--;
								lastRecOffset--;
							}
						}
						if(found==1)
						{
							curRecOffset++;
							curRec++;
							break;
						}
						else
						{
							if(changed==1)
							{
								if (BF_WriteBlock(fileDesc, lastBlockNum) < 0)
									return -1;
								changed=0;
							}
							lastBlockNum--;
							if(lastBlockNum>curBlockNum)
							{
								if (BF_ReadBlock(fileDesc, lastBlockNum, &blockLast) < 0)
									return -1;
								lastRecOffset=numRecsInBlock-1;
								lastRec=(Record*)blockLast;
								lastRec += lastRecOffset;//pame sthn teleutaia eggrafh tou block
								changed=0;
							}
							else//ftasame sto trexon block
							{
								break;
							}
						}
					}
					if(lastBlockNum==curBlockNum)
					{
						lastRecOffset=numRecsInBlock-1;
						lastRec=(Record*)block;
						lastRec += lastRecOffset;//pame sthn teleutaia eggrafh tou current block

						while(lastRecOffset>curRecOffset)
						{
							isNull=recordIsNull(*lastRec);
							if(isNull!=1)
							{
								curRec->id=lastRec->id;
								strcpy(curRec->name,lastRec->name);
								strcpy(curRec->surname,lastRec->surname);
								strcpy(curRec->city,lastRec->city);

								lastRec->id=0;
								strcpy(lastRec->name,"");
								strcpy(lastRec->surname,"");
								strcpy(lastRec->city,"");

								found=1;
								changed=1;
								break;
							}
							else
							{
								lastRec--;
								lastRecOffset--;
							}
						}
					}
				}
				if(changed==1)
				{
					if (BF_WriteBlock(fileDesc, lastBlockNum) < 0)
						return -1;
					changed=0;
				}
				if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
					return -1;
			}
			curBlockNum++;
		}

		//teleutaio block
		lastBlockNum=totblockNums-1;
		if (BF_ReadBlock(fileDesc, lastBlockNum, &blockLast) < 0)
			return -1;
		lastRecOffset=numRecsInBlock-1;
		lastRec=(Record*)blockLast;
		lastRec += lastRecOffset;//pame sthn teleutaia eggrafh tou teleutaiou block
		
		curRecOffset=0;
		curRec=(Record*)blockLast;
		while(curRecOffset<lastRecOffset)
		{
			while(curRecOffset<numRecsInBlock)
			{
				isNull=recordIsNull(*curRec);
				if(isNull==1)
					break;
				else
				{
					curRecOffset++;
					curRec++;
				}			
			}
			if(curRecOffset==numRecsInBlock)
				break;

			while(lastRecOffset>=0)
			{
				isNull=recordIsNull(*lastRec);
				if(isNull!=1)
					break;
				else
				{
					lastRecOffset--;						
					lastRec--;
				}
			}
			if(lastRecOffset<0)
				break;

			if(curRecOffset>=lastRecOffset)
				break;
			else
			{
				curRec->id=lastRec->id;
				strcpy(curRec->name,lastRec->name);
				strcpy(curRec->surname,lastRec->surname);
				strcpy(curRec->city,lastRec->city);

				lastRec->id=0;
				strcpy(lastRec->name,"");
				strcpy(lastRec->surname,"");
				strcpy(lastRec->city,"");

				curRecOffset++;
				curRec++;

				lastRecOffset--;						
				lastRec--;
			}
		}
	}
	return 0;
}

void HP_GetAllEntries(int fileDesc, char* fieldName, void *value)
{
	int totblockNums, numRecsInBlock, equal, i, isNull, curBlockNum, recNum=0, blocksRead=0;
	void *block;
	Record *curRec;

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	totblockNums = BF_GetBlockCounter(fileDesc);

	if(totblockNums>1)//exei block dedomenwn
	{
		curBlockNum=1;//to 0 to agnooume giati einai to header block
		while(curBlockNum<totblockNums)//ola ta block dedomenwn
		{
			if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
				return;
			blocksRead++;
			curRec=(Record*)block;

			for(i=0;i<numRecsInBlock;i++)
			{
				isNull=recordIsNull(*curRec);
				if(isNull!=1)
				{
					if(value==NULL)
					{
						recNum++;
						printf("\nRecord %d : (%d, %s, %s, %s)\n", recNum, curRec->id, curRec->name, curRec->surname, curRec->city);
					}
					else
					{
						equal=checkRecord(*curRec,fieldName,value);
						if(equal==1)//ektupwnetai mono an isxuei h sinthiki
						{
							recNum++;
							printf("\nRecord %d : (%d, %s, %s, %s)\n", recNum, curRec->id, curRec->name, curRec->surname, curRec->city);
						}
					}
				}
				curRec++;
			}
			curBlockNum++;
		}
	}
	if(recNum==0)
		printf("\nNo data found for this selection\n");
	printf("\nNumber of blocks read: %d\n",blocksRead);
}