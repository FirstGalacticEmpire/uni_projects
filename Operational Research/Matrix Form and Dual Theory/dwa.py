from pulp import LpProblem, LpMaximize

from pulp import *
import numpy as np
import pandas as pd

if __name__ == '__main__':
    model = LpProblem(name="some-problem", sense=LpMaximize)
    var_names = [str(_ + 1).upper() for _ in range(0, 6)]
    x = LpVariable.dicts("x", var_names, 0,) #cat='Integer')

    const_names = ["LE" + str(_ + 1) for _ in range(0, 4)] + ["EQ1", "EQ2"]
    sense = [-1 for _ in range(0, 4)] + [0, 0]  # GE, LE, EQ
    coefs = [[4, 0, 5, 2, 0, 2], [4, 3, 0, 4, 4, 2], [0, 3, 2, 0, 5, 1], [0, 0, 5, 4, 2, 1],
             [2, 0, 0, 0, 0, -1], [0, 1, -1, 0, 0, 0]]
    # Matrix coefs
    rhs = [120*60, 60*60, 80*60, 120*60, 0, 0]  # 0, 0]

    for c, s, r, cn in zip(coefs, sense, rhs, const_names):
        expr = lpSum([x[var_names[i]] * c[i] for i in range(0, 6)])
        # print(LpConstraint(e=expr, sense=s, name=cn, rhs=r))
        model += LpConstraint(e=expr, sense=s, name=cn, rhs=r)

    obj_coefs = [2.5, 1, 4, 3, 3.5, 4]
    model += lpSum([x[var_names[i]] * obj_coefs[i] for i in range(0, 6)])
    # print(model)
    status = model.solve()
    # print(f"status: {model.status}, {LpStatus[model.status]}")
    # print(f"objective: {model.objective.value()}")
    for n in var_names:
        print(x[n].value())
