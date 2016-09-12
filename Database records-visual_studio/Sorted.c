#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "BF.h"
#include "Sorted.h"
#include "Record.h"

int Sorted_CreateFile(char *fileName)
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

			if (BF_ReadBlock(fd, 0, &block) < 0) //diabazetai to 1o block (block 0) sth metablhth block
				return -1;
			ch = 'S';//sorted file
			strncpy((char*)block, &ch, sizeof(char));
			if (BF_WriteBlock(fd, 0) < 0)
				return -1;
		}
	}
	return 0;
}

int Sorted_OpenFile(char *fileName)
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
		if(*ch=='S')//sorted file
			return fd;
		else
			return -1;
	}
}

int Sorted_CloseFile(int fileDesc)
{
	if (BF_CloseFile(fileDesc) < 0)
		return -1;
	else
		return 0;
}

int Sorted_InsertEntry(int fileDesc, Record record)
{
	int totblockNums, recNum, blocksRead, ret;
	void *block;
	Record *rec;

	totblockNums = BF_GetBlockCounter(fileDesc);
	if(totblockNums==1)//uparxei mono to header block
	{
		if (BF_AllocateBlock(fileDesc) < 0) 
			return -1;
		if (BF_ReadBlock(fileDesc, 1, &block) < 0) //2o block, pou einai to prwto block pou tha periexei dedomena
			return -1;
		rec=(Record*)block;
		rec->id=record.id;
		strcpy(rec->name,record.name);
		strcpy(rec->surname,record.surname);
		strcpy(rec->city,record.city);
		
		if (BF_WriteBlock(fileDesc, 1) < 0)
			return -1;
		return 0;
	}
	else
	{
		ret=binarySearch(fileDesc,totblockNums-1,record.id,'i',record,&recNum,&blocksRead);
		return ret;
	}
}

int Sorted_DeleteEntry(int fileDesc, char* fieldName, void *value)
{
	int totblockNums, numRecsInBlock, equal, isNull, recNum=0, blocksRead=0, *id, ret;
	int found, lowLimitBlockNum, lowLimitOffset, highLimitBlockNum, highLimitOffset;
	void *lowLimitBlock, *highLimitBlock;
	Record emptyRec, *lowRec, *highRec;

	emptyRec.id=0;
	strcpy(emptyRec.name,"");
	strcpy(emptyRec.surname,"");
	strcpy(emptyRec.city,"");

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	totblockNums = BF_GetBlockCounter(fileDesc);

	if(totblockNums<=1)//den exei block dedomenwn
		return 0;
	else//exei block dedomenwn
	{
		if(strcmp(fieldName,"id")==0)//ginetai duadikh anazhthsh
		{
			id=(int*)value;
			ret=binarySearch(fileDesc,totblockNums-1,*id,'d',emptyRec,&recNum,&blocksRead);
			return ret;
		}
		else//seiriakh anazhthsh
		{
			found=0;
			lowLimitBlockNum=1;//to 0 to agnooume giati einai to header block
			lowLimitOffset=0;
			if (BF_ReadBlock(fileDesc, lowLimitBlockNum, &lowLimitBlock) < 0)
				return -1;
			lowRec=(Record*)lowLimitBlock;
			while(found==0)
			{
				isNull=recordIsNull(*lowRec);
				if(isNull!=1)
				{
					equal=checkRecord(*lowRec,fieldName,value);
					if(equal==1)
					{
						lowRec->id=0;
						strcpy(lowRec->name,"");
						strcpy(lowRec->surname,"");
						strcpy(lowRec->city,"");
						found=1;
					}
					else
					{
						lowRec++;
						lowLimitOffset++;
						if(lowLimitOffset==numRecsInBlock)
						{
							lowLimitBlockNum++;
							lowLimitOffset=0;
							if(lowLimitBlockNum==totblockNums)
								return 0;
							if (BF_ReadBlock(fileDesc, lowLimitBlockNum, &lowLimitBlock) < 0)
								return -1;
							lowRec=(Record*)lowLimitBlock;
						}
					}
				}
				else
					return 0;
			}
			if(found==1)
			{
				highLimitBlockNum=lowLimitBlockNum;
				highLimitOffset=lowLimitOffset+1;
				if(highLimitOffset==numRecsInBlock)
				{
					highLimitBlockNum++;
					highLimitOffset=0;
				}
				if(highLimitBlockNum==totblockNums)
					return 0;
				if (BF_ReadBlock(fileDesc, highLimitBlockNum, &highLimitBlock) < 0)
					return -1;
				highRec=(Record*)highLimitBlock;
				highRec+=highLimitOffset;

				while(1)
				{
					equal=checkRecord(*highRec,fieldName,value);
					while(equal==1)
					{
						highRec->id=0;
						strcpy(highRec->name,"");
						strcpy(highRec->surname,"");
						strcpy(highRec->city,"");
						highLimitOffset++;
						if(highLimitOffset==numRecsInBlock)
						{
							if (BF_WriteBlock(fileDesc, highLimitBlockNum) < 0)
								return -1;
							highLimitBlockNum++;
							highLimitOffset=0;
							if(highLimitBlockNum==totblockNums)
								return 0;
							if (BF_ReadBlock(fileDesc, highLimitBlockNum, &highLimitBlock) < 0)
								return -1;
							highRec=(Record*)highLimitBlock;
						}
						else
							highRec++;
						equal=checkRecord(*highRec,fieldName,value);
					}

					lowRec->id=highRec->id;
					strcpy(lowRec->name,highRec->name);
					strcpy(lowRec->surname,highRec->surname);
					strcpy(lowRec->city,highRec->city);

					highRec->id=0;
					strcpy(highRec->name,"");
					strcpy(highRec->surname,"");
					strcpy(highRec->city,"");

					lowLimitOffset++;
					if(lowLimitOffset==numRecsInBlock)
					{
						if (BF_WriteBlock(fileDesc, lowLimitBlockNum) < 0)
							return -1;
						lowLimitBlockNum++;
						lowLimitOffset=0;
						if(lowLimitBlockNum==totblockNums)
							return 0;
						if (BF_ReadBlock(fileDesc, lowLimitBlockNum, &lowLimitBlock) < 0)
							return -1;
						lowRec=(Record*)lowLimitBlock;
					}
					else
						lowRec++;

					highLimitOffset++;
					if(highLimitOffset==numRecsInBlock)
					{
						if (BF_WriteBlock(fileDesc, highLimitBlockNum) < 0)
							return -1;
						highLimitBlockNum++;
						highLimitOffset=0;
						if(highLimitBlockNum==totblockNums)
							return 0;
						if (BF_ReadBlock(fileDesc, highLimitBlockNum, &highLimitBlock) < 0)
							return -1;
						highRec=(Record*)highLimitBlock;
					}
					else
						highRec++;
				}
			}
		}
	}
	return 0;
}

void Sorted_GetAllEntries(int fileDesc, char* fieldName, void *value)
{
	int totblockNums, numRecsInBlock, equal, i, isNull, curBlockNum, recNum=0, blocksRead=0, *val, id;
	void *block;
	Record *curRec, emptyRec;

	emptyRec.id=0;
	strcpy(emptyRec.name,"");
	strcpy(emptyRec.surname,"");
	strcpy(emptyRec.city,"");

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	totblockNums = BF_GetBlockCounter(fileDesc);

	if(totblockNums>1)//exei block dedomenwn
	{
		if((value!=NULL)&&((strcmp(fieldName,"id")==0)))//ginetai duadikh anazhthsh
		{
			val=(int*)value;
			id=*val;
			binarySearch(fileDesc,totblockNums-1,id,'g',emptyRec,&recNum,&blocksRead);
		}
		else//seiriakh anazhthsh h' ektupwnontai oles oi eggrafes tou arxeiou
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
							if(equal==1)
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
	}
	if(recNum==0)
		printf("\nNo data found for this selection\n");
	printf("\nNumber of blocks read: %d\n",blocksRead);
}