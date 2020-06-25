import random
import time
import plotly.graph_objects as go
import sys

sys.setrecursionlimit(10 ** 6)


def generate_random_array(n, range_=True):
    array = list()
    for k in range(0, n):
        if not range_:
            array.append(random.randint(1, n))
        else:
            array.append(random.randint(1, n * 25))  # Range of values in the array.;
    return array


def constant_array(n, constant=1):
    return [constant for x in range(0, n)]


def increasing_order(n):
    array = [x for x in range(0, n)]
    return array


def descending_order(n):
    array = [x for x in range(0, n)]
    array.sort(reverse=True)
    return array


def a_shape(n):
    array = [x for x in range(0, n)]
    left_array = [i for i in array if i % 2 == 1]
    left_array.sort()
    right_array = [i for i in array if i % 2 == 0]
    right_array.sort(reverse=True)
    return (left_array + right_array)


def v_shape(n):
    array = [x for x in range(0, n)]
    left_array = [i for i in array if i % 2 == 1]
    left_array.sort(reverse=True)
    right_array = [i for i in array if i % 2 == 0]
    right_array.sort()
    return (left_array + right_array)


def is_correct(array1, array2):
    if sorted(array1) == array2:
        return True
    else:
        return False


def bubble_sort(array):
    start = time.time()

    length = len(array)
    for i in range(0, length):
        for k in range(0, length - i - 1):
            if array[k] > array[k + 1]:
                array[k], array[k + 1] = array[k + 1], array[k]

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def counting_sort(array):
    start = time.time()

    max_ = max(array) + 1
    count_array = [0] * max_
    for i in array:
        count_array[i] += 1
    i = 0
    for a in range(max_):
        for b in range(count_array[a]):
            array[i] = a
            i += 1

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def shell_sort(array):
    start = time.time()

    n = len(array)
    rift = n // 2
    while rift > 0:
        for i in range(rift, n):
            temp = array[i]
            j = i
            while j >= rift and array[j - rift] > temp:
                array[j] = array[j - rift]
                j -= rift
            array[j] = temp
        rift = rift // 2

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def _heapify(array, n, k):
    max_ = k
    a = 2 * k + 1
    b = 2 * k + 2

    if a < n and array[k] < array[a]:
        max_ = a

    if b < n and array[max_] < array[b]:
        max_ = b

    if max_ != k:
        array[k], array[max_] = array[max_], array[k]
        _heapify(array, n, max_)


def heap_sort(array):
    start = time.time()

    n = len(array)
    for k in range(n, -1, -1):
        _heapify(array, n, k)

    for k in range(n - 1, 0, -1):
        array[k], array[0] = array[0], array[k]

        _heapify(array, k, 0)

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def quick_sort_rec(array, low, right):
    if low >= right - 1:
        return

    h = low
    g = int(low + (right - low) / 2)
    array[g], array[right - 1] = array[right - 1], array[g]

    for i in range(low, right):
        if array[i] < array[right - 1]:
            array[i], array[h] = array[h], array[i]
            h += 1
    array[h], array[right - 1] = array[right - 1], array[h]

    quick_sort_rec(array, low, h)
    quick_sort_rec(array, h + 1, right)


def quick_sort(array):
    start = time.time()

    low = 0
    right = len(array)
    quick_sort_rec(array, low, right)

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def merge_sort(array):
    start = time.time()

    if len(array) > 1:

        mid = len(array) // 2
        right = array[mid:]
        left = array[:mid]

        merge_sort(left)
        merge_sort(right)

        a = b = c = 0

        while a < len(left) and b < len(right):
            if left[a] < right[b]:
                array[c] = left[a]
                a += 1
            else:
                array[c] = right[b]
                b += 1
            c += 1

        while a < len(left):
            array[c] = left[a]
            a += 1
            c += 1

        while b < len(right):
            array[c] = right[b]
            b += 1
            c += 1

    end = time.time()
    time_elapsed = end - start
    return time_elapsed


def test_method(measuring_points, sorting_method, type_of_data):
    time_table = []

    for measuring_point in measuring_points:
        time_elapsed_table = []
        for x in range(0, 30):  # 30 tries for each measuring point

            array = type_of_data(measuring_point)
            array_copy = array.copy()

            time_elapsed = sorting_method(array=array)
            time_elapsed_table.append(time_elapsed)

            if not array == sorted(array_copy):
                print('Error sorting method didnt work.')
                return False
        time_table.append([measuring_point, sum(time_elapsed_table) / len(time_elapsed_table)])

    return time_table


def print_line_chart(data, measuring_points):
    fig = go.Figure()

    colors = ['blue', 'red', 'yellow', 'black', 'orange', 'green']
    for method in data:
        color = colors.pop()
        y = [x[1] for x in method['time_table']]
        fig.add_trace(go.Scatter(x=measuring_points, y=y,
                                 mode='lines+markers',
                                 name=method['name'], line=dict(color=color, width=4)))

    fig.update_layout(
        title='Sorting time of specific sorting algorithms.',
        xaxis_title='Number of elements in the table [measuring points]',
        yaxis_title='Time [s]')
    fig.show()


def exercise1(measuring_points, sorting_methods):
    data = list()
    for sorting_method in sorting_methods:
        time_table = test_method(measuring_points, sorting_method[0], generate_random_array)
        print(str(sorting_method), ' : ', time_table)
        data.append({'name': str(sorting_method[1]), 'time_table': time_table})

    print_line_chart(data=data, measuring_points=measuring_points)


def print_line_chart2(data, measuring_points):
    fig = go.Figure()

    colors = ['blue', 'red', 'green']
    for method in data:
        color = colors.pop()
        y1 = [x for x in method['time_table'][0][0]]
        y1 = [x[1] for x in y1]
        print(y1)
        name = ''
        name = str(method['time_table'][0][1])
        method_type = str(method['name'])
        name = method_type + ': ' + name
        fig.add_trace(go.Scatter(x=measuring_points, y=y1,
                                 mode='lines',
                                 name=name, line=dict(color=color, width=4)))
        y2 = [x for x in method['time_table'][1][0]]
        y2 = [x[1] for x in y2]

        name2 = ''
        name2 = str(method['time_table'][1][1])
        name2 = method_type + ': ' + name2
        fig.add_trace(go.Scatter(x=measuring_points, y=y2,
                                 mode='lines',
                                 name=name2, line=dict(color=color, width=4, dash='dash')))

    fig.update_layout(title='Sorting time of specific sorting algorithms',
                      xaxis_title='Number of elements in the table [measuring points]',
                      yaxis_title='Time [s]')
    fig.show()


def exercise2(sorting_methods, types_of_data, measuring_points):
    data = list()
    for sorting_method in sorting_methods:
        time_tables = list()
        for type_ in types_of_data:
            a_list = [test_method(measuring_points, sorting_method[0], type_[0]), type_[1]]
            time_tables.append(a_list)
        data.append({'name': sorting_method[1], 'time_table': time_tables})

    print_line_chart2(data=data, measuring_points=measuring_points)


if __name__ == '__main__':
    measuring_points_ex1 = [x for x in range(20000, 80000, 2000)]

    sorting_methods = [[heap_sort, 'Heap Sort'], [counting_sort, 'Counting Sort'], [bubble_sort, 'Bubble Sort'],
                       [shell_sort, 'Shell Sort']]

    sorting_methods_without_bubble_sort = [[heap_sort, 'Heap Sort'], [counting_sort, 'Counting Sort'],
                                           [shell_sort, 'Shell Sort']]

    exercise1(measuring_points_ex1, sorting_methods)
    exercise1(measuring_points_ex1, sorting_methods_without_bubble_sort)

    sorting_methods2 = [[heap_sort, 'Heap Sort'],
                        [merge_sort, 'Merge Sort'], [quick_sort, 'Quick Sort']]

    measuring_points_ex2 = [x for x in range(5000, 15000, 200)]
    types_of_data = [[generate_random_array, 'Random Array'], [constant_array, 'Constant Array']]
    exercise2(sorting_methods2, types_of_data, measuring_points_ex2)

    measuring_points_ex2 = [x for x in range(2000, 6400, 100)]
    types_of_data = [[increasing_order, "Increasing order"], [descending_order, 'Descending order']]
    exercise2(sorting_methods2, types_of_data, measuring_points_ex2)

    measuring_points_ex2 = [x for x in range(5000, 15000, 200)]
    types_of_data = [[v_shape, 'V Shape'], [a_shape, 'A Shape']]
    exercise2(sorting_methods2, types_of_data, measuring_points_ex2)
