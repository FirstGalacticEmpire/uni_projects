int main(int argc, char *argv[])
{
    sem_t *sem_reader;    sem_reader = sem_open("/reader", 0, 0600, 0);    while (1)
    {
        sem_wait(sem_reader);        printf("Reading");
        sleep(6);
        printf("Finished reading\n");        sem_post(sem_reader);        sleep(3);    }}