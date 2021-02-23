from collections import Counter
from queue import PriorityQueue


class Node:
    def __init__(self, letter, left_side, right_side, value, code=None):
        self.letter = letter
        self.left_side = left_side
        self.right_side = right_side
        self.value = value
        self.code = code

    def __lt__(self, other):
        if self.value < other.value:
            return True
        else:
            return False


class HoffmannEncoder:
    def __init__(self):
        self.encoding_dictionary = dict()
        self.decoding_dictionary = dict()

    def create_code(self, text):
        counts = dict(Counter(text))
        alphabet = sorted(set(text))
        priority_queue = PriorityQueue()
        for letter in alphabet:
            priority_queue.put(Node(letter, None, None, counts[letter]))

        while priority_queue.qsize() > 1:
            node_one = priority_queue.get()
            node_two = priority_queue.get()
            root = Node(False, node_one, node_two, value=node_one.value + node_two.value)
            priority_queue.put(root)
        root = priority_queue.get()
        root.code = ''
        self._traverse_root(root)
        self._create_decoding_dictionary()

    def _create_decoding_dictionary(self):
        self.decoding_dictionary = dict([(value, key) for key, value in self.encoding_dictionary.items()])

    def _traverse_root(self, root):
        # preorder traversal
        if root:
            if root.letter:
                self.encoding_dictionary[root.letter] = root.code
            if root.left_side:
                root.left_side.code = root.code + "0"
                self._traverse_root(root.left_side)
            if root.right_side:
                root.right_side.code = root.code + "1"
                self._traverse_root(root.right_side)

    def encode(self, text):
        output = ''
        for letter in text:
            output += self.encoding_dictionary[letter]
        return output

    def decode(self, encoded_text):
        output = ''
        current_code = ''
        for bit in encoded_text:
            current_code += bit
            if current_code in self.decoding_dictionary:
                output += self.decoding_dictionary[current_code]
                current_code = ''
        return output

    def print_encoding_dictionary(self):
        print(dict(sorted(self.encoding_dictionary.items(), key=lambda t: t[0])))

    def print_decoding_dictionary(self):
        print(dict(sorted(self.decoding_dictionary.items(), key=lambda t: t[1])))


if __name__ == "__main__":
    # Loading text to be encoded:
    with open('data/norm_wiki_sample.txt', 'r') as file:
        encoded_text = file.read()

    # Instating encoder:
    Hoffmann_Encoder = HoffmannEncoder()
    Hoffmann_Encoder.create_code(encoded_text)

    # Printing the binary codes assigned to characters
    Hoffmann_Encoder.print_encoding_dictionary()
    Hoffmann_Encoder.print_decoding_dictionary()

    # The number of bits used to store uncompressed text (64733646)
    print(len(encoded_text) * 6)
    # The number of bits of the compressed text. (46489714)
    print(len(Hoffmann_Encoder.encode(encoded_text)))
    # The algorithm has used smaller amount bits to save the text.

    # Asserting that the decoder and encoder works properly.
    assert encoded_text == Hoffmann_Encoder.decode(Hoffmann_Encoder.encode(encoded_text))
