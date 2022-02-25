import matplotlib.pyplot as plt
import numpy as np
from skimage.feature import hog
from skimage import data, exposure
from pprint import pprint
import sys
import cv2

np.set_printoptions(threshold=sys.maxsize)
if __name__ == '__main__':
    image = cv2.imread("cathedral/d001.jpg")
    second_image = cv2.imread("cathedral/d005.jpg")
    param =36
    fd, hog_image = hog(image, orientations=8, pixels_per_cell=(param,param),
                        cells_per_block=(1, 2), visualize=True)
    fd, hog_image2 = hog(second_image, orientations=8, pixels_per_cell=(param,param),
                         cells_per_block=(1, 2), visualize=True)
    print(image.shape)

    #
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(26, 10))

    ax1.axis('off')
    ax1.imshow(image, cmap=plt.cm.gray)
    ax1.set_title('Input image')

    # Rescale histogram for better display
    # hog_image = exposure.rescale_intensity(hog_image, in_range=(0, 10))
    # hog_image2 = exposure.rescale_intensity(hog_image2, in_range=(0, 10))
    # print(image.shape)
    # pprint(hog_image_rescaled.shape)

    ax2.axis('off')
    ax2.imshow(hog_image, cmap=plt.cm.gray)
    ax2.set_title('Histogram of Oriented Gradients')

    ax3.axis('off')
    ax3.imshow(hog_image2, cmap=plt.cm.gray)
    ax3.set_title('Histogram of Oriented Gradients')
    plt.show()
    # print(image.shape)
    print(hog_image[490:500, 742:752])
    print()
    print(hog_image2[490:500, 742:752])
    # print(hog_image2)
    # print(hog_image[490:499, 740:752] == hog_image2[490:499, 740:752])
    table1 = hog_image2 * 0.8 <= hog_image
    table2 = hog_image <= hog_image2 * 1.2
    print(500*752)
    print(np.sum((table1 * table2).astype(np.int8)))
    image[table1 * table2] = second_image[table1 * table2]
    plt.imshow(image)
    plt.show()
    # print(image[table2 *one_table].shape)
    # print(image[hog_image2 * 0.9 < hog_image < hog_image2 * 1.1].shape)
