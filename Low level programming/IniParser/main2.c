#include <stdio.h>
#include <stdlib.h>

int main() {
    int n;
    scanf("%d", &n);

    // alloc a NxN matrix
    int **t = malloc(n * sizeof(*t));
    for (int i = 0; i < n; i++)
        t[i] = malloc(n * sizeof(*t[i]));

    // do some matrix operations
    // ...
    // ...
    // ...

    // free the matrix
    for (int i = 0; i < n; i++)
        free(t[i]);
    free(t);
    return 0;
}