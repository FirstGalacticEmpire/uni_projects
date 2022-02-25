import numpy as np
from numpy.lib.stride_tricks import as_strided

img = np.zeros((10, 10), np.uint8)
img[1:3, 1:7] = 1
img[5:9, 2:4] = 1
img[5:8, 5:9] = 1
img[3:5, 2] = 1


def asStride(arr, sub_shape, stride):
    '''Get a strided sub-matrices view of an ndarray.

    <arr>: ndarray of rank 2.
    <sub_shape>: tuple of length 2, window size: (ny, nx).
    <stride>: int, stride of windows.

    Return <subs>: strided window view.

    See also skimage.util.shape.view_as_windows()
    '''
    s0, s1 = arr.strides[:2]
    m1, n1 = arr.shape[:2]
    m2, n2 = sub_shape[:2]

    view_shape = (1 + (m1 - m2) // stride, 1 + (n1 - n2) // stride, m2, n2) + arr.shape[2:]
    # print(view_shape)
    strides = (stride * s0, stride * s1, s0, s1) + arr.strides[2:]
    subs = np.lib.stride_tricks.as_strided(arr, view_shape, strides=strides)

    return subs


def dilate(img, f):
    dilated_img = asStride(np.pad(img, [(0, 1), (0, 1)]), (2, 2), 1) * f
    dilated_img = dilated_img > 0
    print(dilated_img.shape)
    dilated_img = dilated_img.reshape(10, 10, 4)
    dilated_img = np.any(dilated_img, axis=2)
    dilated_img = dilated_img.astype(np.int8)
    return dilated_img


def erode(img, f):
    eroded_img = asStride(np.pad(img, [(0, 1), (0, 1)]), (2, 2), 1) * f
    eroded_img = eroded_img > 0
    eroded_img = eroded_img.reshape(10, 10, 4)
    eroded_img = np.all(eroded_img, axis=2)
    eroded_img = eroded_img.astype(np.int8)
    return eroded_img


if __name__ == '__main__':
    f = np.array([[1, 1, 1],
                  [1, 1, 1],
                  [1, 1, 1]])
    # g = np.array([[1, 1, 1], [1, 1, 1], [1, 1, 1]])
    # print(img)
    # g = np.array([[0, -1], [1, 0]])
    # print(np.pad(f, 1))
    # print(np.pad(img,1))
    # print()
    # print(np.pad(f, [(1, 0), (1, 1)]))
    # xd = asStride(np.pad(f, [(1, 0), (1, 1)]), mode='constant'), (3, 3), 1))
    print(asStride(np.pad(img, [(1, 0), (1, 1)]), (3, 3), 1))

    xd = asStride(np.pad(img, [(1, 1), (1, 1)]), (3, 3), 1) * f

    print(xd)
    xd = xd >= 0

    xd = xd.reshape(10, 10, 9)
    xd = np.any(xd, axis=2)

    print(xd.astype(np.int8))
