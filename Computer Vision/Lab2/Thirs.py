import cv2
import matplotlib.pyplot as plt
import numpy as np
import PIL
import math
from pprint import pprint as pp

if __name__ == '__main__':
    img = np.array(PIL.Image.open('./lena_std.tif'))
    img = cv2.resize(img, (256, 256))
    plt.imshow(img)
    plt.show()
    # img_pil = img_pil[:, :, ::-1]
    img_gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    where = (img_gray >= 120) & (img_gray <= 160)
    img[where] = ~img[where]

    plt.imshow(img)
    plt.show()
    # plt.imshow(img_gray)
    # plt.show()

    # a = img_gray

    # img_pil[where] = ~img_pil[where]
    #
    # plt.imshow(a)
    # plt.show()