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

typedef struct Node * TList;
typedef struct Node {
    int data;
    struct Node *next;
    struct Node *prev;
} TNode;

/* Creates an empty List */
TList Create();

/* Insert in the list in an ordered way */
void Add(TList * plist, int data);

/* Displays the content of the list */
void Show(TList list);

/* Destroys the list and sets it to NULL */
void Destroy(TList * plist);


#endif	/* LINKEDLIST_H */
