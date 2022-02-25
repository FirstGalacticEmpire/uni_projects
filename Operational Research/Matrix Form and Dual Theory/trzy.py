import numpy as np
import itertools as it
import pandas as pd

pd.set_option('display.chop_threshold', 0.00001)
pd.set_option("display.max_rows", None, "display.max_columns", None)

# pd.set_option('display.float_format', lambda x: '%.3f' % x)
from numpy.linalg import LinAlgError

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

# c = [4, 2]
# b = [[1],
#      [1],
#      [3]]
# A = [[0.5, -1],
#      [-2, 1],
#      [1, 1]]
# variables = [0, 1, 2, 3, 4]
# row_zero = np.array([4, 2, 0, 0, 0])

if __name__ == '__main__':
    # print(np.identity(4))
    tabelka = pd.DataFrame(
        columns=["Z", "x1", "x2", "x3", "slack1", "slack2", "slack3", "slack4", "P_F", "P_O", "P_STATE",
                 "D_STATE", "D_F", "D_O",
                 "y1", "y2", "y3", "y4", "sur1", "sur2", "sur3"])
    # print(M)

    new_row = {}
    all_basic_solutions = list(it.combinations(variables, 4))  # 3
    Zs = []
    for basic_solution in all_basic_solutions:
        # print(basic_solution)
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
            new_row["Z"] = Z
            connected = np.concatenate((row1_deciscison, row1_slacks), 1)

            Zs.append(Z)

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
                new_row["P_O"] = True
                new_row["D_O"] = False
                print("Is optimal")
            else:
                # print(min(list(row0_deciscion_coefs) + list(row0_slacks_coefs)))
                new_row["P_O"] = False
                new_row["D_O"] = True
                print("Is not optimal")

            if np.min(np.matmul(invB, b)) >= -0.0000000001:
                new_row["P_F"] = True
                new_row["D_F"] = False
                print("Is feasible")
            else:
                # print(min(list(row0_deciscion_coefs) + list(row0_slacks_coefs)))
                new_row["P_F"] = False
                new_row["D_F"] = True
                print("Is not feasible")

            if new_row["P_F"] and new_row["P_O"]:
                new_row["D_F"] = True
                new_row["D_O"] = True
                new_row["D_STATE"] = "Optimal"
                new_row["P_STATE"] = "Optimal"

            if new_row["P_O"] and not new_row["P_F"]:
                new_row["P_STATE"] = "Super"
                new_row["D_STATE"] = "Sub"

            if not new_row["P_O"] and new_row["P_F"]:
                new_row["P_STATE"] = "Sub"
                new_row["D_STATE"] = "Super"

            if not new_row["P_O"] and not new_row["P_F"]:
                new_row["P_STATE"] = "Sub"
                new_row["D_STATE"] = "Super"

            primal = {"x" + str(x + 1): y for x, y in zip(basic_solution, list(x[0] for x in np.matmul(invB, b)))}
            for slack_ in [x + 1 for x in variables if x not in basic_solution]:
                primal["x" + str(slack_)] = 0
            # print(basic_solution)
            primal = dict(sorted(primal.items()))
            print(primal)
            for key, value in primal.items():
                if int(key[1]) >= 4:
                    new_row["slack" + str(int(key[1]) - 3)] = value
                else:
                    new_row[key] = value

            dual = {key for key, value in primal.items() if value != 0}

            dual = {"y_" + str(6 - int(key[1])): 0 for key in dual}
            dual = dict(sorted(dual.items()))
            print(dual)

            counter_ = 1
            for x in np.matmul(Cb, invB):
                new_row["y" + str(counter_)] = x
                counter_ += 1
            counter_ = 1
            for x in Cb @ invB @ A - c:
                new_row["sur" + str(counter_)] = x
                counter_ += 1
            print(np.matmul(Cb, invB))
            print(Cb @ invB @ A - c)
            # new_row["D_STATE"] = "XDD"
            # new_row["P_STATE"] = "XDD2"
            print(new_row)
            print()
            # break
            tabelka = tabelka.append(new_row, ignore_index=True)
        except LinAlgError:
            continue
        # break
    print(Zs)
    print(tabelka.round(3))
    # print(B)
    # print(x_b @ B)
