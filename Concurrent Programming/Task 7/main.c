#define number_of_readers 3int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Wrong number of arguments\n");
        return 0;
    }    if (strtol(argv[1], NULL, 0) == 1) {
        printf("Start");
        sem_t *sem_reader, *sem_writer;
        sem_reader = sem_open("/reader", O_CREAT, 0600, number_of_readers);
        sem_writer = sem_open("/writer", O_CREAT, 0600, 1);    }
    else if (strtol(argv[1], NULL, 0) == 0)
    {
        printf("Unlink");
        sem_unlink("/reader");
        sem_unlink("/writer");    }    return 0;
}