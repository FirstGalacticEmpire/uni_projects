from pulp import *
import numpy as np
import pandas as pd

if __name__ == '__main__':
    model = LpProblem(name="my_fucking_problem", sense=LpMinimize)
    var_names = [str(_ + 1).upper() for _ in range(0, 4)]
    x = LpVariable.dicts("y", var_names, 0)

    const_names = ["GE" + str(_ + 1) for _ in range(0, 3)]  # +["EQ1", "EQ2"]
    sense = [1 for _ in range(0, 3)]  # + [0, 0]  # GE, LE, EQ
    coefs = [[1, 0, 3, 1], [1, 2, 0, 1], [0, 1, 2, 1]]  # [6, -6, -1, -1],
    # [-5, 3, 1, -5]]  # Matrix coefs
    rhs = [4, 2, 3]

    for c, s, r, cn in zip(coefs, sense, rhs, const_names):
        expr = lpSum([x[var_names[i]] * c[i] for i in range(0, 4)])
        print(LpConstraint(e=expr, sense=s, name=cn, rhs=r))
        model += LpConstraint(e=expr, sense=s, name=cn, rhs=r)

    obj_coefs = [10, 12, 15, 20]
    model += lpSum([x[var_names[i]] * obj_coefs[i] for i in range(0, 4)])
    print(model)
    status = model.solve()
    print(f"status: {model.status}, {LpStatus[model.status]}")
    print(f"objective: {model.objective.value()}")
    for n in var_names:
        print(x[n].value())
