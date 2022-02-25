import numpy as np
import scipy.signal as signal


def add_padding(size, shape, matrix):
    if size == 0:
        return matrix
    padded_image = np.zeros((shape[0] + size * 2, shape[1] + size * 2))
    padded_image[size:-size, size:-size] = matrix
    return padded_image


def conv(matrix_to_convolve, mask, padding=0):
    shape = matrix_to_convolve.shape
    if shape[0] > 1:
        mask_shape = mask.shape
        # numpy.pad didnt know if i can use it
        image = add_padding(padding, shape, matrix_to_convolve)
        final = np.zeros(((matrix_to_convolve.shape[0] - mask_shape[0] + padding) + 1, (matrix_to_convolve.shape[1] - mask_shape[1] + padding) + 1))
        for x in range(0, final.shape[0]):
            for y in range(0, final.shape[1]):
                # print((mask * image[x: x + mask_shape[0], y: y + mask_shape[1]]), (x, y))
                final[x, y] = (mask * image[x: x + mask_shape[0], y: y + mask_shape[1]]).sum()
        return final
    else:
        mask_shape = mask.shape
        padded_image = np.zeros((shape[1] + 2 * padding))
        print(padded_image)
        padded_image[padding: -padding] = matrix_to_convolve
        final = np.zeros(matrix_to_convolve.shape[1] - mask_shape[1] + 2 * padding + 1)
        for x in range(0, final.shape[0]):
            # print(np.array(list(padded_image[x: x+ mask_shape[1]])) * mask)
            final[x] = (np.array(list(padded_image[x: x + mask_shape[1]])) * mask).sum()
        return np.array([final])


if __name__ == '__main__':
    f = np.array([[1, 2, 3, 4],
                  [2, 2, 2, 4],
                  [3, 3, 3, 4],
                  [1, 2, 2, 2]], np.uint8)
    g = np.array([[1, 1],
                  [1, 1]], np.uint8)
    f = np.array([[1, 2, 3,5,5]], np.uint8)
    g = np.array([[1, 0, 1]], np.uint8)
    fg = conv(f, g,1)
    print(fg)
    # print(np.array([1, 0, 1] * np.array([2, 5, 2])).shape)
    # print(signal.convolve2d(f, g, mode="valid"))
    # print(fg)
