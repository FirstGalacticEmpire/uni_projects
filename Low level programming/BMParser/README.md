# BMPparser
Compile with: cc -g -o main main.c structures.h functions.c -lm

$ ./program PATH-TO-INPUT-BMP PATH-TO-ENCODED-BMP "text to be hidden"  //hides text in least significant bits

$ ./program PATH-TO-INPUT-BMP PATH-TO-ENCODED-BMP  //saves grayified bmp file in out file.

$ ./program PATH-TO-INPUT-BMP //reads from header
