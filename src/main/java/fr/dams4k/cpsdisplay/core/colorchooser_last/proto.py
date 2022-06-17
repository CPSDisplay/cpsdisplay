from PIL import Image
import colorsys

def hsv2rgb(h,s,v):
    return tuple(round(i * 255) for i in colorsys.hsv_to_rgb(h,s,v))

size = (64, 64)

img = Image.new("RGB", size)
img2 = Image.new("RGB", (6, size[1]))

for y in range(size[1]):
    for x in range(size[0]):
        img.putpixel((x, y), hsv2rgb(0, x/(size[0]+1), 1-y/(size[1]+1)))

for y in range(size[1]):
    for x in range(6):
        color = hsv2rgb(y/(size[1]+1), 1, 1)
        img2.putpixel((x,y), color)

img.show()
img2.show()
