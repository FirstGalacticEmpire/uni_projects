bits 32
global main
extern printf
extern scanf
section .data
    scanf_format      db '%d', 0
    printf_format     db '%d ', 0
    array       times 100 dd 0
section .text
    main:
        mov     esi, -1
	mov     edi, 0
    input:
        inc     esi
        lea     ebx, [array + esi * 4]
        push    ebx
        push    scanf_format
        call    scanf
        add     esp, 8
        cmp     eax, 1
        je      input
        cmp     esi, 0
        jz      end
        mov     eax, -1
    main_loop:
        inc     eax
        cmp     eax, esi
        je      print_loop
        mov     ecx, 0
    inside_loop:
        inc     ecx
        cmp     ecx, esi
        je      main_loop
        mov     edx, [array + ecx * 4]
        mov     ebx, [array + ecx * 4 - 4]
        cmp     edx, ebx
        jge     inside_loop
        mov     [array + ecx * 4], ebx
        mov     [array + (ecx - 1) * 4], edx
        jmp     inside_loop
    print_loop:
        push    dword [array + edi * 4]
        push    printf_format
        call    printf
        add     esp, 8
        inc     edi
        cmp     edi, esi
        jl      print_loop
    end:
        mov     eax, 0

