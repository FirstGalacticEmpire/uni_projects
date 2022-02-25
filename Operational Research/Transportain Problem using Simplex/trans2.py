from pprint import pprint
import networkx as nx
import numpy as np
import matplotlib.pyplot as plt

M = 9999
costs = np.array([[7, 5, 5, 0],
                  [3, 10, 10, M],
                  [3, 10, 10, 0],
                  [M, M, 0, 0]])

sources = np.array([30, 20, 80, 80])
demands = np.array([40, 40, 20, 110])

# costs = np.array([[5, 4, M],
#                   [5, 4, 0],
#                   [3, 2, 0]])
# sources = np.array([20, 50, 30])
# demands = np.array([30, 40, 30])


def initial_solutions(sources, demands, costs):
    # todo to jest zjebane
    # pprint(my_table.table)
    assert len(sources) == len(demands)
    x = 0
    y = 0
    initial_solution = []
    for _ in range(0, len(sources)):
        for __ in range(0, len(demands)):
            # if costs[x, y] == 0 and demands[x] == 0:
            #     # print("Kupa", x + 1, y + 1, demands[x])
            #     initial_solution.append([y, x, demands[x]])
            #     demands[x] = 0
            #     x += 1
            #     x = x % len(sources)
            #     continue

            # Going to next column
            if sources[y] > demands[x]:
                # print(x + 1, y + 1, demands[x])
                initial_solution.append([x, y, demands[x]])
                sources[y] = sources[y] - demands[x]
                demands[x] = 0
                x += 1
                x = x % len(sources)
                continue

            # Going to next row
            if sources[y] <= demands[x]:
                # print(x + 1, y + 1, sources[y])
                initial_solution.append([x, y, sources[y]])
                demands[x] = demands[x] - sources[y]
                sources[y] = 0
                y += 1
                y = y % len(demands)
                break

    # Assertions
    # print(sources)
    # print(demands)
    # print(initial_solution)
    assert (np.array(demands) == 0).all()
    assert (np.array(sources) == 0).all()
    return initial_solution


class Cell:
    def __init__(self, is_base, x, y, cost, current_value) -> None:
        self.current_value = current_value
        self.cost = cost
        self.y = y
        self.x = x
        self.is_base = is_base
        self.is_a_bad_boy = False

    def set_current_value(self, new_value):
        # assert self.is_base is False
        self.current_value = new_value

    def __str__(self):
        try:
            return f"({'M' if self.cost > 900 else self.cost} / {'M' if self.current_value > 900 else self.current_value} {self.is_base})"
        except TypeError:
            return f"({self.cost} / {self.current_value} {self.is_base})"

    def __repr__(self):
        try:
            return f"({'M' if self.cost > 900 else self.cost} / {'M' if self.current_value > 900 else self.current_value} {self.is_base})"
        except TypeError:
            return f"({self.cost} / {self.current_value} {self.is_base})"


class Table:
    def __init__(self, sources, demands, costs) -> None:
        self.table = []
        for x in range(0, len(sources)):
            _ = []
            for y in range(0, len(demands)):
                _.append(Cell(False, x, y, costs[x, y], None))
            self.table.append(_)
        self.np_table = np.array(self.table)
        self.sources = sources
        self.demands = demands
        self.costs = costs
        # self.np_flows = np.zeros((len(demands), len(sources)))
        self.np_flows = np.full((len(demands), len(sources)), -1)
        assert len(sources) == len(demands)
        self.u = np.full((len(sources), 1), fill_value=np.nan)
        self.v = np.full((1, len(sources)), fill_value=np.nan)

    def initialize_table(self):
        initial_solution = initial_solutions(np.copy(self.sources), np.copy(self.demands), np.copy(self.costs))
        for cell in initial_solution:
            self.np_table[cell[1], cell[0]].set_current_value(cell[2])
            self.np_table[cell[1], cell[0]].is_base = True
            self.np_flows[cell[1], cell[0]] = cell[2]

            if self.costs[cell[1], cell[0]] == 0:
                self.np_table[cell[1], cell[0]].is_a_bad_boy = True
        # pprint(self.np_table)
        # print(len(self.sources) + len(self.demands) - 1)
        # assert len(self.bases_cells()) == len(self.sources) + len(self.demands) - 1

    def _find_row_with_most_bases(self):
        return np.argmax((self.np_flows > 0).sum(axis=1))

    def _find_base_in_row(self, row):
        return

    # Return coordinates of all cels in base
    # def coordinates_of_bases(self):
    #     return list(np.argwhere(self.np_flows > 0))

    # Return all cells i base
    # Do not edit the cells!!!!
    def bases_cells(self):
        # print(self.np_flows)
        # return np.copy(self.np_table[self.np_flows > -0.5]).flatten()
        base_cells = []
        for cell in self.np_table.flatten():
            if cell.is_base:
                base_cells.append(cell)
        return base_cells

    def non_base_cells(self):
        # print(self.np_flows)
        # return np.copy(self.np_table[np.logical_not(self.np_flows > -0.5)]).flatten()
        non_base_cellz = []
        for cell in self.np_table.flatten():
            if not cell.is_base:
                non_base_cellz.append(cell)
        return non_base_cellz

    def calculate_u_v(self):
        # I think this is need - the reinitalization!
        self.u = np.full((len(sources), 1), fill_value=np.nan)
        self.v = np.full((1, len(sources)), fill_value=np.nan)

        best_row = self._find_row_with_most_bases()
        # print(best_row)
        self.u[best_row] = 0
        for cell in self.table[best_row]:
            if cell.is_base:
                self.v[0, cell.y] = cell.cost - self.u[best_row]
        # pprint(self.table)
        # print(self.bases_cells())
        while np.isnan(self.u).any() or np.isnan(self.v).any():
            for base_cell in self.bases_cells():
                assert base_cell.is_base is True
                # print(self.v[0, base_cell.y], self.u[base_cell.x, 0])
                # print(base_cell.x, base_cell.y)
                if np.isnan(self.v[0, base_cell.y]):
                    if not np.isnan(self.u[base_cell.x, 0]):
                        self.v[0, base_cell.y] = base_cell.cost - self.u[base_cell.x, 0]

                if np.isnan(self.u[base_cell.x, 0]):
                    if not np.isnan(self.v[0, base_cell.y]):
                        self.u[base_cell.x, 0] = base_cell.cost - self.v[0, base_cell.y]
            # print(self.u)
            # print(self.v)
            # break

            # break

    def calculate_current_values(self):
        for non_base_cell in self.non_base_cells():
            assert non_base_cell.is_base is False
            non_base_cell.current_value = non_base_cell.cost - self.u[non_base_cell.x, 0] - self.v[0, non_base_cell.y]

    def calculate_z(self):
        Z = 0
        for base_cell in self.bases_cells():
            assert base_cell.is_base is True
            Z += base_cell.cost * base_cell.current_value
        return Z

    def check_if_optimal(self):
        most_negative_cell = self.table[0][0]
        for non_base_cell in self.non_base_cells():
            assert non_base_cell.is_base is False
            if non_base_cell.current_value < 0:
                if most_negative_cell.current_value > non_base_cell.current_value:
                    most_negative_cell = non_base_cell
        if most_negative_cell.current_value >= 0:
            return True
        else:
            return False, most_negative_cell

    def change_base(self, cell_to_enter_base):
        # pprint(self.table)
        graph = nx.Graph()
        cells = list(self.bases_cells()) + [cell_to_enter_base]
        # print(cells)
        for base_cell_id, base_cell in enumerate(cells):
            # if counter != 7:
            #     assert base_cell.is_base is True
            graph.add_node(base_cell_id)
            for cell_id, cell in enumerate(cells):
                if cell.x == base_cell.x or cell.y == base_cell.y:
                    if not base_cell_id == cell_id:
                        graph.add_edge(base_cell_id, cell_id)

        found_cycles = nx.algorithms.cycle_basis(graph)
        # print(found_cycles)
        # print(found_cycles)
        chain = [cycle for cycle in found_cycles if len(cells) - 1 in cycle and len(cycle) % 2 == 0]
        # print(chain)
        # nx.draw(graph, with_labels=True)
        # plt.show()
        # print(chain)
        assert len(chain) == 1
        # print(len(cells))
        # print(np.roll(np.array(chain[0]), 7))
        # print(type(np.array(chain[0])))
        # print(chain[0])
        chain = np.array(chain[0])
        # print(len(np.array([1, 2, 7, 0])) - np.argmax(np.array([1, 2, 7, 0])))

        # chain = np.roll(chain, len(cells)-1)
        chain = np.roll(chain, len(chain) - np.argmax(chain))
        # print(chain)

        current_values = []
        for cell in np.array(cells)[chain[1:]]:
            current_values.append(cell.current_value)
        # print(current_values[::2])
        min_value = min(current_values[::2])  # min_value = min(current_values)

        # print(min_value)
        donor = False
        first_iter = True
        chosen_cells = np.array(cells)[chain]
        # print(chosen_cells)
        flag = False
        for cell in chosen_cells:
            if first_iter:
                cell.current_value = min_value
                cell.is_base = True
                # print("ASD")
                first_iter = False
                continue
            assert cell.is_base is True
            if donor:
                cell.current_value = cell.current_value + min_value
                donor = False
            else:
                donor = True
                cell.current_value = cell.current_value - min_value
                # print(cell.current_value - min_value)
                # todo zeby tak bylo ze nie bierze tych z costem rownym zero
                if cell.current_value == 0 and not flag:
                    cell.is_base = False
                    # first donnor leaves the base
                    flag = True

            # if cell.current_value == -5:
            #     print("luasd", cell)
            #     # print()

        # pprint(self.table)
        assert len(self.bases_cells()) == len(self.sources) + len(self.demands) - 1
        assert len(cells) == len(self.sources) + len(self.demands)
        # print(chosen_cells)
        # pprint(self.table)

    def recrate_np_flows(self):
        self.np_flows = np.zeros((len(demands), len(sources)))
        # pprint(self.np_table)
        for cell in np.copy(self.np_table).flatten():
            if cell.is_base:
                self.np_flows[cell.x, cell.y] = cell.current_value
        # print(self.np_flows)

    def one_step(self, print_first=True, print_anything=True):

        print("Wchodząca:")
        pprint(self.table)
        try:
            optimal, cell = self.check_if_optimal()
        except TypeError:
            print("Solution is optimal")
            print("Z:", self.calculate_z())
            return True

        self.change_base(cell)
        print("Wychodząca")
        pprint(self.table)
        self.recrate_np_flows()
        self.calculate_u_v()
        self.calculate_current_values()
        print()
        return False

    def final(self):
        my_table.initialize_table()
        my_table.calculate_u_v()
        my_table.calculate_current_values()

        for _ in range(0, 15):
            if self.one_step():
                return True

        raise "error"

if __name__ == '__main__':
    # Table initialization
    my_table = Table(sources, demands, costs)
    my_table.final()
    # my_table.initialize_table()
    # my_table.calculate_u_v()
    # my_table.calculate_current_values()
    #
    # my_table.one_step()
    # my_table.one_step()
    # my_table.one_step()
    # my_table.one_step()
    # my_table.one_step()
    # print("----")
    # my_table.calculate_u_v()
    # print(my_table.u)
    # print(my_table.v)
    # my_table.calculate_current_values()
    # pprint(my_table.table)

    # my_table.one_step(False)
    # my_table.calculate_u_v()
    # my_table.calculate_current_values()
    # # pprint(my_table.table)
    # # print(my_table.calculate_z())
    # optimal, cell = my_table.check_if_optimal()
    # my_table.change_base(cell)
    # my_table.recrate_np_flows()
    # # x = -7
    # # print(-7-x)
