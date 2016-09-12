#ifndef __Record_H
#define __Record_H
typedef struct
{
	int id;
	char name[15];
	char surname[20];
	char city[10];
}Record;
#define SIZE_OF_REC sizeof(Record)
#endif

int recordIsNull(Record rec);
int checkRecord(Record rec, char* fieldName, void* value);
int binarySearch(int fileDesc, int lastBlockNum, int id, char mode, Record record, int *recNum, int *blocksRead);


