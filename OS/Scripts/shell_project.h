#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"

#include "job_control.h"
#include <string.h>

typedef int bool;
#define TRUE 1
#define FALSE 0
#define MAX_LINE 256

void print_prompt(char current_path[MAX_LINE]);
void report_background(pid_t pid_fork, char command_entered[MAX_LINE]);
void report_error(char command_entered[MAX_LINE]);
void report_foreground(pid_t pid_fork, char command_entered[MAX_LINE], char term_type[MAX_LINE], int info);
bool is_empty(char *args[MAX_LINE/2], int index);
void if_at_home_change_current_path(char current_path[MAX_LINE]);
bool is_cd(char command_entered[MAX_LINE]);
void change_directory(char *args[MAX_LINE/2], char previous_directory[MAX_LINE]);
