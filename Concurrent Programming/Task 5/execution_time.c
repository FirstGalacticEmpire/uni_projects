#include <stdio.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <unistd.h>int main(int argc, char *argv[])
{
    if (argc < 2) {
        printf("Wrong number of arguments\n");
        return 0;
    }
    struct timeval start, end;    char* args[argc];
    for (int i = 1; i < argc; i++) {
        args[i - 1] = argv[i];
    }    char const* cmd = argv[1];
    args[argc - 1] = NULL;
    int status;
    gettimeofday(&start, NULL);
    if (fork() == 0) {
        status = execvp(cmd, args);
    }
    else {        wait(&status);
        gettimeofday(&end, NULL);        double time_elapsed = ((end.tv_sec - start.tv_sec) * 1000000u +
                               end.tv_usec - start.tv_usec) / 1.e6;        printf("\nTime elapsed: %f\n", time_elapsed);
    }
}