bits    32
global  main

extern  printf

section .data
    format      db 'Hello world!', 0xA,0

section .bss
    unused      resd 1

section .text
    main:
        push    format
        call    printf
        add     esp, 4
        sub     eax, eax
        ret