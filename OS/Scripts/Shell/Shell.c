#include "job_control.h"
#include "shell_functions.h"
#include <string.h>

#define MAX_LENGTH 256

int main(void)
{
	char command_input[MAX_LENGTH];
	bool is_background;
	char *command_arguments[MAX_LENGTH/2];

	pid_t pid_fork, pid_wait;
	int wait_status;
	enum status wait_status_result;
	int wait_info;
  char buffer[2000];
	char termination_type[16];
	while (TRUE)
	{
    print_prompt();
		get_command(command_input, MAX_LENGTH, command_arguments, &is_background);  /* get next command */

		if(is_empty(command_arguments)){
			continue;   // if empty command
		}else{
			pid_fork = fork();
			if(pid_fork == 0){
				execvp(command_arguments[0], command_arguments);
				fprintf(stderr, "Error: command not found: %s\n", command_arguments[0]);;
			}else{
				if(is_background){
					printf("Log: process %d running in is_background", pid_fork);
				}else{
					waitpid(-1, &wait_status, WUNTRACED | WCONTINUED);
					if (WIFEXITED(wait_status)){
          					wait_info = WEXITSTATUS(wait_status);
          					sprintf(buffer, "parent: child %d terminated with exit(%d)\n", pid_fork, wait_info);
					 }
					if (WIFSIGNALED(wait_status)){
          					wait_info = WTERMSIG(wait_status);
          					sprintf(buffer, "parent: child %d kill by signal %d\n", pid_fork, wait_info);
      					}
      					if (WIFSTOPPED(wait_status)){
          					wait_info = WSTOPSIG(wait_status);
          					sprintf(buffer, "parent: child %d stopped by signal %d\n", pid_fork, wait_info);
     					}
      					if (WIFCONTINUED(wait_status)){
          					sprintf(buffer, "parent: child %d continued\n", pid_fork);
      					}
      					write(2, buffer, strlen(buffer));

					printf("%s\n",status_strings[analyze_status(wait_status, &wait_info)] );
				}
			}
		}
		/* the steps are:
			 (1) fork a child process using fork()
			 (2) the child process will invoke execvp()
			 (3) if is_background == 0, the parent will wait, otherwise continue
			 (4) Shell shows a wait_status message for processed command
			 (5) loop returns to get_commnad() function
		*/

	} // end while
}
