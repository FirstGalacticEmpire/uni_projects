import cv2
import matplotlib.pyplot as plt
import numpy as np
import PIL
import math
from pprint import pprint as pp


def BGR2RGB(img_bgr):
    return img_bgr[:, :, ::-1]


def BGR2HSV(image):
    shape_of_the_input = image.shape
    dummy_coppy_of_rgb = image
    image = image.reshape(-1, 3)
    r, g, b = image[:, 0], image[:, 1], image[:, 2]
    r, g, b = r / 255, g / 255, b / 255

    maxC = np.maximum(np.maximum(b, g), r)  # r,g,b
    minC = np.minimum(np.minimum(b, g), r)

    delta = maxC - minC

    V = maxC

    # To avoid divide by zero error.
    delta[np.where(maxC == 0)] = 0
    maxC[maxC == 0] = 1
    ###
    S = delta / maxC

    # Refreshing values
    delta = maxC - minC
    maxC = np.maximum(np.maximum(r, g), b)
    minC = np.minimum(np.minimum(r, g), b)

    delta[delta == 0] = 1
    H = ((r - g) / delta + 4) * 60
    H[g == maxC] = ((b[g == maxC] - r[g == maxC]) / delta[g == maxC] + 2) * 60
    H[maxC == r] = ((g[maxC == r] - b[maxC == r]) / delta[maxC == r] % 6) * 60
    H[maxC == minC] = 0
    # H = H/2
    S = S * 255
    V = V * 255
    H = H / 2

    # S = np.trunc(S)
    # V = np.ceil(V)
    H = np.ceil(H)

    final_image = np.dstack([H, S, V])
    final_image = final_image.reshape(shape_of_the_input)
    final_image = final_image.astype(int)

    return final_image


def BGR2Gray(image_bgr):
    image_bgr = image_bgr[:, :, ::-1]
    r, g, b = image_bgr[:, :, 0], image_bgr[:, :, 1], image_bgr[:, :, 2]
    gray = 0.2989 * r + 0.5870 * g + 0.1140 * b
    gray = gray.astype(int)
    # plt.imshow(gray)
    # plt.show()
    return gray


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    img_pil = np.array(PIL.Image.open('./lena_std.tif'))
    img_pil = cv2.resize(img_pil, (256, 256))
    original = cv2.cvtColor(img_pil, cv2.COLOR_BGR2GRAY)
    plt.imshow(original)
    plt.show()
    print(original[1])
    print(BGR2Gray(img_pil)[1])
    new_image = BGR2Gray(img_pil)
    print(np.allclose(original, new_image, 1, 0))
    # plt.show()

    # img = img_pil
    # img = BGR2HSV(img)
    # plt.imshow(img)
    # plt.show()
