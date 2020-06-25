from random import randrange as rand_int
import sys
import matplotlib.pyplot as plt
import numpy as np
import time
import random
import os
import resource
import itertools

sys.setrecursionlimit(10 ** 9)
resource.setrlimit(resource.RLIMIT_STACK, (resource.RLIM_INFINITY, resource.RLIM_INFINITY))


def number_of_edges(n, saturation):
    num_of_edges = int(n * (n - 1) * saturation / 2)
    return num_of_edges


def generate_connected_graph(n, saturation):
    graph_matrix = np.zeros((n, n), dtype=bool)

    num_of_edges = number_of_edges(n, saturation)

    connected_edges = [rand_int(0, n)]
    not_connected_edges = [a for a in range(n) if a not in connected_edges]
    random.shuffle(not_connected_edges)

    for a in range(n - 1):
        not_connected = not_connected_edges.pop()
        connected = random.choice(connected_edges)
        graph_matrix[not_connected][connected] = True
        graph_matrix[connected][not_connected] = True
        connected_edges.append(not_connected)

    for a in range(num_of_edges - n + 1):
        x = rand_int(0, n)
        y = rand_int(0, n)
        while x == y or graph_matrix[x][y]:
            x = rand_int(0, n)
            y = rand_int(0, n)
        graph_matrix[x][y] = True
        graph_matrix[y][x] = True
    return graph_matrix


def is_connected_(node_counter, graph, n, visited_vertices):
    visited_vertices[node_counter] = True
    if sum(visited_vertices) == len(visited_vertices):
        return True
    for a in range(0, n):
        if graph[node_counter][a] and not visited_vertices[a]:
            if is_connected_(a, graph, n, visited_vertices):
                return True
    return False


def is_connected(graph, n):
    visited = [False for x in range(0, n)]
    return is_connected_(0, graph, n, visited)


def generate_eulerian_graph(n, saturation):
    graph_matrix = generate_connected_graph(n, saturation)

    even_vertices = []
    odd_vertices = []

    for i in range(0, n):
        num_od_edges = np.dot(np.ones(n), graph_matrix[i])
        if num_od_edges % 2 == 0:
            even_vertices.append(i)
        else:
            odd_vertices.append(i)

    desired_number_of_edges = number_of_edges(n, saturation)

    if len(odd_vertices):
        while True:
            for vertex_one, vertex_two in itertools.combinations(odd_vertices, 2):
                if graph_matrix[vertex_one][vertex_two]:
                    graph_matrix[vertex_one][vertex_two] = False
                    graph_matrix[vertex_two][vertex_one] = False
                    if is_connected(graph_matrix, n):
                        desired_number_of_edges -= 1
                        odd_vertices.remove(vertex_one)
                        odd_vertices.remove(vertex_two)
                        break
                    graph_matrix[vertex_one][vertex_two] = True
                    graph_matrix[vertex_two][vertex_one] = True
                    if len(odd_vertices) == 2:
                        edge = 0
                        while edge == vertex_one or edge == vertex_two or graph_matrix[edge][vertex_one] or \
                                graph_matrix[edge][vertex_two]:
                            edge += 1
                        graph_matrix[edge][vertex_one] = True
                        graph_matrix[edge][vertex_two] = True
                        graph_matrix[vertex_one][edge] = True
                        graph_matrix[vertex_two][edge] = True

                        odd_vertices.remove(vertex_one)
                        odd_vertices.remove(vertex_two)
                        break

            for vertex_one, vertex_two in itertools.combinations(odd_vertices, 2):
                if not graph_matrix[vertex_one][vertex_two]:
                    desired_number_of_edges += 1
                    graph_matrix[vertex_one][vertex_two] = True
                    graph_matrix[vertex_two][vertex_one] = True
                    odd_vertices.remove(vertex_one)
                    odd_vertices.remove(vertex_two)
                    break

    return graph_matrix


def generate_connected_graphs(saturations):
    for saturation in saturations:
        print(saturation)
        for i in range(10, 301, 10):
            for h in range(0, 11):
                graph_matrix = generate_connected_graph(i, saturation)
                os.makedirs("data/connected_graphs/" + str(saturation) + "/" + str(i), exist_ok=True)
                np.savetxt("data/connected_graphs/" + str(saturation) + "/" + str(i) + "/" + str(h) + ".txt", graph_matrix,
                           fmt="%5i")


def generate_eulerian_graphs(saturations):
    for saturation in saturations:
        print(saturation)
        for i in range(10, 301, 10):
            for h in range(0, 11):
                grap_matrix = generate_eulerian_graph(i, saturation)
                os.makedirs("data/connected_eulerian_graphs/" + str(saturation) + "/" + str(i), exist_ok=True)
                np.savetxt("data/connected_eulerian_graphs/" + str(saturation) + "/" + str(i) + "/" + str(h) + ".txt", grap_matrix,
                           fmt="%5i")


def return_list_of_incidents(array, n):
    list_of_incidents = []
    for a in range(0, n):
        incidents = []
        for b in range(0, n):
            if a != b and array[a][b] == 1:
                incidents.append(b)
        list_of_incidents.append(incidents)
    return list_of_incidents


def backtracking_algorithm(graph, number_of_vertices, number_of_edges, path, depth):
    if depth == number_of_edges:
        final_edges = path[0] in graph[path[depth - 1]]
        if final_edges:
            path.append(path[0])
        return final_edges

    for vertice in graph[path[depth - 1]]:
        graph[path[depth - 1]].remove(vertice)
        graph[vertice].remove(path[depth - 1])
        path.append(vertice)

        if backtracking_algorithm(graph, number_of_vertices, number_of_edges, path, depth + 1):
            return path

        del path[-1]

        graph[path[depth - 1]].append(vertice)
        graph[vertice].append(path[depth - 1])
    return False


def find_eulerian_cycle(graph, number_of_vertices):
    num_of_edges = 0
    for vlist in graph:
        length = len(vlist)
        if length % 2 == 1:
            return False
        num_of_edges += length
    num_of_edges = int(num_of_edges / 2)
    return backtracking_algorithm(graph, number_of_vertices, num_of_edges, [0], 1)


def measure_one_saturation_eulerian(saturation):
    time_table = []
    for n in range(10, 301, 10):
        time_table_one_run = []
        for copy in range(0, 11):
            graph_matrix = np.loadtxt(f"data/connected_eulerian_graphs/{str(saturation)}/{str(n)}/{str(copy)}.txt", dtype=np.int8)
            list_of_incidents = return_list_of_incidents(array=graph_matrix, n=n)
            start = time.time()
            find_eulerian_cycle(graph=list_of_incidents, number_of_vertices=n)
            end = time.time()
            time_elapsed = end - start

            time_table_one_run.append(time_elapsed)
        print(time_table_one_run)
        time_table.append(sum(time_table_one_run) / len(time_table_one_run))
    print("Saturation:", str(saturation))
    print(time_table)
    print("*")
    return time_table


def measure_saturations_eulerian(saturations):
    Y_axis_plots = []
    for saturation in saturations:
        time_table = measure_one_saturation_eulerian(saturation=saturation)
        Y_axis_plots.append(time_table)
    return Y_axis_plots


def plot_eulerian(Y_axis_plots, saturations):
    counter = 0
    X_axis = [a for a in range(10, 301, 10)]
    for Y_axis_plot in Y_axis_plots:
        plt.plot(X_axis, Y_axis_plot, label=str(saturations[counter]))
        counter += 1
    plt.legend()
    plt.xlabel('Number of Elements')
    plt.ylabel('Time [s]')
    plt.title("Time it takes to find an Eulerian cycle in the graph")
    plt.show()


def backtrack_hamilton(graph, number_of_veritces, cycle, depth, start):
    if time.time() - start >= 31:
        return False
    if depth == number_of_veritces:
        return cycle[0] in graph[cycle[depth - 1]]
    for vertex in graph[cycle[depth - 1]]:
        if vertex not in cycle:
            cycle.append(vertex)
            if backtrack_hamilton(graph, number_of_veritces, cycle, depth + 1, start):
                return cycle
            del cycle[-1]
    return False


def measure_one_saturation_hamilton(saturation):
    time_table = []
    for n in range(100, 401, 10):
        time_table_one_run = []
        for copy in range(0, 11):
            graph_matrix = np.loadtxt(f"data/connected_graphs/{str(saturation)}/{str(n)}/{str(copy)}.txt", dtype=np.int8)
            list_of_incidents = return_list_of_incidents(array=graph_matrix, n=n)
            start = time.time()
            backtrack_hamilton(list_of_incidents, n, [0], 1, start)
            end = time.time()
            time_elapsed = end - start
            time_table_one_run.append(time_elapsed)
        print(time_table_one_run)
        time_table.append(sum(time_table_one_run) / len(time_table_one_run))
    print("Saturation:", str(saturation))
    print(time_table)
    print("*")
    return time_table


def measure_saturations_hamilton(saturations):
    Y_axis_plots = []
    for saturation in saturations:
        time_table = measure_one_saturation_hamilton(saturation=saturation)
        Y_axis_plots.append(time_table)
    return Y_axis_plots


def plot_hamilton(Y_axis_plots, saturations):
    counter = 0
    X_axis = [a for a in range(10, 301, 10)]
    for Y_axis_plot in Y_axis_plots:
        plt.plot(X_axis, Y_axis_plot, label=str(saturations[counter]))
        counter += 1
    plt.legend()
    plt.xlabel('Number of Elements')
    plt.ylabel('Time [s]')
    plt.title("Time it takes to find an Eulerian cycle in the graph")
    plt.show()


if __name__ == "__main__":
    saturations = [0.2, 0.3, 0.4, 0.6, 0.8, 0.95]  # [0.6, 0.8, 0.95]
    generate_connected_graphs(saturations)
    generate_eulerian_graphs(saturations)

    Y_axis_plots = measure_saturations_eulerian(saturations=saturations)
    plot_eulerian(Y_axis_plots=Y_axis_plots, saturations=saturations)

    y_axis_plots = measure_saturations_hamilton(saturations=saturations)
    plot_hamilton(y_axis_plots, saturations)
