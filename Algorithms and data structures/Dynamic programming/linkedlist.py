# class List:
#     def __init__(self, file='data.txt'):
#         self.array_of_indexes = []
#         self.array_of_strings = []
#         with open(file, 'r+') as data:
#             line = data.readline()
#             while line:
#                 pocket = line.split()
#                 self.array_of_indexes.append(int(pocket[0]))
#                 self.array_of_strings.append(pocket[1:])
#                 line = data.readline()
#         # print(self.array_of_indexes)
#         # print(self.array_of_strings)
#
#     def add_element(self, element=[6990447, 'asd', 'asd']):
#         # print(self.array_of_indexes)
#         len_ = len(self.array_of_indexes)
#         for id_ in range(0, len_):
#             if self.array_of_indexes[id_] >= element[0]:
#                 self.array_of_indexes.insert(id_, element[0])
#                 self.array_of_strings.insert(id_, element[1:])
#                 break
#         # print(self.array_of_indexes)
#         # print(self.array_of_strings)
#
#     def search_for_element(self, element=6990447):
#         len_ = len(self.array_of_indexes)
#         for id_ in range(0, len_):
#             if self.array_of_indexes[id_] == element:
#                 # print(self.array_of_strings[id_])
#                 return id_
#         if True:
#             print("Element not found.")
#             return False
#
#     def delete_element(self, element=6990447):
#         id_ = self.search_for_element(element)
#         del self.array_of_indexes[id_]
#         del self.array_of_strings[id_]
#         # print(self.array_of_indexes)
#         # print(self.array_of_strings)
#
#
# def fill_tree(tree, num_elems=10):
#     for i in range(num_elems):
#         cur_elem = random.randint(0, 15)
#         tree.insert(cur_elem)
#     return tree


class Node:
    def __init__(self, index, data, next=None):
        self.index = index
        self.data = data
        self.next = next


class DynamicList:
    def __init__(self):
        self.head = None

    def insert(self, index, data):
        new_node = Node(index, data)
        if self.head is None:
            new_node.next = self.head
            self.head = new_node

        elif self.head.index >= new_node.index:
            new_node.next = self.head
            self.head = new_node

        else:
            current_node = self.head
            while current_node.next is not None and current_node.next.index < new_node.index:
                current_node = current_node.next

            new_node.next = current_node.next
            current_node.next = new_node

    def find(self, index):
        temp = self.head
        while temp is not None and temp.index != index:
            temp = temp.next
        if temp is not None:
            return temp
        else:
            print("Node not found.")
            return None

    def delete_index(self, index):
        found = False
        if self.head is None:
            print('Node couldnt be deleted, as the list is empty.', index)
            return
        if self.head.index == index:
            self.head = self.head.next
            return
        else:
            current_node = self.head
            while current_node.next is not None:
                if current_node.next.index == index:
                    found = True
                    current_node.next = current_node.next.next
                    break
                else:
                    current_node = current_node.next
        if not found:
            print('Node couldnt be deleted, as it isnt in the list.')

    def print_tree(self):
        temp = self.head
        while (temp):
            print(temp.index)
            temp = temp.next


if __name__ == "__main__":
    print()
    # import random
    # print()
    # tree = DynamicList()
    # a_array = []
    # with open('data.txt', 'r+') as data:
    #     line = data.readline()
    #     while line:
    #         pocket = line.split()
    #         a_array.append(pocket)
    #         tree.insert(index=int(pocket[0]), data=pocket[1:])
    #         line = data.readline()
    # random.shuffle(a_array)
    # #tree.print_tre
    # random.seed(20)
    # random.shuffle(a_array)
    # for pocket in a_array:
    #     print(pocket)
    #     tree.find(index=int(pocket[0]))

