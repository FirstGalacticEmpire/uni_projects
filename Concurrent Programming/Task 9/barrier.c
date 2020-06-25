#include <stdio.h>
#include <sys/mman.h>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>pthread_mutex_t mutex;
pthread_cond_t thread_condition;
const int n = 5;
int waiting_threads = 0;
time_t start, end;void *barrier(int n) {
    pthread_mutex_lock(&mutex);    waiting_threads++;
    if (waiting_threads == n) {
        pthread_cond_broadcast(&thread_condition);
    } else {
        while (waiting_threads != n) {
            pthread_cond_wait(&thread_condition, &mutex);
        }
    }    pthread_mutex_unlock(&mutex);
}void *worker(void *params_) {
    int *param = (int *) params_;
    int value = param[0];
    printf("Thread %d is working\n", value);
    time(&start);
    sleep(value);
    barrier(n);
    time(&end);
    long time_taken = end - start;
    printf("Thread %d can continue its work. Time it had to wait (same for every thread, because of an barrier): %ld\n",
           value, time_taken);}int main() {
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&thread_condition, NULL);
    pthread_t table_of_threads[n];    for (int a = 0; a < n; a++) {
        int *params = (int *) malloc(1 * sizeof(int));
        params[0] = a;
        pthread_create(&table_of_threads[a], NULL, worker, params);
    }
    for (int b = 0; b < n; b++) {
        pthread_join(table_of_threads[b], NULL);
    }    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&thread_condition);
    return 0;
}