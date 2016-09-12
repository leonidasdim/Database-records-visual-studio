#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "BF.h"
#include "Record.h"

int recordIsNull(Record rec)
{
	if((rec.id==0)&&(strcmp(rec.name,"")==0)&&(strcmp(rec.surname,"")==0)&&(strcmp(rec.city,"")==0))
		return 1;
	else
		return -1;
}

int checkRecord(Record rec, char* fieldName, void* value)
{
	int *id;
	char* str;

	if(strcmp(fieldName,"id")==0)
	{
		id=(int*)value;
		if(rec.id==(*id))
			return 1;
		else
			return 0;
	}
    else if(strcmp(fieldName,"name")==0)
	{
		str=(char*)value;
		if(strcmp(str,rec.name)==0)
			return 1;
		else
			return 0;
	}
	else if(strcmp(fieldName,"surname")==0)
	{
		str=(char*)value;
		if(strcmp(str,rec.surname)==0)
			return 1;
		else
			return 0;
	}
	else if(strcmp(fieldName,"city")==0)
	{
		str=(char*)value;
		if(strcmp(str,rec.city)==0)
			return 1;
		else
			return 0;
	}
	else
		return -1;
}

int binarySearch(int fileDesc, int lastBlockNum, int id, char mode, Record record, int *recNum, int *blocksRead)
{
	void *block, *newBlock, *lowLimitBlock, *highLimitBlock;
	int low,high,curBlockNum,numRecsInBlock,checkLeft,checkRight;
	int curBlockNumTemp,i,isNull,exit,changed;
	int found,lowLimitBlockNum,lowLimitOffset,highLimitBlockNum,highLimitOffset;
	Record *curRec, *curRec2, lastRec, newRec, *newBlockRec, *lowRec, *highRec, tempRec;

	low=1;
	high=lastBlockNum;

	numRecsInBlock = BLOCK_SIZE / SIZE_OF_REC;

	while(low<=high)
	{
		curBlockNum=(low+high)/2;
		if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
			return -1;
		(*blocksRead)++;
		curRec=(Record*)block;

		if(curRec->id > id)
			high--;
		else if(curRec->id < id)
		{
			curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
			isNull=recordIsNull(*curRec);
			if(isNull==1)
			{
				checkLeft=0;
				checkRight=0;
				break;
			}
			else
			{
				if(curRec->id > id)//an uparxei h zhtoumenh eggrafh tha brisketai MONO sto block auto
				{
					checkLeft=0;
					checkRight=0;
					break;
				}
				else if(curRec->id == id)
				{
					checkLeft=0;
					checkRight=1;
					break;
				}
				else//if(curRec->id < id)
					low++;
			}
		}
		else //if(curRec->id == id)
		{
			checkLeft=1;
			curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
			if(curRec->id > id)
				checkRight=0;
			else //if(curRec->id == id)
				checkRight=1;
			break;
		}
	}

	curRec=(Record*)block;
	if(mode=='g')//get all entries
	{
		for(i=0;i<numRecsInBlock;i++)
		{
			isNull=recordIsNull(*curRec);
			if(isNull!=1)
			{
				if(curRec->id == id)
				{
					(*recNum)++;
					printf("\nRecord %d : (%d, %s, %s, %s)\n", *recNum, curRec->id, curRec->name, curRec->surname, curRec->city);
				}
			}
			curRec++;
		}
		curBlockNumTemp=curBlockNum;
		if(checkLeft==1)
		{
			exit=0;
			while(1)
			{
				curBlockNum--;
				if(curBlockNum==0)
					break;
				if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
					return -1;
				(*blocksRead)++;
				curRec=(Record*)block;
				curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
				for(i=0;i<numRecsInBlock;i++)
				{
					if(curRec->id == id)
					{
						(*recNum)++;
						printf("\nRecord %d : (%d, %s, %s, %s)\n", *recNum, curRec->id, curRec->name, curRec->surname, curRec->city);
					}
					else
					{
						exit=1;
						break;
					}
					curRec--;
				}
				if(exit==1)
					break;
			}
		}
		curBlockNum=curBlockNumTemp;
		if(checkRight==1)
		{
			exit=0;
			while(1)
			{
				curBlockNum++;
				if(curBlockNum==lastBlockNum+1)
					break;
				if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
					return -1;
				(*blocksRead)++;
				curRec=(Record*)block;
				for(i=0;i<numRecsInBlock;i++)
				{
					isNull=recordIsNull(*curRec);
					if(isNull!=1)
					{
						if(curRec->id == id)
						{
							(*recNum)++;
							printf("\nRecord %d : (%d, %s, %s, %s)\n", *recNum, curRec->id, curRec->name, curRec->surname, curRec->city);
						}
						else
						{
							exit=1;
							break;
						}
					}
					else
					{
						exit=1;
						break;
					}
					curRec++;
				}
				if(exit==1)
					break;
			}
		}
	}
	else if(mode=='i')//insert entry
	{
		if(curBlockNum<lastBlockNum)
		{
			curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
			lastRec.id=curRec->id;
			strcpy(lastRec.name,curRec->name);
			strcpy(lastRec.surname,curRec->surname);
			strcpy(lastRec.city,curRec->city);

			curRec2=curRec-1;//proteleutaia eggrafh tou block

			for(i=0;i<numRecsInBlock-1;i++)
			{
				if(curRec2->id > id)
				{
					curRec->id=curRec2->id;
					strcpy(curRec->name,curRec2->name);
					strcpy(curRec->surname,curRec2->surname);
					strcpy(curRec->city,curRec2->city);
				}
				else
				{
					curRec->id=record.id;
					strcpy(curRec->name,record.name);
					strcpy(curRec->surname,record.surname);
					strcpy(curRec->city,record.city);
					break;
				}
				curRec--;
				curRec2--;
			}
			newRec.id=lastRec.id;
			strcpy(newRec.name,lastRec.name);
			strcpy(newRec.surname,lastRec.surname);
			strcpy(newRec.city,lastRec.city);

			if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
				return -1;
			curBlockNum++;
			while(curBlockNum<lastBlockNum)
			{
				if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
					return -1;
				(*blocksRead)++;
				curRec=(Record*)block;
				curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
				lastRec.id=curRec->id;
				strcpy(lastRec.name,curRec->name);
				strcpy(lastRec.surname,curRec->surname);
				strcpy(lastRec.city,curRec->city);
				
				curRec2=curRec-1;//proteleutaia eggrafh tou block

				for(i=0;i<numRecsInBlock-1;i++)
				{
					curRec->id=curRec2->id;
					strcpy(curRec->name,curRec2->name);
					strcpy(curRec->surname,curRec2->surname);
					strcpy(curRec->city,curRec2->city);

					curRec--;
					curRec2--;
				}
				curRec->id=newRec.id;
				strcpy(curRec->name,newRec.name);
				strcpy(curRec->surname,newRec.surname);
				strcpy(curRec->city,newRec.city);

				newRec.id=lastRec.id;
				strcpy(newRec.name,lastRec.name);
				strcpy(newRec.surname,lastRec.surname);
				strcpy(newRec.city,lastRec.city);

				if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
					return -1;
				curBlockNum++;
			}
			if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
				return -1;
			(*blocksRead)++;

			//curBlockNum==lastBlockNum
			curRec=(Record*)block;
			curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
			isNull=recordIsNull(*curRec);
			if(isNull!=1)
			{//to teleutaio block einai gemato, opote antigrafoume thn teleutaia eggrafh tou se neo block
				if (BF_AllocateBlock(fileDesc) < 0) 
					return -1;
				if (BF_ReadBlock(fileDesc, lastBlockNum+1, &newBlock) < 0) //2o block, pou einai to prwto block pou tha periexei dedomena
					return -1;
				(*blocksRead)++;

				newBlockRec=(Record*)newBlock;
				newBlockRec->id=curRec->id;
				strcpy(newBlockRec->name,curRec->name);
				strcpy(newBlockRec->surname,curRec->surname);
				strcpy(newBlockRec->city,curRec->city);
				if (BF_WriteBlock(fileDesc, lastBlockNum+1) < 0)
					return -1;
			}
			curRec->id=newRec.id;
			strcpy(curRec->name,newRec.name);
			strcpy(curRec->surname,newRec.surname);
			strcpy(curRec->city,newRec.city);

			curRec2=curRec-1;//proteleutaia eggrafh tou block
			for(i=0;i<numRecsInBlock-1;i++)
			{
				isNull=recordIsNull(*curRec2);
				if((curRec2->id > curRec->id)||(isNull==1))
				{
					tempRec.id=curRec->id;
					strcpy(tempRec.name,curRec->name);
					strcpy(tempRec.surname,curRec->surname);
					strcpy(tempRec.city,curRec->city);

					curRec->id=curRec2->id;
					strcpy(curRec->name,curRec2->name);
					strcpy(curRec->surname,curRec2->surname);
					strcpy(curRec->city,curRec2->city);

					curRec2->id=tempRec.id;
					strcpy(curRec2->name,tempRec.name);
					strcpy(curRec2->surname,tempRec.surname);
					strcpy(curRec2->city,tempRec.city);
				}
				curRec--;
				curRec2--;
			}
			if (BF_WriteBlock(fileDesc, lastBlockNum) < 0)
				return -1;
		}
		else if(curBlockNum==lastBlockNum)
		{
			exit=0;
			curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
			isNull=recordIsNull(*curRec);
			if(isNull!=1)
			{//to teleutaio block einai gemato
				if (BF_AllocateBlock(fileDesc) < 0) 
					return -1;
				if (BF_ReadBlock(fileDesc, lastBlockNum+1, &newBlock) < 0) //2o block, pou einai to prwto block pou tha periexei dedomena
					return -1;
				(*blocksRead)++;

				newBlockRec=(Record*)newBlock;
				if(curRec->id > record.id)
				{
					newBlockRec->id=curRec->id;
					strcpy(newBlockRec->name,curRec->name);
					strcpy(newBlockRec->surname,curRec->surname);
					strcpy(newBlockRec->city,curRec->city);

					curRec->id=record.id;
					strcpy(curRec->name,record.name);
					strcpy(curRec->surname,record.surname);
					strcpy(curRec->city,record.city);
				}
				else
				{
					newBlockRec->id=record.id;
					strcpy(newBlockRec->name,record.name);
					strcpy(newBlockRec->surname,record.surname);
					strcpy(newBlockRec->city,record.city);
					exit=1;
				}
				if (BF_WriteBlock(fileDesc, lastBlockNum+1) < 0)
					return -1;
			}
			else
			{//bazoume sth teleutaia thesi th nea eggrafh kai meta taksinomoume to block
				curRec->id=record.id;
				strcpy(curRec->name,record.name);
				strcpy(curRec->surname,record.surname);
				strcpy(curRec->city,record.city);
			}
			if(exit==0)
			{
				curRec2=curRec-1;//proteleutaia eggrafh tou block

				for(i=0;i<numRecsInBlock-1;i++)
				{
					isNull=recordIsNull(*curRec2);
					if((curRec2->id > curRec->id)||(isNull==1))
					{
						tempRec.id=curRec->id;
						strcpy(tempRec.name,curRec->name);
						strcpy(tempRec.surname,curRec->surname);
						strcpy(tempRec.city,curRec->city);

						curRec->id=curRec2->id;
						strcpy(curRec->name,curRec2->name);
						strcpy(curRec->surname,curRec2->surname);
						strcpy(curRec->city,curRec2->city);

						curRec2->id=tempRec.id;
						strcpy(curRec2->name,tempRec.name);
						strcpy(curRec2->surname,tempRec.surname);
						strcpy(curRec2->city,tempRec.city);
					}
					curRec--;
					curRec2--;
				}
			}
			if (BF_WriteBlock(fileDesc, lastBlockNum) < 0)
				return -1;
		}
	}
	else if(mode=='d')//delete entry
	{
		found=0;
		for(i=0;i<numRecsInBlock;i++)
		{
			isNull=recordIsNull(*curRec);
			if(isNull!=1)
			{
				if(curRec->id == id)
				{
					curRec->id=0;
					strcpy(curRec->name,"");
					strcpy(curRec->surname,"");
					strcpy(curRec->city,"");

					if(found==0)
					{
						found=1;
						lowLimitBlockNum=curBlockNum;
						lowLimitOffset=i;
					}
					highLimitBlockNum=curBlockNum;
					highLimitOffset=i;
				}
			}
			curRec++;
		}

		if(checkLeft==0)
		{			
			if(checkRight==1)
			{
				exit=0;
				while(1)
				{
					curBlockNum++;
					if(curBlockNum==lastBlockNum+1)
						break;
					if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
						return -1;
					changed=0;
					curRec=(Record*)block;
					for(i=0;i<numRecsInBlock;i++)
					{
						isNull=recordIsNull(*curRec);
						if(isNull!=1)
						{
							if(curRec->id == id)
							{
								changed=1;
								curRec->id=0;
								strcpy(curRec->name,"");
								strcpy(curRec->surname,"");
								strcpy(curRec->city,"");
								highLimitBlockNum=curBlockNum;
								highLimitOffset=i;
							}
							else
							{
								exit=1;
								break;
							}
						}
						else
						{
							exit=1;
							break;
						}
						curRec++;
					}
					if(changed==1)
					{
						if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
							return -1;
					}
					if(exit==1)
						break;
				}
			}
		}
		else//checkLeft==1
		{
			curBlockNumTemp=curBlockNum;
			exit=0;
			while(1)
			{
				curBlockNum--;
				if(curBlockNum==0)
					break;
				if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
					return -1;
				changed=0;
				curRec=(Record*)block;
				curRec+=numRecsInBlock-1;//teleutaia eggrafh tou block
				for(i=0;i<numRecsInBlock;i++)
				{
					if(curRec->id == id)
					{
						changed=1;
						curRec->id=0;
						strcpy(curRec->name,"");
						strcpy(curRec->surname,"");
						strcpy(curRec->city,"");
						lowLimitBlockNum=curBlockNum;
						lowLimitOffset=numRecsInBlock-1-i;
					}
					else
					{
						exit=1;
						break;
					}
					curRec--;
				}
				if(changed==1)
				{
					if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
						return -1;
				}
				if(exit==1)
					break;
			}

			curBlockNum=curBlockNumTemp;
			if(checkRight==1)
			{
				exit=0;
				while(1)
				{
					curBlockNum++;
					if(curBlockNum==lastBlockNum+1)
						break;
					if (BF_ReadBlock(fileDesc, curBlockNum, &block) < 0)
						return -1;
					changed=0;
					curRec=(Record*)block;
					for(i=0;i<numRecsInBlock;i++)
					{
						isNull=recordIsNull(*curRec);
						if(isNull!=1)
						{
							if(curRec->id == id)
							{
								changed=1;
								curRec->id=0;
								strcpy(curRec->name,"");
								strcpy(curRec->surname,"");
								strcpy(curRec->city,"");
								highLimitBlockNum=curBlockNum;
								highLimitOffset=i;
							}
							else
							{
								exit=1;
								break;
							}
						}
						else
						{
							exit=1;
							break;
						}
						curRec++;
					}
					if(changed==1)
					{
						if (BF_WriteBlock(fileDesc, curBlockNum) < 0)
							return -1;
					}
					if(exit==1)
						break;
				}
			}
		}

//se auto to shmeio exoun diagrafei oles oi eggrafes gia tis opoies isxuei h sinthiki.
//Twra tha prepei na kanoume mia metatopish wste na kalufthei to keno me NULL eggrafes
//pou exei dhmiourgithei sto diasthma (lowLimitBlock,lowLimitOffset) -> (highLimitBlock,highLimitOffset)

		if(found==1)
		{
			highLimitOffset++;
			if(highLimitOffset==numRecsInBlock)
			{
				highLimitBlockNum++;
				highLimitOffset=0;
			}
			if(highLimitBlockNum==lastBlockNum+1)
				return 0;
			if (BF_ReadBlock(fileDesc, highLimitBlockNum, &highLimitBlock) < 0)
				return -1;
			if (BF_ReadBlock(fileDesc, lowLimitBlockNum, &lowLimitBlock) < 0)
				return -1;
			
			lowRec=(Record*)lowLimitBlock;
			lowRec+=lowLimitOffset;
			highRec=(Record*)highLimitBlock;
			highRec+=highLimitOffset;
			while(1)
			{
				lowRec->id=highRec->id;
				strcpy(lowRec->name,highRec->name);
				strcpy(lowRec->surname,highRec->surname);
				strcpy(lowRec->city,highRec->city);

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
					if(highLimitBlockNum==lastBlockNum+1)
						return 0;
					if (BF_ReadBlock(fileDesc, highLimitBlockNum, &highLimitBlock) < 0)
						return -1;
					highRec=(Record*)highLimitBlock;
				}
				else
					highRec++;

				lowLimitOffset++;
				if(lowLimitOffset==numRecsInBlock)
				{
					if (BF_WriteBlock(fileDesc, lowLimitBlockNum) < 0)
						return -1;
					lowLimitBlockNum++;
					lowLimitOffset=0;
					if(lowLimitBlockNum==lastBlockNum+1)
						return 0;
					if (BF_ReadBlock(fileDesc, lowLimitBlockNum, &lowLimitBlock) < 0)
						return -1;
					lowRec=(Record*)lowLimitBlock;
				}
				else
					lowRec++;
			}
		}
	}
	return 0;
}

