# Averaging - low pass filter - it disallows high frequencies (High frequencies are averaged)
# Gaussian - low pass filter "attenuating high frequency signals."
# Sobel - high pass filter - it eliminates all the low frequency content
# Laplasjan - high pass filter - the output image has mean value of 0
f_lap_shift, f_lap_mag = fft(laplacian, (512, 512))
fshift, spectrum = fft(img_grayscale)
img_i = np.real(ifft(fshift * f_lap_shift))

imshow(np.concatenate([img_grayscale, np.real(spectrum * f_lap_shift), img_i], 1))

img_i = cv2.filter2D(img_grayscale, -1, laplacian)
imshow(np.concatenate([img_grayscale, img_i], 1))

imshow((np.concatenate([combined_f, combined_i],1))
files = [
    './bug/b_bigbug0000_croppped.png',
    './bug/b_bigbug0001_croppped.png',
    './bug/b_bigbug0002_croppped.png',
    './bug/b_bigbug0003_croppped.png',
    './bug/b_bigbug0004_croppped.png',
    './bug/b_bigbug0005_croppped.png',
    './bug/b_bigbug0006_croppped.png',
    './bug/b_bigbug0007_croppped.png',
    './bug/b_bigbug0008_croppped.png',
    './bug/b_bigbug0009_croppped.png',
    './bug/b_bigbug0010_croppped.png',
    './bug/b_bigbug0011_croppped.png',
    './bug/b_bigbug0012_croppped.png',
]

def detect_prec(img):
    img_grayscale = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY) / 255.0
    img_edges = cv2.filter2D(img_grayscale, -1, g)
    img_edges = np.abs(img_edges)
    img_edges = rescale_intensity(img_edges)
    # print(img_edges[img_edges>0.5])
    # imshow(np.concatenate([img_grayscale, img_edges], 1) * 255.0)
    return img_edges


def merge(bugs, bugs_prec):
  a = np.zeros((306,403,3))
  b = np.zeros((306,403,3))
  for x in range(0, len(bugs)):
    bugs_prec[x] = np.repeat(bugs_prec[x], 3)
    bugs_prec[x] = np.resize(bugs_prec[x], (306,403,3))
    bugs[x] = bugs[x] * bugs_prec[x]
    b = b + bugs_prec[x]
    a = a+ bugs[x]
  return a/b
  # imshow(final[1]*255)

# data loading
bugs = [cv2.imread(f, 1) for f in files]
bugs = list(map(lambda i: cv2.resize(i, None, fx=0.3, fy=0.3), bugs))
bugs_prec = list(map(detect_prec, bugs))

# load the ground truth image
result = cv2.imread('./bug/result.png', 1)
result = cv2.resize(result, None, fx=0.3, fy=0.3)

print('\n===')
print('Pictures of ants with different sharpness at different distances\n')
imshow(np.concatenate(bugs[0:4], 1))
imshow(np.concatenate(bugs[4:8], 1))
imshow(np.concatenate(bugs[8:12], 1))

imshow((np.concatenate(bugs_prec[0:4], 1) * 255).astype(np.uint8))
imshow((np.concatenate(bugs_prec[4:8], 1) * 255).astype(np.uint8))
imshow((np.concatenate(bugs_prec[8:12], 1) * 255).astype(np.uint8))
bugs_copy = np.copy(bugs)
bug_combined = merge(bugs_copy, bugs_prec)
bug = np.stack(bugs, 0).mean(0)

print('\n===')
print('Normal averaging of the component images_ and the target image\n')
imshow(np.concatenate([result, bug], 1))

print('\n===')
print('The result of image reconstruction based on the detection of high-sharpness areas\n')
imshow(np.concatenate([result, bug_combined], 1))