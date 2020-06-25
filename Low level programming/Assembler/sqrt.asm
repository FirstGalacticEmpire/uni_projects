bits    32
global  main

extern  printf
extern scanf

section .data
    welcome_msg      db 'Provide a max value',10,0
    scanf_format     db '%lf', 0
    out_format       db 'sqrt(%lf) num: %lf', 10, 0
    step             dq 0.125
    cur_number       dq 0

section .bss
    max_number       resq 1

section .text

    finish:
        ret
    for_loop:
        fld qword[cur_number]
        fld qword[max_number]
        fcomp
        fstsw ax
        and eax, 0100011100000000B
        cmp eax, 0000000100000000B
        je finish
        cmp eax, 0100000000000000B
        je finish

        fld qword[cur_number]
        sub     esp,8
        fst qword[esp]
        fsqrt
        sub     esp, 8
        fstp qword[esp]
        push    out_format
        call    printf
        add     esp, 20
        sub     eax, eax
        fld qword[cur_number]
        fadd qword[step]
        fstp qword[cur_number]
        jmp for_loop

        
    main:
        ;print welcome message
        push    welcome_msg
        call    printf
        add     esp, 4
        ;scanf word
        push    max_number
        push    scanf_format
        call    scanf 
        add     esp, 8
        ;print word
        jmp for_loop
