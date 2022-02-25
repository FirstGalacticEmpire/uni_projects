import os
import json
import numpy as np


class Task(object):
    pass


def load_task(dir_name, task_file):
    with open(os.path.join('./ARC-master', 'data', dir_name, task_file), 'rb') as fp:
        task = json.load(fp)

    def __parse_grid(grid):
        in_grid = np.array(grid['input'], np.int32)
        out_grid = np.array(grid['output'], np.int32)
        return in_grid, out_grid

    obj = Task()
    obj.name = os.path.basename(task_file)
    obj.demo_count, obj.test_count = len(task['train']), len(task['test'])
    obj.demo_inputs, obj.demo_outputs = list(zip(*map(__parse_grid, task['train'])))
    obj.demo_in_sizes, obj.demo_out_sizes = [i.shape for i in obj.demo_inputs], [i.shape for i in obj.demo_outputs]
    obj.test_inputs, obj.test_outputs = list(zip(*map(__parse_grid, task['test'])))
    obj.test_in_sizes, obj.test_out_sizes = [i.shape for i in obj.test_inputs], [i.shape for i in obj.test_outputs]
    return obj


def asStride(array_to_stride, sub_shape, stride=1):
    s0, s1 = array_to_stride.strides[:2]
    m1, n1 = array_to_stride.shape[:2]
    m2, n2 = sub_shape[:2]
    # print(arr.shape)
    view_shape = (1 + (m1 - m2) // stride, 1 + (n1 - n2) // stride, m2, n2) + array_to_stride.shape[2:]
    # print(view_shape)
    strides = (stride * s0, stride * s1, s0, s1) + array_to_stride.strides[2:]
    # print(strides)
    result = np.lib.stride_tricks.as_strided(array_to_stride, view_shape, strides=strides)
    # print(result.shape)
    return result


def neighbourhood_descriptor(img):
    padded_img = np.pad(img, 1)
    # print(padded_img)
    strided_result = asStride(padded_img, (3, 3))
    # print(padded_img.shape)
    shape = strided_result.shape
    result = strided_result.reshape((shape[0] * shape[1], 3, 3))

    final = []
    for pixel in result:
        # print(pixel)
        middle_value = pixel[1, 1]
        # print(middle_value)
        # print(mask)
        num_friends = np.count_nonzero(pixel == middle_value) - 1
        final.append(num_friends)
        # print(num_friends)

        # print(mask)
        # print(middle_value)
        # print()
        #
    #     # break
    # print(len(final))
    final = np.array(final)
    final = final.reshape(img.shape)
    # print(final)
    # # print(result[1, 1, 1])
    # # # result[1, 1, 1] = 9
    # # print(result[1])
    return final


if __name__ == '__main__':
    p_73251a56 = load_task('training', '73251a56.json')
    neighbourhood_descriptor(p_73251a56.demo_inputs[0])
