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

int HP_CreateFile(char *fileName);
int HP_OpenFile(char *fileName);
int HP_CloseFile(int fileDesc);
int HP_InsertEntry(int fileDesc, Record record);
int HP_DeleteEntry(int fileDesc, char* fieldName, void *value);
void HP_GetAllEntries(int fileDesc, char* fieldName, void *value);