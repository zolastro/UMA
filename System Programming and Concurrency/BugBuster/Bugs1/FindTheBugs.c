#include <stdio.h>
#include <stdlib.h>
#include "LinkedList.h"

void main(){
    TList list = Create();
    printf("List created\n");
    Insert(&list, 14);
    Insert(&list, 16);
    Insert(&list, 38);
    Insert(&list, 215);
    printf("Inserted numbers\n");
    Show(list);
    Insert(&list, 3007);
    Show(list);
    Delete(&list);
    printf("Deleted First\n");
    Show(list);
    Delete(&list);
    Delete(&list);
    printf("Deleted\n");
    Show(list);
    Destroy(&list);
    printf("List destroyed\n");
    Show(list);
}
