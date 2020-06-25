
#include <stdio.h>

#include <sys/stat.h>

#include <fcntl.h>

#include <stdlib.h>

#include <sys/types.h>

#include <unistd.h>

​

​

int main(int argc, char *argv[]) {

​

    char buf[20];

    int n;

    int fileToRead = open(argv[1], O_RDONLY, 0666);

​

    if (fileToRead == -1) { //check if file opened correctly.

        printf("errno:%d\n", errno);

        perror("Openingafile");

        exit(1);

    }

​

    int fileToWrite = open(argv[2], O_WRONLY, 0666); // | O_CREAT | O_APPEND

​

​

    if (fileToWrite == -1) {  //check if file opened correctly.

        printf("errno:%d\n", errno);

        perror("Openingafile");

        exit(1);

    }

​

    ftruncate(fileToWrite, 0); //cleaning the file

​

​

    while ((n = read(fileToRead, buf, 20)) > 0) { //while data in stream, perform copying.

        write(fileToWrite, buf, n);

    }

​

    printf("File copied correctly.");

​

    //close file streams..

    close(fileToRead);

    close(fileToWrite);

    return 0;

}

