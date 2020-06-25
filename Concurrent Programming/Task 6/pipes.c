#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>int main() {
    pid_t pid1, pid2, pid3;
    int pipe1[4];
    pipe(pipe1);
    pipe(pipe1 + 2);    pid1 = fork();    if (pid1 < 0) {
        perror("first fork failed");
        exit(1);
    } else if (pid1 == 0) {        dup2(pipe1[1], STDOUT_FILENO);        close(pipe1[0]);
        close(pipe1[1]);
        close(pipe1[2]);
        close(pipe1[3]);        execlp("ls", "ls", "-l", "/tmp", NULL);        exit(0);
    } else {
        pid2 = fork();
        if (pid2 < 0) {
            perror("second fork failed");
            exit(1);
        } else if (pid2 == 0) {
            dup2(pipe1[0], STDIN_FILENO);
            dup2(pipe1[3], STDOUT_FILENO);            close(pipe1[0]);
            close(pipe1[1]);
            close(pipe1[2]);
            close(pipe1[3]);
            printf("\n");
            execlp("tail", "tail", "-5", NULL);        } else {
            pid3 = fork();            if (pid3 < 0) {
                perror("third fork failed");
                exit(1);
            } else if (pid3 == 0) {                dup2(pipe1[2], STDIN_FILENO);                close(pipe1[0]);
                close(pipe1[1]);
                close(pipe1[2]);
                close(pipe1[3]);
                execlp("sort", "sort", "-n", "-k 5,5", (char *) 0);                exit(0);
            }
        }
    }    close(pipe1[0]);
    close(pipe1[1]);
    close(pipe1[2]);
    close(pipe1[3]);    for (int i = 0; i < 3; i++)
        wait(NULL);    exit(0);}