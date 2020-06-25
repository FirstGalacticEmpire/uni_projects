#include <stdio.h>
#include <sys/mman.h>
#include <semaphore.h>
#include <pthread.h>#define N 20
sem_t sem_consumer, sem_producer;int buffer[N];void *producer(void *arg) {
    int a = 0;
    while (1) {
        sem_wait(&sem_producer);
        buffer[a % N] = a;
        sem_post(&sem_consumer);
        a++;
    }
}void *consumer(void *arg) {
    int a = 0;
    while (1) {
        sem_wait(&sem_consumer);
        printf("Reading: %d\n", buffer[a % N]);
        sem_post(&sem_producer);
        a++;
    }
}int main() {
    sem_init(&sem_consumer, 1, 0);
    sem_init(&sem_producer, 1, N);    pthread_t thread_producer, thread_consumer;    pthread_create(&thread_consumer, NULL, consumer, NULL);
    pthread_create(&thread_producer, NULL, producer, NULL);    pthread_join(thread_producer, NULL);
    pthread_join(thread_consumer, NULL);
    return 0;
}