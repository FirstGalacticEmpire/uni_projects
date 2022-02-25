import numpy as np
import matplotlib.pyplot as plt
import cv2

g = np.array([
    [0, -1, 0],
    [-1, 4, -1],
    [0, -1, 0]
], np.float32)


# bugs_prec = list(map(detect_prec, bugs))

# # load the ground truth image
# result = cv2.imread('./bug/result.png', 1)
# result = cv2.resize(result, None, fx=0.3, fy=0.3)

# print('\n===')
# print('Pictures of ants with different sharpness at different distances\n')
# imshow(np.concatenate(bugs[0:4], 1))
# imshow(np.concatenate(bugs[4:8], 1))
# imshow(np.concatenate(bugs[8:12], 1))

# imshow((np.concatenate(bugs_prec[0:4], 1) * 255).astype(np.uint8))
# imshow((np.concatenate(bugs_prec[4:8], 1) * 255).astype(np.uint8))
# imshow((np.concatenate(bugs_prec[8:12], 1) * 255).astype(np.uint8))

# bug_combined = merge(bugs, bugs_prec)
# bug = np.stack(bugs, 0).mean(0)

# print('\n===')
# print('Normal averaging of the component images_ and the target image\n')
# imshow(np.concatenate([result, bug], 1))

# print('\n===')
# print('The result of image reconstruction based on the detection of high-sharpness areas\n')
# imshow(np.concatenate([result, bug_combined], 1))
def detect_prec(img):
    img_grayscale = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) / 255.0
    img_edges = cv2.filter2D(img_grayscale, -1, g) * 255
    cv2.imshow("Xd", np.concatenate([img_grayscale, img_edges], 1) * 255.0)
    return img_edges


if __name__ == '__main__':
    # img = cv2.imread("1.png", 1)
    # # img = img[:, :, ::-1]
    # img = detect_prec(img)
    # plt.imshow(img)
    # plt.show()
    # a = [1, 2, 3]
    # print(list(map(a, b)))
