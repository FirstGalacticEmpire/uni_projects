#include <stdio.h>

int f(int p, int r) {
    printf("Hello, World!");
    if (p == r)
        return p;

    int q = p + (r - p) / 2;
    return f(p, q) + f(q + 1, r);
}

int main() {
    int p, r;
    scanf("%d %d", &p, &r);
    printf("%d\n", f(p, r));
    return 0;
}