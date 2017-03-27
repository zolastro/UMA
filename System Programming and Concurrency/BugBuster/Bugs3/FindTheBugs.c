#include <stdio.h>
#include <stdlib.h>
#include "LinkedList.h"

int main(int argc, char** argv) {
    // TNode * list = NULL;             //We have written all that code for some reason, use the functions that we created!
    TList list = Create();
    Add(&list, 1);
    Add(&list, 4);
    Add(&list, 16);
    Add(&list, 2);
    Add(&list, 8);
    Add(&list, 5);
    Add(&list, 9);
    Show(list);
    printf("\nDestroying list...\n" );
    Destroy(&list);
    Show(list);
    return (EXIT_SUCCESS);
}
