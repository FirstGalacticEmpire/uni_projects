inputs	= $(wildcard *.asm)
objects	= $(patsubst %.asm,%.o,$(inputs))
outputs	= $(patsubst %.asm,%,$(inputs))

all: $(outputs)

clean:
	$(RM) $(objects) $(outputs)

%.o: %.asm
	nasm -f elf32 $^

%: %.o
	cc -m32 -o $@ $^
