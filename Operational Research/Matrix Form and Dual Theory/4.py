import numpy as np
import itertools as it
import pandas as pd
from numpy.linalg import LinAlgError

pd.set_option('display.chop_threshold', 0.00001)
pd.set_option("display.max_rows", None, "display.max_columns", None)

# pd.set_option('display.float_format', lambda x: '%.3f' % x)


c = np.array([4, 2, 3])
b = np.array([[10],
              [12],
              [15],
              [20]])
A = np.array([[1, 1, 0],
              [0, 2, 1],
              [3, 0, 2],
              [1, 1, 1]])
# 4 equalities, 7 variables = 3 degrees of freedom

variables = [0, 1, 2, 3, 4, 5, 6]
row_zero = np.array([4, 2, 3, 0, 0, 0, 0])


def get_table(basic_solution):
    M = np.concatenate((A, np.identity(len(basic_solution))), 1)

    try:
        one_solution = basic_solution  # all_basic_solutions[0]
        B = M[:, one_solution]
        Cb = row_zero[np.array(one_solution)]
        # print(one_solution)
        # print()
        # print(B)
        invB = np.linalg.inv(B)
        x_b = np.matmul(invB, b)
        row0_slacks_coefs = np.matmul(Cb, invB)
        row0_deciscion_coefs = np.matmul(np.matmul(Cb, invB), A) - c
        row1_deciscison = np.matmul(invB, A)
        row1_slacks = invB
        Z = np.matmul(Cb, x_b)[0]
        connected = np.concatenate((row1_deciscison, row1_slacks), 1)

        columns = ["Final", "Z"] + ["x_" + str(x + 1) for x in variables]
        # first_column = ["Z"] + ["x_" + str(x+1) for x in basic_solution]
        # rest_of_columns = [list(range(0, 5)) for x in range(0, 7)]
        df = pd.DataFrame(columns=columns)
        df.loc[0] = ["Z", 1] + list(row0_deciscion_coefs) + list(row0_slacks_coefs)
        # df= df.append(["Z", 1] + list(row0_deciscion_coefs) + list(row0_slacks_coefs))
        counter = 1
        for var_ in basic_solution:
            df.loc[counter] = ["x_" + str(var_ + 1), 0] + list(connected[counter - 1])
            counter += 1
        df["RS"] = [Z] + list(x[0] for x in np.matmul(invB, b))
        print(df.round(8))

        if min(list(row0_deciscion_coefs) + list(row0_slacks_coefs)) >= -0.0000000001:
            print("Is optimal")
        else:
            # print(min(list(row0_deciscion_coefs) + list(row0_slacks_coefs)))
            print("Is not optimal")

        if np.min(np.matmul(invB, b)) >= -0.0000000001:
            print("Is feasible")
        else:
            # print(min(list(row0_deciscion_coefs) + list(row0_slacks_coefs)))
            print("Is not feasible")

    except LinAlgError:
        return None


if __name__ == '__main__':
    all_basic_solutions = list(it.combinations(variables, 4))  # 3
    get_table(all_basic_solutions[0])
    Zs = []

    # print(basic_solution)

    # break

    # print(B)
    # print(x_b @ B)
