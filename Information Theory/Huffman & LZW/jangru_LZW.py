# While making this project, I've used following resources:
# https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch
# https://rosettacode.org/wiki/LZW_compression

import pickle
from io import StringIO
import six


def read_bytes_and_return_generator(file_path, buffer_size=1):
    with open(file_path, "rb") as file:
        while True:
            buffer = file.read(buffer_size)
            if buffer:
                for b in buffer:
                    yield b
            else:
                break


def load_text_into_memory(file_path="text.txt"):
    with open(file_path, "r") as file:
        return file.read()


def encode_using_LZW(data, print_dictionary=False):
    dictionary_size = 256
    dictionary = {chr(a): a for a in range(dictionary_size)}

    result = []
    code = ""

    for char in data:

        temp_string = code + char
        if temp_string in dictionary:
            code = temp_string
        else:
            result.append(dictionary[code])
            dictionary[temp_string] = dictionary_size
            dictionary_size += 1
            code = char
    if code:
        result.append(dictionary[code])

    if print_dictionary:
        print({key: v for key, v in sorted(dictionary.items(), key=lambda item: item[1], reverse=True)})
    return result


def decode_using_LZW(data):
    dictionary_size = 256
    dictionary = {a: chr(a) for a in range(dictionary_size)}

    # StringIO() is used to avoid using string concatenation in a loop
    encoded_data = StringIO()

    temp_char = chr(data.pop(0))
    encoded_data.write(temp_char)

    for key in data:

        if key in dictionary:
            value = dictionary[key]
        elif key == dictionary_size:
            value = temp_char + temp_char[0]
        encoded_data.write(value)

        dictionary[dictionary_size] = temp_char + value[0]
        dictionary_size += 1
        temp_char = value

    encoded_data = encoded_data.getvalue()
    return encoded_data


def compress_bitmap(path_to_bitmap="lab_lzw/lena.BMP", path_to_be_saved="compressed.lzw"):
    bytes_generator = read_bytes_and_return_generator(path_to_bitmap)
    data = [str(a) for a in bytes_generator]

    result = StringIO()
    for char in data:
        result.write(" " + char)
    compressed = encode_using_LZW(result.getvalue())

    with open(path_to_be_saved, "wb") as file:
        pickle.dump(compressed, file)


def decompress_bitmap(path_to_bitmap="compressed.lzw", path_to_be_saved="mytest3.bmp"):
    with open(path_to_bitmap, "rb") as file:
        data = pickle.load(file)

    decompressed = decode_using_LZW(data)
    decompressed = [int(x) for x in decompressed.split()]

    with open(path_to_be_saved, "wb") as file:
        file.flush()
        for x in decompressed:
            file.write(six.int2byte(x))


def compress_text(text_to_be_compressed, path):
    compressed = encode_using_LZW(text_to_be_compressed)
    with open(path, "wb") as file:
        pickle.dump(compressed, file)


def decompress_text(path):
    with open(path, "rb") as file:
        compressed = pickle.load(file)
    print(decode_using_LZW(compressed))


if __name__ == '__main__':
    # Short example:
    data = encode_using_LZW("test text to be encoded ASDDDDDDaaaa", True)
    print(data)
    print((decode_using_LZW(data)))

    # Compressing text:
    text = load_text_into_memory("lab_lzw/norm_wiki_sample.txt")
    compress_text(text, "norm_wiki_sample.lzw")
    decompress_text("norm_wiki_sample.lzw")

    text = load_text_into_memory("lab_lzw/wiki_sample.txt")
    compress_text(text, "wiki_sample.lzw")
    decompress_text("wiki_sample.lzw")

    # Compressing bitmap
    # With redundant data in the bitmap, (different example than lena)
    # the algorithm works very well
    compress_bitmap("lab_lzw/bitmap.bmp", "compressed_bitmap.lzw")
    decompress_bitmap("compressed_bitmap.lzw", "decompressed_bitmap.bmp")

    # With data with not much redundancy the algorithm works not so well
    compress_bitmap("lab_lzw/lena.bmp", "compressed_lena.lzw")
    decompress_bitmap("compressed_lena.lzw", "decompressed_lena.bmp")
