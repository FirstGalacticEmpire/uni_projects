from bst import binary_search_tree
from bbst import balanaced_binary_search_tree
from linkedlist import DynamicList
import plotly.graph_objects as go
import string
import random
import time
import pandas as pd
from IPython.display import display, HTML


def generate_dat(n_elements=10):
    a_dict = dict()
    a_list = sorted([random.randint(1000000, 9999999) for x in range(n_elements)])
    a_list = list(dict.fromkeys(a_list))
    for index in a_list:
        letters = string.ascii_uppercase
        a_dict[index] = [''.join(random.choice(letters) for i in range(0, 12)),
                         ''.join(random.choice(letters) for i in range(0, 12))]
    with open('data.txt', 'w+') as file:
        for key, value in a_dict.items():
            file.write(str(key) + ' ' + value[0] + ' ' + value[1] + '\n')


def load_data_to_BST(data_structure, file='data.txt', random_=False, number=5000):
    tree = data_structure()
    a_array = []
    with open(file, 'r+') as data:
        line = data.readline()
        while line:
            pocket = line.split()
            a_array.append(pocket)
            line = data.readline()
    a_array = a_array[:number]

    if not random_:
        for pocket in a_array:
            tree.insert(index=int(pocket[0]), data=pocket[1:])
        return tree
    if random_:
        random.shuffle(a_array)

        start = time.time()
        for pocket in a_array:
            tree.insert(index=int(pocket[0]), data=pocket[1:])
        end = time.time()

        random.seed(random.randint(1, 100))
        random.shuffle(a_array)

        start2 = time.time()
        for pocket in a_array:
            tree.find(index=int(pocket[0]))
        end2 = time.time()

        random.seed(random.randint(1, 100))
        random.shuffle(a_array)

        start3 = time.time()
        for pocket in a_array:
            tree.delete_index(index=int(pocket[0]))
        end3 = time.time()
        time_elapsed_add = end - start
        time_elapsed_add = time_elapsed_add / len(a_array)
        time_elapsed_find = end2 - start2
        time_elapsed_find = time_elapsed_find / len(a_array)
        time_elapsed_delete = end3 - start3
        time_elapsed_delete = time_elapsed_delete / len(a_array)
    return tree, time_elapsed_add, time_elapsed_find, time_elapsed_delete


def measure_one(data_structure, data_structure_name, len_):
    array_of_times = []
    array_of_times2 = []
    array_of_times3 = []
    for n in range(1000, len_, 100):  # step = 100
        time_elapsed1 = []
        time_elapsed2 = []
        time_elapsed3 = []
        for x in range(0, 15):
            data_pocket = load_data_to_BST(data_structure=data_structure, random_=True, number=n)
            time_elapsed_ = data_pocket[1]
            time_elapsed_2 = data_pocket[2]
            time_elapsed_3 = data_pocket[3]
            time_elapsed1.append(time_elapsed_)
            time_elapsed2.append(time_elapsed_2)
            time_elapsed3.append(time_elapsed_3)
        array_of_times.append([n, (sum(time_elapsed1) / len(time_elapsed1))])
        array_of_times2.append([n, (sum(time_elapsed2) / len(time_elapsed2))])
        array_of_times3.append([n, (sum(time_elapsed3) / len(time_elapsed3))])

    return {'add': [array_of_times, str(data_structure_name)], 'find': [array_of_times2, str(data_structure_name)],
            'delete': [array_of_times3, str(data_structure_name)]}


def print_line_chart(data):
    add = []
    find = []
    delete = []
    for x in data:
        for key, value in x.items():
            if key == 'add':
                add.append(value)
            if key == 'find':
                find.append(value)
            if key == 'delete':
                delete.append(value)

    def print_(data, name):
        fig = go.Figure()
        colors = ['blue', 'red', 'yellow', 'black', 'orange', 'green']
        for method in data:
            color = colors.pop()
            x = [method[0][x][0] for x in range(0, len(method[0]))]
            y = [float(method[0][x][1]) for x in range(0, len(method[0]))]
            fig.add_trace(go.Scatter(x=x, y=y,
                                     mode='lines+markers',
                                     name=method[1], line=dict(color=color, width=4)))
        fig.update_layout(
            title=name,
            xaxis_title='Number of elements [n]',
            yaxis_title='Time [s]')
        fig.show()


    print_(data=add, name='Time elapsed while adding n elements to the structure.')
    print_(data=find, name='Average time to find an element in the structure.')
    print_(data=delete, name='Average time to delete an element from the structure.')
    dictionary__a = {
        'n': [add[0][0][x][0] for x in range(0, len(add[0][0]))],
        add[0][1]: [add[0][0][x][1] for x in range(0, len(add[0][0]))],
        add[1][1]: [add[1][0][x][1] for x in range(0, len(add[1][0]))],
        add[2][1]: [add[2][0][x][1] for x in range(0, len(add[2][0]))]
    }
    df_a = pd.DataFrame(dictionary__a)
    dictionary__s = {
        'n': [add[0][0][x][0] for x in range(0, len(add[0][0]))],
        add[0][1]: [add[0][0][x][1] for x in range(0, len(add[0][0]))],
        add[1][1]: [add[1][0][x][1] for x in range(0, len(add[1][0]))],
        add[2][1]: [add[2][0][x][1] for x in range(0, len(add[2][0]))]
    }
    df_s = pd.DataFrame(dictionary__s)
    dictionary__d = {
        'n': [add[0][0][x][0] for x in range(0, len(add[0][0]))],
        add[0][1]: [add[0][0][x][1] for x in range(0, len(add[0][0]))],
        add[1][1]: [add[1][0][x][1] for x in range(0, len(add[1][0]))],
        add[2][1]: [add[2][0][x][1] for x in range(0, len(add[2][0]))]
    }
    df_d = pd.DataFrame(dictionary__d)

    with open('add.html', 'w+') as file:
        file.write(df_a.to_html())
    with open('search.html', 'w+') as file:
        file.write(df_s.to_html())
    with open('delete.html', 'w+') as file:
        file.write(df_d.to_html())

if __name__ == "__main__":
    data = list()
    data.append(measure_one(data_structure=balanaced_binary_search_tree, data_structure_name='AVLTree', len_=10000))
    data.append(measure_one(data_structure=binary_search_tree, data_structure_name='binary_search_tree', len_=10000))
    data.append(measure_one(data_structure=DynamicList, data_structure_name='DynamicList', len_=10000))
    print_line_chart(data)
