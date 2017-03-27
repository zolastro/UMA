/*
 * File:   LinkedList.h
 * Author: galvez
 *
 * Created on 21 de marzo de 2017, 13:54
 */

#ifndef LINKEDLIST_H
#define	LINKEDLIST_H

#include <stdio.h>
#include <stdlib.h>

// These definitions have 1 bug.
typedef struct Node * TList;
typedef struct Node {
    int data;
    struct Node* next;
} TNode;

/* Creates an empty List */
TList Create();

/* Insert in the head of the list */
void Insert(TList * plist, int data);

/* Displays the content of the list */
void Show(TList list);

/* Deletes the head of the list */
void Delete(TList* plist);

/* Destroys the list and sets it to NULL */
void Destroy();


#endif	/* LINKEDLIST_H */
