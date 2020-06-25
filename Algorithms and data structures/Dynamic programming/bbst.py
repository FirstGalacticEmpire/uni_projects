class node_2:
    def __init__(self, index=None, data=None):
        self.index = index
        self.data = data
        self.left_child = None
        self.right_child = None
        self.parent = None
        self.height = 1


class balanaced_binary_search_tree:
    def __init__(self):
        self.root = None

    def insert(self, index, data):
        if self.root is None:
            self.root = node_2(index, data)
        else:
            self._insert(index, data, self.root)

    def _insert(self, index, data, current_node):
        if index < current_node.index:
            if current_node.left_child is None:
                current_node.left_child = node_2(index, data)
                current_node.left_child.parent = current_node
                self._check_if_balanced_after_insertion(current_node.left_child)
            else:
                self._insert(index, data, current_node.left_child)
        elif index > current_node.index:
            if current_node.right_child is None:
                current_node.right_child = node_2(index, data)
                current_node.right_child.parent = current_node
                self._check_if_balanced_after_insertion(current_node.right_child)
            else:
                self._insert(index, data, current_node.right_child)
        else:
            print("Index already in tree!")

    def print_tree(self):
        if self.root is not None:
            self._print_tree(self.root)

    def _print_tree(self, current_node):
        if current_node is not None:
            self._print_tree(current_node.left_child)
            print((str(current_node.index), ' h= ', current_node.height))
            self._print_tree(current_node.right_child)

    def height(self):
        if self.root is not None:
            return self._height(self.root, 0)
        else:
            return 0

    def _height(self, current_node, current_height):
        if current_node is None:
            return current_height
        left_height = self._height(current_node.left_child, current_height + 1)
        right_height = self._height(current_node.right_child, current_height + 1)
        return max(left_height, right_height)

    def find(self, index):
        if self.root is not None:
            return self._find(index, self.root)
        else:
            return None

    def _find(self, index, current_node):
        if index == current_node.index:
            return current_node
        elif index < current_node.index and current_node.left_child is not None:
            return self._find(index, current_node.left_child)
        elif index > current_node.index and current_node.right_child is not None:
            return self._find(index, current_node.right_child)

    def delete_index(self, index):
        return self.delete_node(self.find(index))

    def delete_node(self, node):
        if node is None or self.find(node.index) is None:
            print("Node wasnt deleted, cause node isnt in the tree")
            return None

        def min_index_node(n):
            current = n
            while current.left_child is not None:
                current = current.left_child
            return current

        def num_children(n):
            num_children = 0
            if n.left_child is not None:
                num_children += 1
            if n.right_child is not None:
                num_children += 1
            return num_children

        node_parent = node.parent
        node_children = num_children(node)

        if node_children == 0:

            if node_parent is not None:
                if node_parent.left_child == node:
                    node_parent.left_child = None
                else:
                    node_parent.right_child = None
            else:
                self.root = None

        if node_children == 1:
            if node.left_child is not None:
                child = node.left_child
            else:
                child = node.right_child

            if node_parent is not None:
                if node_parent.left_child == node:
                    node_parent.left_child = child
                else:
                    node_parent.right_child = child
            else:
                self.root = child
            child.parent = node_parent

        if node_children == 2:
            successor = min_index_node(node.right_child)
            node.index = successor.index
            self.delete_node(successor)
            return

        if node_parent is not None:
            node_parent.height = 1 + max(self.get_height(node_parent.left_child),
                                         self.get_height(node_parent.right_child))
            self._check_if_balanced_after_deletion(node_parent)

    # with help of https://www.geeksforgeeks.org/avl-tree-set-1-insertion/

    def taller_child(self, current_node):
        left = self.get_height(current_node.left_child)
        right = self.get_height(current_node.right_child)
        return current_node.left_child if left >= right else current_node.right_child

    def get_height(self, current_node):
        if current_node is None:
            return 0
        return current_node.height

    def _check_if_balanced_after_insertion(self, current_node, path=[]):
        if current_node.parent is None:
            return
        path = [current_node] + path

        left_height = self.get_height(current_node.parent.left_child)
        right_height = self.get_height(current_node.parent.right_child)

        if abs(left_height - right_height) > 1:
            path = [current_node.parent] + path
            self._rebalance_node(path[0], path[1], path[2])
            return

        new_height = 1 + current_node.height
        if new_height > current_node.parent.height:
            current_node.parent.height = new_height

        self._check_if_balanced_after_insertion(current_node.parent, path)

    def _check_if_balanced_after_deletion(self, current_node):
        if current_node is None:
            return

        left_height = self.get_height(current_node.left_child)
        right_height = self.get_height(current_node.right_child)

        if abs(left_height - right_height) > 1:
            y = self.taller_child(current_node)
            x = self.taller_child(y)
            self._rebalance_node(current_node, y, x)

        self._check_if_balanced_after_deletion(current_node.parent)

    def _rebalance_node(self, z, y, x):
        if y == z.left_child and x == y.left_child:
            self._right_rotate(z)
        elif y == z.left_child and x == y.right_child:
            self._left_rotate(y)
            self._right_rotate(z)
        elif y == z.right_child and x == y.right_child:
            self._left_rotate(z)
        elif y == z.right_child and x == y.left_child:
            self._right_rotate(y)
            self._left_rotate(z)

    def _right_rotate(self, z):
        sub_root = z.parent
        y = z.left_child
        t3 = y.right_child
        y.right_child = z
        z.parent = y
        z.left_child = t3
        if t3 is not None:
            t3.parent = z
        y.parent = sub_root
        if y.parent is None:
            self.root = y
        else:
            if y.parent.left_child == z:
                y.parent.left_child = y
            else:
                y.parent.right_child = y
        z.height = 1 + max(self.get_height(z.left_child),
                           self.get_height(z.right_child))
        y.height = 1 + max(self.get_height(y.left_child),
                           self.get_height(y.right_child))

    def _left_rotate(self, z):
        sub_root = z.parent
        y = z.right_child
        t2 = y.left_child
        y.left_child = z
        z.parent = y
        z.right_child = t2
        if t2 is not None:
            t2.parent = z
        y.parent = sub_root
        if y.parent is None:
            self.root = y
        else:
            if y.parent.left_child == z:
                y.parent.left_child = y
            else:
                y.parent.right_child = y
        z.height = 1 + max(self.get_height(z.left_child),
                           self.get_height(z.right_child))
        y.height = 1 + max(self.get_height(y.left_child),
                           self.get_height(y.right_child))


def load_data_to_BST(file='data.txt'):
    array_of_strings = []
    tree = balanaced_binary_search_tree()
    a_array = []
    with open(file, 'r+') as data:
        line = data.readline()
        while line:
            pocket = line.split()
            a_array.append(pocket)
            tree.insert(index=int(pocket[0]))
            # tree.delete_index(index=int(pocket[0]))
            array_of_strings.append(pocket[1:])
            line = data.readline()
    # random.shuffle(a_array)
    # # print(a_array)
    # for pocket in a_array:
    #     tree.insert(index=pocket[0])
    # # print(a_array)
    return tree


if __name__ == "__main__":
    tree = load_data_to_BST()
    print(tree.height())
