#include "LinkedList.h"

/* Creates an empty List */
TList Create(){               // It should return a TList
    return NULL;
}

/* Insert in the list in an ordered way */
void Add(TList * plist, int data){
    TList ptr;
    TList aux;

    aux = (TNode *)malloc(sizeof(TNode));
    aux->data = data;
    aux->next = NULL;
    aux->prev = NULL;

    ptr = (*plist);
    if((*plist) == NULL){
        (*plist) = aux;
    } else {
        while((ptr->next != NULL) && (ptr->next->data <= data))         //ptr->next->data <= data
            ptr = ptr->next;
        if(ptr->next == NULL){
            ptr->next = aux;
            aux->prev = ptr;
        } else {
            ptr->next->prev = aux;
            aux->next = ptr->next;
            aux->prev = ptr;
            ptr->next = aux;
        }
    }
}

/* Displays the content of the list */
void Show(TList list){
    while (list != NULL){
        printf("%d ", list->data);
        list = list->next;
    }
    printf("\n");
}


/* Destroys the list and sets it to NULL */
void Destroy(TList * plist){
    // TList ptr = (*plist);      //We don't need this
    TList aux;
    while ((*plist) != NULL){
        aux = (*plist);
        (*plist) = (*plist)->next;
        free(aux);
    }
    (*plist) = NULL;
}
