class node_1:
    def __init__(self, index=None, data=None):
        self.index = index
        self.data = data
        self.left_child = None
        self.right_child = None
        self.parent = None


class binary_search_tree:
    def __init__(self):
        self.root = None

    def insert(self, index, data):
        if self.root is None:
            self.root = node_1(index, data)
        else:
            self._insert(index, data, self.root)

    def _insert(self, index, data, current_node):
        if index < current_node.index:
            if current_node.left_child is None:
                current_node.left_child = node_1(index, data)
                current_node.left_child.parent = current_node
            else:
                self._insert(index, data, current_node.left_child)
        elif index > current_node.index:
            if current_node.right_child is None:
                current_node.right_child = node_1(index, data)
                current_node.right_child.parent = current_node
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
            print(str(current_node.index), str(current_node.data))
            self._print_tree(current_node.right_child)

    def height(self):
        if self.root is not None:
            return self._height(self.root, 0)
        else:
            return 0

    def _height(self, current_node, cur_height):
        if current_node is None:
            return cur_height
        left_height = self._height(current_node.left_child, cur_height + 1)
        right_height = self._height(current_node.right_child, cur_height + 1)
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

        def num_children(n):
            num_children = 0
            if n.left_child is not None:
                num_children += 1
            if n.right_child is not None:
                num_children += 1
            return num_children

        def min_index_node(n):
            current = n
            while current.left_child is not None:
                current = current.left_child
            return current

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

        elif node_children == 1:

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

        elif node_children == 2:

            successor = min_index_node(node.right_child)
            node.index = successor.index
            self.delete_node(successor)


def load_data_to_BST(file='data.txt'):
    array_of_strings = []
    tree = binary_search_tree()
    with open(file, 'r+') as data:
        line = data.readline()
        while line:
            pocket = line.split()
            tree.insert(index=int(pocket[0]), data=pocket[1:])
            # tree.delete_index(index=int(pocket[0]))
            array_of_strings.append(pocket[1:])
            line = data.readline()

    return tree


if __name__ == "__main__":
    tree = load_data_to_BST()
    tree.print_tree()
    print(tree.height())
