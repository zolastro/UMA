#include "shell_project.h"

int main(void){
	char command_entered[MAX_LINE];
	char *args[MAX_LINE/2];
	char term_type[MAX_LINE];
	char previous_directory[MAX_LINE];
	char current_path[MAX_LINE];
	bool is_background;
	enum status status_res;
	int info;
	int waitpid_return;
	int status;
	pid_t pid_fork;

	strcpy(previous_directory, "not initializated");

	while (1) {
		print_prompt(current_path);
		get_command(command_entered, MAX_LINE, args, &is_background);
		if (is_empty(args, 0)) {
			continue;
		} else {
			pid_fork = fork();

			if (pid_fork == -1) {
				fprintf(stderr, "parent: error in fork\n");
				exit(1);
			}

			if (pid_fork == 0) {
				execvp(command_entered, args);
			  exit(-1);
			} else {
				if (is_background) {
					report_background(pid_fork, command_entered);
				} else {
					waitpid_return = waitpid(pid_fork, &status, WUNTRACED);

					if (waitpid_return == -1) {
						perror("waitpid");
						exit(EXIT_FAILURE);
					}

					if (WIFEXITED(status)){
						info = WEXITSTATUS(status);
						strcpy(term_type, "Exited");
        	} else if (WIFSIGNALED(status)){
						info = WTERMSIG(status);
						strcpy(term_type, "Killed");
					} else if (WIFSTOPPED(status)){
						info = WSTOPSIG(status);
						strcpy(term_type, "Stopped");
					}

					if (info == 255)
						if (is_cd(command_entered)){
							change_directory(args, previous_directory);
						} else {
							report_error(command_entered);
						}
					else
						report_foreground(pid_fork, command_entered, term_type, info);
				}

			}
			continue;
		}
	}
}

void print_prompt(char current_path[MAX_LINE]){
	getcwd(current_path, MAX_LINE);
	if_at_home_change_current_path(current_path);
	printf("%sguest@custom-shell%s:%s%s%s$", KGRN, KWHT, KBLU, current_path, KWHT);
	fflush(stdout);
}

void report_background(pid_t pid_fork, char command_entered[MAX_LINE]){
        printf("Info: %sBackground job running... pid: %d, command: %s\n%s", KMAG, pid_fork, command_entered, KWHT);
}

void report_error(char command_entered[MAX_LINE]){
	printf("Info: %sError, command not found: %s\n%s", KRED, command_entered, KWHT);
}

void report_foreground(pid_t pid_fork, char command_entered[MAX_LINE], char term_type[MAX_LINE], int info){
	printf("Info: %sForeground pid: %d, command: %s, %s, info: %d\n%s", KCYN, pid_fork, command_entered, term_type, info, KWHT);	
}

bool is_empty(char *args[MAX_LINE/2], int i){
	return args[i] == NULL;
}

bool is_cd(char command_entered[MAX_LINE]){
	return !(strcmp("cd", command_entered));
}

void if_at_home_change_current_path(char current_path[MAX_LINE]){
	char * home_starts_here;
	home_starts_here = strstr(current_path, "/home");
	if (home_starts_here != NULL){
		strcpy(current_path, (home_starts_here+4));
		current_path[0] = '~';
	}
}

void change_directory(char *args[MAX_LINE/2], char previous_directory[MAX_LINE]){
	int flag;
	char new_directory[MAX_LINE/2];
	char current_directory[MAX_LINE];
	char first_character;
	char second_character;
	char third_character;
	bool change_directory = TRUE;
	getcwd(current_directory, MAX_LINE);
	if (is_empty(args, 1))
		strcpy(new_directory, "/home");
	else {
		first_character = args[1][0];
		second_character = args[1][1];
		third_character = args[1][2];

		switch (first_character) {
			case '-':
				if (second_character == '\0'){
					if (!strcmp(previous_directory, "not initializated")) {
						printf("info: cd: previous_directory not set\n");
						change_directory = FALSE;
					} else {
						strcpy(new_directory, previous_directory);
						printf("%s\n", new_directory);
					}
				} else if (second_character == '-' && third_character == '\0'){
					strcpy(new_directory, "/home");
				} else {
					printf("info: cd: --: invalid option\n");
					change_directory = FALSE;
				}
				break;
			
			case '.':
				if (second_character == '/' || second_character == '\0') {
					char directory_for_single_dot[MAX_LINE/2];
					getcwd(directory_for_single_dot, MAX_LINE/2);
        	                        strcat(directory_for_single_dot, args[1]+1);
	                                strcpy(new_directory, directory_for_single_dot);
				} else if (second_character == '.') {
					bool stop_going_backwards = FALSE;
					char directory_for_double_dots[MAX_LINE/2];
					getcwd(previous_directory, MAX_LINE);
	                                do {
        	                                chdir("..");
						getcwd(directory_for_double_dots, MAX_LINE/2);
                	                        args[1]+=3;
                        	                stop_going_backwards = args[1][-1] == '\0' || args[1][0] != '.';
                                	} while (!stop_going_backwards);
					
					if (args[1][-1] != '\0') {
						strcat(directory_for_double_dots, "/");
						strcat(directory_for_double_dots, args[1]);
						printf("%s\n", directory_for_double_dots);
						strcpy(new_directory, directory_for_double_dots);
						chdir(new_directory);
					}
					change_directory = FALSE;
				}
				break;
			case '~':
				args[1]++;
        	                strcpy(new_directory, "/home");
	                        strcat(new_directory, args[1]);
				break;
			default:
				strcpy(new_directory, args[1]);
				break;	
		}
	}
	if (change_directory){
	getcwd(previous_directory, MAX_LINE);
	flag = chdir(new_directory);
	if (new_directory[0] == '\0')
		flag = 0;
	if (flag) {
		printf("Info cd: %s: No such file or directory\n", args[1]);
		chdir(current_directory);
	} else {
		printf("Info: %sUsed cd (change directory)\n", KCYN);
	}
	}
}
