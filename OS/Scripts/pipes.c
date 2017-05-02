#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define MAX_ARGS 256

int main(int argc, char** argv){
  int status;
  pid_t pid;
  int code;
  int signum;


  char *executable_file;
  char *executable_args[MAX_ARGS];

  int pipefd[2];

  if (pipe(pipefd) == -1) {
   perror("pipe");
   exit(EXIT_FAILURE);
  }

  pid = fork();         //First child

  if (pid == -1){
    fprintf(stderr, "parent: error in fork\n");
    exit(EXIT_FAILURE);
  }

  if(pid != 0){
    pid = fork();       //Second child

    if(pid == 0){
      // Reader
      close(0);
      dup2(pipefd[0],0);
      close(pipefd[0]);
      close(pipefd[1]);

      executable_file = "/usr/bin/nl";
      executable_args[0] = "Adding lines";
      executable_args[1] = NULL;

      execvp(executable_file, executable_args);

      fprintf(stderr, "child 2: error in exec\n");
      exit(EXIT_FAILURE);
    }
  }



  if (pid == 0){
    //Writer
    close(1);
    dup2(pipefd[1],1);
    close(pipefd[1]);
    close(pipefd[0]);

    executable_file = "/usr/bin/cut";
    executable_args[0] = "Reading text";
    executable_args[1] = "-c";
    executable_args[2] = "1-4";
    executable_args[3] = NULL;

    execvp(executable_file, executable_args);

    fprintf(stderr, "child 1: error in exec\n");
    exit(EXIT_FAILURE);
  }else{
    fprintf(stderr, "parent: waiting for children %d\n", getpid());

    close(pipefd[0]);
    close(pipefd[1]);

    while ((pid = waitpid(-1, &status, WUNTRACED | WCONTINUED)) != -1){
      if (WIFEXITED(status)){
          code = WEXITSTATUS(status);
          fprintf(stderr, "parent: child %d terminated with exit(%d)\n", pid, code);
      }
      if (WIFSIGNALED(status)){
          signum = WTERMSIG(status);
          fprintf(stderr, "parent: child %d kill by signal %d\n", pid, signum);
      }
      if (WIFSTOPPED(status)){
          signum = WSTOPSIG(status);
          fprintf(stderr, "parent: child %d stopped by signal %d\n", pid, signum);
      }
      if (WIFCONTINUED(status)){
          fprintf(stderr, "parent: child %d continued\n", pid);
      }
    }
  }

    return (EXIT_SUCCESS);
  }
