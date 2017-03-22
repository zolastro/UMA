#ifndef MEMORYMANAGER_H_
#define MEMORYMANAGER_H_

/*
 * MemoryManager.h
 *
 *  Created on: 04/03/2012
 *      Author: Llopis
 *      Translated: Sergio Galvez Rojas
 */

	typedef struct T_Node* T_handler;

	struct T_Node {
			unsigned start;
			unsigned end;
			T_handler next;
		};

// Creates the required structure to manage the available memory
	void Create(T_handler* handler);

// Frees the required structure
	void Destroy(T_handler* handler);

/* Returns in "ad" the memory address where the required block of memory with length "size" starts.
 * If this operation finishes successfully then "ok" holds a TRUE value; FALSE otherwise.
 */
	void Allocate(T_handler* handler, unsigned size, unsigned* ad, unsigned* ok);

/* Frees a block of memory with length "size" which starts at "ad" address.
 * If needed, can be assumed to be a previous allocated block
 */
	void Deallocate(T_handler* handler, unsigned size ,unsigned ad);

/* Shows the current status of the memory */
	void Show (T_handler handler);

	

#endif /* MEMORYMANAGER_H_ */
