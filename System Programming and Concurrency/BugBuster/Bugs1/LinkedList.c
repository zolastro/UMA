#include "LinkedList.h"

/* Creates an empty List */

TList Create(){                     // Had to change the return type
    return NULL;
}

/* Insert in the head of the list */
// This function has 1 bug.
void Insert(TList * plist, int data){
    // TNode  auxNode = {data, (*plist)->next};     //Why?
    TList auxNode = (TList)malloc(sizeof(TNode));
    auxNode->data = data;
    auxNode->next = *plist;
    (*plist) = auxNode;
}

/* Displays the content of the list */
// This function has 1 bug
void Show(TList list){                 // We don't need a pointer
    while (list != NULL){
        printf("%d ", list->data);
        list = list->next;
    }
    printf("\n");
}

/* Deletes the head of the list */
// This function has several bugs
void Delete(TList *plist){              // We need a pointer!
    TList aux = *plist;
    *plist = (*plist)->next;
    free(aux);
    aux = NULL;
}

/* Destroys the list and sets it to NULL */
// This function has 1 missing operation
void Destroy(TList * plist){
    // TList ptr = (*plist);             // We don't need this
    TList aux;
    while ((*plist) != NULL){
        aux = (*plist);
        *plist = (*plist)->next;
        free(aux);
    }
    plist = NULL;
}
