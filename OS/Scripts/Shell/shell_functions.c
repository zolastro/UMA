#include "shell_functions.h"
#include "job_control.h"

void print_prompt(){
    char current_working_directory[1000];
    char user[1000];
		getlogin_r(user, sizeof(user));
		getcwd(current_working_directory, sizeof(current_working_directory));
    printf("%s%s@my-terminal:", KGRN, user);
    printf("%s%s%s $ ",KCYN, current_working_directory, KNRM);
    fflush(stdout);
}

bool is_empty(char* command[MAX_LENGTH/2]){
  return command[0] == NULL;
}
