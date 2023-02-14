from PIL import Image


def lerp(value, *colors):
    return [
        colors[0][0] + (colors[1][0] - colors[0][0]) * value,
        colors[0][1] + (colors[1][1] - colors[0][1]) * value,
        colors[0][2] + (colors[1][2] - colors[0][2]) * value,
    ]


def draw_gradient(w, h, *colors):
    img = Image.new("RGBA", (w, h))

    for i in range(len(colors)-1):
        start_color = colors[i]
        end_color = colors[i+1]

        gradient_w = int(w/(len(colors)-1))

        for x in range(gradient_w):
            diff = (1 / gradient_w) * x
            # print(diff)
            # print(tuple([round(i * 256) for i in lerp(diff, *[start_color, end_color])]))
            for y in range(h):
                img.putpixel((gradient_w * i + x, y), tuple([round(i * 256) for i in lerp(diff, *[start_color, end_color])]))

    img.show()

# draw_gradient(100, 20, (15.3/100, 11.8/255, 23.9/100), (23.1/100, 15.3/100, 32.9/100), (16.9/100, .4/100, 47.1/100))
draw_gradient(449, 96, (1, 0, 0), (1, 0, 1), (0, 0, 1), (0, 1, 1), (0, 1, 0), (1, 1, 0), (1, 0, 0))