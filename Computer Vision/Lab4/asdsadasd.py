import cv2
import matplotlib.pyplot as plt
import numpy as np

# https://stackoverflow.com/questions/48097941/strided-convolution-of-2d-in-numpy
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


def dilate_f(img, f):
    dilated_img = asStride(np.pad(img, [(0, f.shape[0] - 1), (0, f.shape[1] - 1)]), f.shape, 1)
    # dilated_img = dilated_img > 0
    dilated_img = dilated_img + (dilated_img * f)
    # print(dilated_img.shape)
    dilated_img = dilated_img.reshape(img.shape[0], img.shape[1], f.shape[0] * f.shape[1])
    dilated_img = np.max(dilated_img, axis=2)
    dilated_img = dilated_img.astype(np.int32)
    return dilated_img


def erode_f(img, f):
    eroded_img = asStride(np.pad(img, [(0, f.shape[0] - 1), (0, f.shape[1] - 1)]), f.shape, 1)
    eroded_img = eroded_img - (eroded_img * f)
    # eroded_img = eroded_img > 0
    eroded_img = eroded_img.reshape(img.shape[0], img.shape[1], f.shape[0] * f.shape[1])
    eroded_img = np.min(eroded_img, axis=2)
    eroded_img = eroded_img.astype(np.int32)
    return eroded_img


if __name__ == '__main__':
    img_space_raw = cv2.imread('./supernova.png', 0)
    struct_float = np.array([
        [0.1, 0.5, 0.1],
        [0.5, 0.8, 0.5],
        [0.1, 0.5, 0.1]
    ])
    img_space_dil_bin = dilate_f(img_space_raw, struct_float)
    print(img_space_dil_bin[img_space_dil_bin > 250])
    # cv2.imshow("xd", img_space_dil_bin)
    # cv2.waitKey()
    # plt.imshow(img_space_dil_bin)
    # plt.show()
