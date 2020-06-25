from random import randint
import itertools
import numpy as np
import time
import matplotlib.pyplot as plt


def generate_knapsack_problem(number_of_elements, min_weight, max_weight, min_value, max_value):
    return [(randint(min_weight, max_weight), randint(min_value, max_value)) for x in range(0, number_of_elements)]


def brute_force(number_of_elements, max_weight_of_knapsack, list_of_elements):
    highest_value = None
    best_combination_of_elements = None

    for number in range(0, number_of_elements):
        # generating all possible combinations:
        for combinations in itertools.combinations(list_of_elements, number + 1):
            sum_of_weights = sum([combination[0] for combination in combinations])
            sum_of_values = sum([combination[1] for combination in combinations])
            if sum_of_weights <= max_weight_of_knapsack and (highest_value is None or sum_of_values >= highest_value):
                highest_value = sum_of_values
                best_combination_of_elements = combinations
    return highest_value, best_combination_of_elements


def greedy_algorithm(number_of_elements, max_weight_of_knapsack, list_of_elements):
    ratios = sorted([(index, element[1] / element[0]) for index, element in enumerate(list_of_elements)],
                    key=lambda a: a[1], reverse=True)
    best_combination = []
    highest_value = 0
    sum_of_weights = 0
    for index, ratio in ratios:
        if list_of_elements[index][0] + sum_of_weights <= max_weight_of_knapsack:
            sum_of_weights += list_of_elements[index][0]
            highest_value += list_of_elements[index][1]
            best_combination.append(list_of_elements[index])

    return highest_value, best_combination


def dynamic_algorithm(number_of_elements, max_weight_of_knapsack, list_of_elements):
    dynamic_matrix = np.zeros((number_of_elements + 1, max_weight_of_knapsack + 1))

    for i in range(1, number_of_elements + 1):
        for g in range(1, max_weight_of_knapsack + 1):
            if g < list_of_elements[i - 1][0]:
                dynamic_matrix[i][g] = dynamic_matrix[i - 1][g]
            else:
                dynamic_matrix[i][g] = max(dynamic_matrix[i - 1][g],
                                           dynamic_matrix[i - 1][g - list_of_elements[i - 1][0]] + list_of_elements[
                                               i - 1][1])
    list_of_chosen_elements = []
    current_row = number_of_elements
    current_column = max_weight_of_knapsack

    while current_row > 0 and current_column > 0 and dynamic_matrix[current_row][current_column] > 0:
        while dynamic_matrix[current_row - 1][current_column] == dynamic_matrix[current_row][current_column]:
            current_row -= 1
        list_of_chosen_elements.append(list_of_elements[current_row - 1])
        current_column -= list_of_elements[current_row - 1][0]
        current_row -= 1
    return int(dynamic_matrix[number_of_elements][max_weight_of_knapsack]), list_of_chosen_elements


def measure_time_in_point(point, function, max_weight_of_knapsack):
    table_of_times = []
    for a in range(0, 10):
        knapsack_problem = generate_knapsack_problem(point, 1, 25, 10, 100)
        start = time.time()
        function(point, max_weight_of_knapsack, knapsack_problem)
        end = time.time()
        time_elapsed = end - start
        table_of_times.append(time_elapsed)
    return sum(table_of_times) / len(table_of_times)


def test_one_function(function, max_weight_of_knapsack, max_number_of_points):
    table_of_times = []
    for point in range(1, max_number_of_points):
        table_of_times.append(measure_time_in_point(point, function, max_weight_of_knapsack))
    return table_of_times


def test_all_functions(max_weight_of_knapsack, max_number_of_points):
    a_dict = dict()
    a_dict["Brute Force"] = test_one_function(function=brute_force, max_weight_of_knapsack=max_weight_of_knapsack,
                                              max_number_of_points=max_number_of_points)
    a_dict["Dynamic Algorithm"] = test_one_function(function=dynamic_algorithm,
                                                    max_weight_of_knapsack=max_weight_of_knapsack,
                                                    max_number_of_points=max_number_of_points)
    a_dict["Greedy Ratio Algorithm"] = test_one_function(function=greedy_algorithm,
                                                         max_weight_of_knapsack=max_weight_of_knapsack,
                                                         max_number_of_points=max_number_of_points)
    a_dict["max_number_of_points"] = max_number_of_points
    return a_dict


def plot_function(a_dict):
    X_axis = [a for a in range(1, a_dict["max_number_of_points"])]
    del a_dict["max_number_of_points"]
    for key, value in a_dict.items():
        plt.plot(X_axis, value, label=str(key))
    plt.legend()
    plt.xlabel('Number of Elements')
    plt.ylabel('Time [s]')
    plt.title("Tittle")
    # plt.yscale("log")
    plt.show()


def compare_functions_in_point(point, max_weight_of_knapsack, min_weight, max_weight, min_value, max_valuez):
    table_of_tries = []
    for a in range(0, 30):
        knapsack_problem = generate_knapsack_problem(point, min_weight, max_weight, min_value, max_valuez)
        max_value = dynamic_algorithm(point, max_weight_of_knapsack, knapsack_problem)[0]
        proposed_value = greedy_algorithm(point, max_weight_of_knapsack, knapsack_problem)[0]
        if max_value > proposed_value:
            difference = ((max_value - proposed_value) / max_value) * 100
        elif max_value == proposed_value:
            difference = 0
        else:
            difference = ((proposed_value - max_value) / max_value) * 100
        table_of_tries.append(difference)
    return sum(table_of_tries) / len(table_of_tries)


def compare_functions_in_points(max_point, max_weight_of_knapsack, min_weight, max_weight, min_value, max_value):
    table = []
    for point in range(1, max_point):
        table.append(
            compare_functions_in_point(point, max_weight_of_knapsack, min_weight, max_weight, min_value, max_value))
    return table


def plot_difference(table, max_point, max_weight_of_knapsack, min_weight, max_weight, min_value, max_value):
    X_axis = [a for a in range(1, max_point)]
    plt.plot(X_axis, table, label="Difference")
    plt.legend()
    plt.xlabel('Number of Elements')
    plt.ylabel('Difference from optimal solution[%]')
    plt.title(
        f"Quality of Greedy ratio algorithm solution. \n Max_knapsack_weight: {str(max_weight_of_knapsack)} Weights_range: {str(min_weight)}-{str(max_weight)} Values_range: {str(min_value)}-{str(max_value)}")
    # plt.yscale("log")
    plt.show()


if __name__ == "__main__":
    a_dict = test_all_functions(50, 16)
    plot_function(a_dict)

    max_weight_of_knapsack = 60
    min_weight = 1
    max_weight = 20
    min_value = 1
    max_value = 20
    table = compare_functions_in_points(150, max_weight_of_knapsack, min_weight, max_weight, min_value, max_value)
    plot_difference(table, 150, max_weight_of_knapsack, min_weight, max_weight, min_value, max_value)
