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

int Sorted_CreateFile(char *fileName);
int Sorted_OpenFile(char *fileName);
int Sorted_CloseFile(int fileDesc);
int Sorted_InsertEntry(int fileDesc, Record record);
int Sorted_DeleteEntry(int fileDesc, char* fieldName, void *value);
void Sorted_GetAllEntries(int fileDesc, char* fieldName, void *value);