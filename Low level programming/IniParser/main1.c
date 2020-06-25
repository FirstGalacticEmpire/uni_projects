#include <stdio.h>

int main() {
    int n;
    scanf("%d", &n);

    int y;
    if (n % 2 == 0) {
        y = 2;
    }
    y++;
    for (int i = 0; i< 10; i++) {
        printf("%d\n", y + i);
    }
    return 0;
}