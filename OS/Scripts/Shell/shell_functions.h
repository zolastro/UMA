#ifndef _SHELL_FUNCTIONS_H
#define _SHELL_FUNCTIONS_H

typedef int bool;
#define TRUE 1
#define FALSE 0

#define MAX_LENGTH 256

//Colors
#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"


void print_prompt();
bool is_empty(char* command[MAX_LENGTH/2]);

#endif
