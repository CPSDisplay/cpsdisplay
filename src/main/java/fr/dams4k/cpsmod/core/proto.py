from PIL import Image
import colorsys

def hsv2rgb(h,s,v):
    return tuple(round(i * 255) for i in colorsys.hsv_to_rgb(h,s,v))

size = (255, 255)

img = Image.new("RGB", size)

for y in range(size[1]):
    for x in range(size[0]):
        img.putpixel((x, y), hsv2rgb(0, x/(size[0]+1), 1-y/(size[1]+1)))

img.show()