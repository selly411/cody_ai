from tensorflow.keras.models import load_model
from keras.preprocessing import image
import matplotlib.pyplot as plt
import os
import numpy as np

def load_image(img_path, show=False):

    img = image.load_img(img_path, target_size=(150, 150))
    img_tensor = image.img_to_array(img)                    # (height, width, channels)
    img_tensor = np.expand_dims(img_tensor, axis=0)         # (1, height, width, channels), add a dimension because the model expects this shape: (batch_size, height, width, channels)
    img_tensor /= 255.                                      # imshow expects values in the range [0, 1]

    if show:
        plt.imshow(img_tensor[0])                           
        plt.axis('off')
        plt.show()

    return img_tensor

'''
model = load_model('1129_finalmodel_2.h5')
model.compile(loss='categorical_crossentropy',
              optimizer='rmsprop',
              metrics=['accuracy'])
'''
img_dir = "myhood.jpg"
img = load_image(img_dir)

#print(model.predict(img))
y = model.predict_classes(img)
#print(model.predict_classes(img))

#print(y[0])

result = y[0]

ClothesList = ['top_hoodie_black_pattern', 'top_hoodie_black_plain', 'top_hoodie_blue_pattern',
            'top_hoodie_blue_plain', 'top_hoodie_brown_pattern', 'top_hoodie_brown_plain',
          'top_hoodie_green_pattern', 'top_hoodie_green_plain', 'top_hoodie_grey_pattern',
          'top_hoodie_grey_plain', 'top_hoodie_red_pattern', 'top_hoodie_red_plain',
          'top_hoodie_white_pattern', 'top_hoodie_white_plain', 'top_hoodie_yellow_pattern',
          'top_hoodie_yellow_plain']

for num in range(0, 17):
    if result == num :
        label_name = ClothesList[num]

print(label_name)
