import pyrebase
from loadmodel import loadmodel
from flask import Flask

#opencv
import cv2
import numpy as np
import opencv_logic
import matplotlib.pyplot as plt
from scipy.spatial import distance

#ML
import tensorflow as tf
from tensorflow.keras.models import load_model
from keras.preprocessing import image
import matplotlib.pyplot as plt
import os
import numpy as np
from tensorflow.python.keras.backend import set_session
from tensorflow.python.keras.models import load_model

config = {
	"apiKey": "AIzaSyB3BsLBpvphPKPzerp3wnzIAVDE6AswwvE",
  	"authDomain": "talktalk-91d2e.firebaseapp.com",
  	"databaseURL": "https://talktalk-91d2e.firebaseio.com",
  	"projectId": "talktalk-91d2e",
  	"storageBucket": "talktalk-91d2e.appspot.com",
  	"messagingSenderId": "532264603729",
  	"appId": "1:532264603729:web:bcbf7464b1a4029417a870",
  	"measurementId": "G-2WSGERGV7Q"
}

ClothesList = ['top_hoodie_black_pattern', 'top_hoodie_black_plain', 'top_hoodie_blue_pattern',
            'top_hoodie_blue_plain', 'top_hoodie_brown_pattern', 'top_hoodie_brown_plain',
          'top_hoodie_green_pattern', 'top_hoodie_green_plain', 'top_hoodie_grey_pattern',
          'top_hoodie_grey_plain', 'top_hoodie_red_pattern', 'top_hoodie_red_plain',
          'top_hoodie_white_pattern', 'top_hoodie_white_plain', 'top_hoodie_yellow_pattern',
          'top_hoodie_yellow_plain']

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2' #for ignore error

firebase = pyrebase.initialize_app(config)

db = firebase.database()
from flask import *

app = Flask(__name__)


storage = firebase.storage()


def load_image(img_path, show=False):

    img = image.load_img(img_path, target_size=(150, 150))
    img_tensor = image.img_to_array(img)                    # (height, width, channels)
    img_tensor = np.expand_dims(img_tensor, axis=0)        
    img_tensor /= 255.                                      # imshow expects values in the range [0, 1]

    if show:
        plt.imshow(img_tensor[0])                           
        plt.axis('off')
        plt.show()
    return img_tensor

sess = tf.Session(config=tf.ConfigProto(device_count={'GPU': 0}))
graph = tf.get_default_graph()

set_session(sess)
model = load_model('1129_finalmodel_2.h5')
model.compile(loss='categorical_crossentropy', optimizer='rmsprop', metrics=['accuracy'])
#model._make_predict_function()
print("model loaded")


	
def stream_handler(message):
    global data
    data = message["data"]
    print(data)
    s = str(data)
    storage.child(s).download("./temp/myimg.jpg")
    if os.path.isfile('./temp/myimg.jpg') :
        ml()

my_stream = db.child("images").stream(stream_handler)


def opencv(label_name, img):
    path_dir = "./data/"
    dir_list = os.listdir(path_dir)
    
    for dl in dir_list :
        if label_name == dl:
            category_dir = os.path.join(path_dir, dl)
            break
    
    print(category_dir)
    
    file_list = os.listdir(category_dir)
    print(file_list)
    
    img1 = opencv_logic.ImgCropResize(img) # input data 
    print("gogo")
    flag = 0
    for fl in file_list:
        img_dir = os.path.join(category_dir, fl)
        print(img_dir)
        img2 = cv2.imread(img_dir)
        img2 = opencv_logic.ImgCropResize(img2) # ref data 
        result = opencv_logic.UseHogGetSimilarity(img1, img2)
        print(result)
        
        if flag == 0: 
            min_result = result
            match_img = fl
            
        if min_result > result :
            min_result = result
            match_img = fl
        
        flag = 1
    print(match_img)
    return match_img

def GetImageNametoString(match_img):
    list = []
    for s in match_img:
        if s != '.':
            list.append(s)
        else :
            break
    res = ''.join(list)
    return res

def GetImageList(image_name):
    list = []
    path_dir = 'codylist'
    dir_list = os.listdir(path_dir)
    #print(dir_list)
    for dl in dir_list :
        if image_name in dl :
            list.append(dl)
    #print(list)
    return list

def ml():
    print("ml start")
    
    img = load_image("./temp/myimg.jpg")
    
    with graph.as_default():
        set_session(sess)
        y = model.predict_classes(img)
    #print(y)
    result = y[0]
    for num in range(0, 17):
        if result == num :
            label_name = ClothesList[num]
    print(label_name)

    #opencv
    src = cv2.imread('./temp/myimg.jpg')
    match_image = opencv(label_name, src) 
    print(match_image)

    image_name = GetImageNametoString(match_image)
    #print(image_name)
    cody_list = GetImageList(image_name)
    #print(cody_list)
   
    for i, cody in enumerate(cody_list) :
        inputdata = {str(i) : "codylist/" + cody}
        db.update(inputdata)
        storage.child("codylist/" + cody).put("./codylist/"+ cody)
    

@app.route('/')
def start():
    print("start")

#methods=['GET', 'POST']


if __name__ == '__main__':
	app.run(debug=True)


