#include <stdio.h>
#include <stdlib.h>
#include "MemoryManager.h"

/*
 * MemoryManager.c
 *
 *  Created on: 10 mar. 2017
 *      Author: alumno
 */

const int MAX = 99;



void fixList(T_handler *phandler);
// Creates the required structure to manage the available memory
void Create(T_handler* pHandler) {
	T_handler ptr = (T_handler) malloc(sizeof(struct T_Node));
	ptr->start = 0;
	ptr->end = MAX;
	ptr->next = NULL;
	(*pHandler) = ptr;
}

// Frees the required structure
void Destroy(T_handler* pHandler) {
	T_handler prev = NULL;
	while (*pHandler != NULL) {
		prev = *pHandler;
		*pHandler = (*pHandler)->next;
		free(prev);
	}
	*pHandler = NULL;
}

/* Returns in "ad" the memory address where the required block of memory with length "size" starts.
 * If this operation finishes successfully then "ok" holds a TRUE value; FALSE otherwise.
 */
void Allocate(T_handler* pHandler, unsigned size, unsigned* ad, unsigned* ok) {
		T_handler ptr = *pHandler;
		*ok = 0;
		printf("Trying to allocate %d blocks of memory\n", size);
		while(ptr != NULL && !*ok){
			if((ptr->end - ptr->start) >= (size-1)){
				// printf("There is %d space here.\n", (*pHandler)->end - (*pHandler)->start+1);
				*ad = ptr->start;
				ptr->start += size;
				*ok = 1;
			}
			ptr = ptr->next;
		}
		if(!ok){
			fprintf(stderr, "Not enough memory.\n");
			return;
		}
		fixList(&(*pHandler));
}

/* Frees a block of memory with length "size" which starts at "ad" address.
 * If needed, can be assumed to be a previous allocated block
 */
void Deallocate(T_handler* pHandler, unsigned size, unsigned ad) {
	printf("Trying to deallocate %d blocks from position %d\n",size, ad);

	if(ad < 0 || size > 100){
			fprintf(stderr, "There is not such a block.\n");
			return;
	}else{
		// Create new block
		T_handler newBlock = (T_handler) malloc(sizeof(struct T_Node));
		newBlock->start = ad;
		newBlock->end = ad+size-1;
		// printf("[%d,%d]\n",newBlock->start,newBlock->end );

		// Find where to put the block

		T_handler prev = NULL;
		T_handler ptr = *pHandler;

		// printf("[%d,%d]\n",ptr->start,ptr->end );
		while((ptr != NULL) && (ptr->start < newBlock->start)){
			prev = ptr;
			ptr = ptr->next;
		}
		newBlock->next = NULL;
		if(prev != NULL){					// If it has to be at the beginning
			prev->next = newBlock;
		}
		if(ptr != NULL){			// If it has to be at the end
			newBlock->next = ptr;
		}
		if(newBlock->start <= (*pHandler)->start){
			*pHandler = newBlock;
		}
		// Show(*pHandler);
		fixList(&(*pHandler));
	}
}

/* Shows the current status of the memory */
void Show(T_handler handler) {
	while (handler != NULL) {
		printf("[%d, %d]", handler->start, handler->end);
		handler = handler->next;
	}
	printf("\n");
}

void fixList(T_handler *phandler){
	int changes = 1;
	while(changes){
		changes = 0;
		T_handler ptr = *phandler;
		T_handler prev = NULL;
		while(ptr != NULL){
			if(ptr->start > ptr-> end){
				if(prev != NULL){
					prev->next = ptr->next;
				}
				free(ptr);
				if(prev != NULL){
					ptr = prev->next;
				}else{
					ptr = NULL;
				}
			}

			if(prev != NULL && ptr!= NULL){
				if(ptr->start <= prev->end){
					// printf("Overlap\n" );
					prev->next = ptr->next;
					prev->end = ptr->end;
					free(ptr);
					ptr = prev->next;
					changes = 1;
				}
			}

			if(ptr != NULL){
				prev = ptr;
				ptr = ptr->next;
			}
		}
	}
}
