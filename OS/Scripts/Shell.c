/**
UNIX Shell Project

Sistemas Operativos
Grados I. Informatica, Computadores & Software
Dept. Arquitectura de Computadores - UMA

Some code adapted from "Fundamentos de Sistemas Operativos", Silberschatz et al.

To compile and run the program:
   $ gcc Shell_project.c job_control.c -o Shell
   $ ./Shell
	(then type ^D to exit program)

**/

#include "job_control.h"   // remember to compile with module job_control.c
#include <string.h>
#define MAX_LINE 256 /* 256 chars per line, per command, should be enough. */

// -----------------------------------------------------------------------
//                            MAIN
// -----------------------------------------------------------------------

int main(void)
{
	char inputBuffer[MAX_LINE]; /* buffer to hold the command entered */
	int background;             /* equals 1 if a command is followed by '&' */
	char *args[MAX_LINE/2];     /* command line (of 256) has max of 128 arguments */
	// probably useful variables:
	int pid_fork, pid_wait; /* pid for created and waited process */
	int status;             /* status returned by wait */
	enum status status_res; /* status processed by analyze_status() */
	int info;		/* info processed by analyze_status() */
  	char buffer[2000];
	char path[1000];
	char user[1000];
	char term_type[16];	/*How did the proccess end*/

	while (1)   /* Program terminates normally inside get_command() after ^D is typed*/
	{
		getlogin_r(user, sizeof(user));
		getcwd(path, sizeof(path));
		printf("%s@my-terminal:%s$",user, path);
		fflush(stdout);
		get_command(inputBuffer, MAX_LINE, args, &background);  /* get next command */
		
		if(args[0]==NULL){
			continue;   // if empty command
		}else{
			pid_fork = fork();
			if(pid_fork == 0){
				execvp(args[0], args);
				fprintf(stderr, "Error: command not found: %s\n", args[0]);;
			}else{
				if(background){
					printf("Log: process %d running in background", pid_fork);
				}else{
					waitpid(-1, &status, WUNTRACED | WCONTINUED);
					if (WIFEXITED(status)){
          					info = WEXITSTATUS(status);
          					sprintf(buffer, "parent: child %d terminated with exit(%d)\n", pid_fork, info);
					 }
					if (WIFSIGNALED(status)){
          					info = WTERMSIG(status);
          					sprintf(buffer, "parent: child %d kill by signal %d\n", pid_fork, info);
      					}
      					if (WIFSTOPPED(status)){
          					info = WSTOPSIG(status);
          					sprintf(buffer, "parent: child %d stopped by signal %d\n", pid_fork, info);
     					}
      					if (WIFCONTINUED(status)){
          					sprintf(buffer, "parent: child %d continued\n", pid_fork);
      					}
      					write(2, buffer, strlen(buffer));
				
					printf("%s\n",status_strings[analyze_status(status, &info)] );
				}
			}
		}
		/* the steps are:
			 (1) fork a child process using fork()
			 (2) the child process will invoke execvp()
			 (3) if background == 0, the parent will wait, otherwise continue
			 (4) Shell shows a status message for processed command
			 (5) loop returns to get_commnad() function
		*/

	} // end while
}

