#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <signal.h>
#include <string.h>


#define MAX_ARGS 256

static pid_t   child [2];


void postman(int s)
{
  int status;
  pid_t pid;
  int code;
  int signum;

  char buffer[2000];

  if ((pid = waitpid(-1, &status, WUNTRACED | WCONTINUED)) != -1)
  {
      if (WIFEXITED(status))
      {
          code = WEXITSTATUS(status);
          sprintf(buffer, "parent: child %d terminated with exit(%d)\n", pid, code);
      }
      if (WIFSIGNALED(status))
      {
          signum = WTERMSIG(status);
          sprintf(buffer, "parent: child %d kill by signal %d\n", pid, signum);
      }
      if (WIFSTOPPED(status))
      {
          signum = WSTOPSIG(status);
          sprintf(buffer, "parent: child %d stopped by signal %d\n", pid, signum);
      }
      if (WIFCONTINUED(status))
      {
          sprintf(buffer, "parent: child %d continued\n", pid);
      }
      write(2, buffer, strlen(buffer));
  }
}

void signalHandler(int signo){
    if (signo == SIGTERM){
     printf("\nTerminating the child processes: \n");
     kill(child[0], SIGKILL);
     kill(child[1], SIGKILL);
    }
}

/*
*
*/
int main(int argc, char** argv)
{
  int status;
  pid_t pid;
  int code;
  int signum;
  int pipefd[2];
  char *executable_file;
  char *executable_args[MAX_ARGS];



  signal(SIGCHLD, postman);

  /***************************************************************************/
  /* <PIPES NEED TO BE INHERITED FROM PARENT>
   * CREATE an unamed pipe for communication between processes
   */
  pipe(pipefd);
  /***************************************************************************/

  // Create first process
  pid = fork();
  child[0] = pid;
  /* Errors in fork mean that there is no child process. Parent is alone. */
  if (pid == -1)
  {
      fprintf(stderr, "parent: error in fork\n");
      exit(1);
  }

  if (pid == 0)
  {
      /***************************************************************************/
      /* This is the first child process. Evey change in this section only affects the
       * child environment.
       */

      // <POST-FORK CHILD ONLY CODE HERE>

      // Redirect stdout to pipefd[1]
      // Close inherited standard output file descriptor
      close(1);
      // Copy writing pipe descriptor on stdout
      dup2(pipefd[1], 1);
      // Close all pipe descriptors to avoid having unknown duplicates
      close(pipefd[1]);
      close(pipefd[0]);

      executable_file = "/bin/cat";
      executable_args[0] = "mypipecat"; // A custom name for the process
      executable_args[1] = NULL; // Last argument

      fprintf(stderr, "child 1: %d %s\n", getpid(), executable_file);
      execvp(executable_file, executable_args);

      fprintf(stderr, "child: error in exec\n");
      exit(EXIT_FAILURE);
  }
  else
  {
    // Create second process
    pid = fork();
    child[1] = pid;
    /* Errors in fork mean that there is no child process. Parent is alone. */
    if (pid == -1)
    {
        fprintf(stderr, "parent: error in second fork\n");
        exit(EXIT_FAILURE);
    }

    if (pid == 0)
    {
        /***************************************************************************/
        /* This is the second child process. Evey change in this section only affects the
         * child environment.
         */

        // <POST-FORK CHILD ONLY CODE HERE>

        // Redirect stdin from pipefd[0]
        // Close inherited standard output file descriptor
        close(0);
        // Copy reading pipe descriptor on stdin
        dup2(pipefd[0], 0);
        // Close all pipe descriptors to avoid having unknown duplicates
        close(pipefd[1]);
        close(pipefd[0]);

        executable_file = "/usr/bin/nl";
        executable_args[0] = "mypipenl"; // A custom name for the process
        executable_args[1] = NULL; // Last argument

        fprintf(stderr, "child 2: %d %s\n", getpid(), executable_file);
        execvp(executable_file, executable_args);

        fprintf(stderr, "child 2: error in exec\n");
        exit(EXIT_FAILURE);
    }
    else{
      // <POST-FORK PARENT ONLY CODE HERE>
      fprintf(stderr, "parent: waiting for children\n", getpid());

      // <PARENT MUST CLOSE DESCRIPTORS FROM PIPES IN USE BY CHILDREN>
      close(pipefd[1]);
      close(pipefd[0]);

      if (signal(SIGTERM, signalHandler) == SIG_ERR){
        printf("\ncan't catch SIGINT\n");
      }


      while (1)
      {
          int reminder = 5;
          while ((reminder = sleep(reminder)) > 0);
          printf("time flies like arrows\n");
      }
    }
  }

  /* Only parent process should reach here */
  return (EXIT_SUCCESS);
}
