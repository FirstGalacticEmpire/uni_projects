bits    32
global  main

extern  printf
extern scanf

section .data
    welcome_msg      db 'Provide a string',10,0
    scanf_format     db '%s', 0
    out_format       db '%s', 10, 0

section .bss
    source      resw 1024
    destination resw 1024

section .text
    whlie_loop:
        cmp dword [source+eax], 0x0
        je while_end
        mov ebx,  [source+eax]
        mov [destination+eax], ebx
        add eax, 1
        jmp whlie_loop
    while_end:
        mov dword [destination+eax], 0
        sub eax, eax
        jmp finish
    main:
        ;print welcome message
        push    welcome_msg
        call    printf
        add     esp, 4
        ;scanf word
        push    source
        push    scanf_format
        call    scanf 
        add     esp, 8
        ;copy word
        sub eax,eax
        jmp whlie_loop
    finish:
        ;print word
        push    destination
        push    out_format
        call    printf
        add     esp, 8
        sub     eax, eax
        ret