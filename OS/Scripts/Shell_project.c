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

// Colors
#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"

// -----------------------------------------------------------------------
//                            MAIN
// -----------------------------------------------------------------------

int main(void)
{
	char input_buffer[MAX_LINE]; 			/* buffer to hold the command entered */
	int background;             			/* equals 1 if a command is followed by '&' */
	char *args[MAX_LINE/2];     			/* command line (of 256) has max of 128 arguments */
	// probably useful variables:
	int pid_fork, pid_wait; 			/* pid for created and waited process */
	int status;             			/* status returned by wait */
	enum status status_res; 			/* status processed by analyze_status() */
	int info;					/* info processed by analyze_status() */
	char term_type[16];				/* info about how did the process end*/

	while (1)   /* Program terminates normally inside get_command() after ^D is typed*/
	{
		printf("%suser@custom-shell%s:%s~%s$", KGRN, KWHT, KBLU, KWHT);
		fflush(stdout);
		get_command(input_buffer, MAX_LINE, args, &background);  /* get next command */

		if(args[0]==NULL) continue;   // if empty command
		else
		{
			pid_fork = fork();	// Create child
			if (pid_fork == -1)
			{
				fprintf(stderr, "parent: error in fork\n");
				exit(1);
			}

			if (pid_fork == 0)	// if it is the child
			{
				execvp(input_buffer, args);

//				fprintf(stderr, "child: error in exec\n");
			        exit(-1);
			}
			else
			{
				if (background)
				{
					printf("[!] %sBackground job running... pid: %d, command: %s\n%s", KMAG, pid_fork, input_buffer, KWHT);
				}
				else
				{
					waitpid(-1, &status, WUNTRACED);
					if (WIFEXITED(status))
					{
						info = WEXITSTATUS(status);
						strcpy(term_type, "Exited");
        				}
					if (WIFSIGNALED(status))
					{
						info = WTERMSIG(status);
						strcpy(term_type, "Killed");
					}
					if (WIFSTOPPED(status))
					{
						info = WSTOPSIG(status);
						strcpy(term_type, "Stopped");
					}
					if (info == 255)
						printf("[!] %sError, command not found: %s\n%s", KRED, input_buffer, KWHT);
					else
						printf("[!] %sForeground pid: %d, command: %s, %s, info: %d\n%s", KCYN, pid_fork, input_buffer, term_type, info, KWHT);
				}

			}
			continue;
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
