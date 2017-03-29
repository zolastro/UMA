#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "Scheduler.h"

void Create(Scheduler *plf){
  // Protip: We are using real TList instead of pointers to TList, so we use name.property to access a property
  (*plf).high = NULL;
  (*plf).low = NULL;
}

void Show(Scheduler plf){
  //The high priority list is always connected to the low priority list
  TList listH = plf.high;
  int i = 0;
  printf("- SCHEDULER CONTENTS ------\n");
  while (listH != NULL) {
    printf("%d. Process: '%s' Priority: %c\n", i, listH->name, listH->type);
    listH = listH->next;
    i++;
  }
  printf("---------------------------\n");
}


void InsertFirst(TList* plist, char* name, char type){
  TList aux = (TList)malloc(sizeof(TProc));
  aux->type = type;
  aux->name = (char*)malloc(sizeof(strlen(name)+1));
  strcpy(aux->name, name);
  aux->next = *plist;
  *plist = aux;
}

void Insert(Scheduler* plf, char* name, char type){
  if(type == 'A'){
    InsertFirst(&((*plf).high), name, type);
  }else if(type == 'B'){
    InsertFirst(&((*plf).low), name, type);
  }else{
    printf("Error, invalid type: %c\n", type);
    exit(-1);
  }
}

void DestroyList(TList* plist){
  TList aux;
  while(*plist != NULL){
    aux = *plist;
    *plist = (*plist)->next;
    free(aux->name);
    free(aux);
    aux = NULL;
  }
  *plist = NULL;
}

void Destroy(Scheduler *plf){
  DestroyList(&((*plf).low));
  DestroyList(&((*plf).high));
}

void ReadFromFile(char *namef, Scheduler *plf){

  FILE * fin = fopen(namef, "rb");
  unsigned long nProcesses;
  //Read the first value (an unsigned long)
  fread(&nProcesses, 1, 4, fin);
  //After looooooots of debuging, figured out that there was a 4bytes blank space...
  char *foo [4];
  fread(foo, 1, 4, fin);
  // printf("NProcesses: %lu\n",nProcesses);
  int i;
  for(i = 0; i < nProcesses; i++){
    char str [20];
    fread(str, sizeof(char), 20, fin);
    int j;
    // printf("Name: %s\n", str);
    char type;
    fread(&type, sizeof(char), 1, fin);
    Insert(plf, str, type);
  }
  fclose(fin);
}

void RemoveHighProc(Scheduler *plf, char *name){
  TList ptr = (*plf).high;
  TList prev = NULL;
  while (ptr != NULL && ptr->type != 'B') {
    // printf("%s\n", ptr->name);
    //Compare our target string with the current string
    if(strcmp(ptr->name, name) == 0){
        if(prev == NULL){       // If it's the first node, we have to move the start of the list
          (*plf).high = ptr->next;
        }else{                 // Otherwise, we have to connect the previous node with the next one
          prev->next = ptr->next;
        }
        free(ptr);
        ptr = NULL;
        //Exit loop
        break;
    }
    prev = ptr;
    ptr = ptr->next;
  }
}
