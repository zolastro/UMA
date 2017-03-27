#include <stdio.h>
#include <stdlib.h>
// #include "LinkedList.h"                      //Why?
#define CHUNK 1024 /* read 1024 bytes at a time */

void main(){

    char buf[CHUNK];                          // We have to allocate the initial size
    FILE *file;
    size_t nread;

    file = fopen("test.txt", "rt");           //Text file, add the "t" flag
    if (file) {
      if (ferror(file)) {                     // The "if" sentence should be first
        printf("Error opening the file.\n");
        exit(-1);
      }
      printf("Succesfully opened the file!\n");
      int i = 0;
        while ((nread = fread(buf, 1, CHUNK, file)) > 0){
          fwrite(buf, 1, nread, stdout);
        }
        printf("\nFile succesfully printed\n");
        fclose(file);
    }
}
