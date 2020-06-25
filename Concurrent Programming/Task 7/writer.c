#define number_of_readers 3int main(int argc, char *argv[])
{
    sem_t *sem_reader, *sem_writer;    sem_reader = sem_open("/reader", 0, 0600, 0);
    sem_writer = sem_open("/writer", 0, 0600, 0);    while (1)
    {
        sem_wait(sem_writer);        for(int a = 0; a < number_of_readers; a++){
            sem_wait(sem_reader);
            printf("Writer %d out of library\n", a);
        }        printf("Writing\n");
        sleep(6);
        printf("Finished\n");        for(int a = 0; a < number_of_readers; a++){
            sem_post(sem_reader);
            printf("%d empty slots in que to library\n", a);
        }        sem_post(sem_writer);
        sleep(6);
    }
}