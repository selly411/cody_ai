#!/usr/bin/env python
# coding: utf-8

# In[20]:


import numpy as np
import cv2
import matplotlib.pyplot as plt
from scipy.spatial import distance
import os

def ImgCropResize(img) :
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    bImage = cv2.adaptiveThreshold(gray, 220, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV,51,7)
    bImage = cv2.dilate(bImage, None)


    mode   = cv2.RETR_EXTERNAL
    method = cv2.CHAIN_APPROX_NONE
    image, contours, hierarchy = cv2.findContours(bImage, mode, method)

    maxLength = 0
    k = 0
    for i, cnt in enumerate(contours):
        perimeter = cv2.arcLength(cnt, closed = True)
        if perimeter> maxLength:
            maxLength = perimeter
            k = i
    #print('maxLength=', maxLength)
    cnt = contours[k]


    area = cv2.contourArea(cnt)
    #print('area=', area)
    x, y, width, height = cv2.boundingRect(cnt)
    dst3 = img.copy()
    cv2.rectangle(dst3, (x, y), (x+width, y+height), (0,0,255), 2)
   
    crop_img = img[y:y+height, x:x+width]
    crop_img = cv2.copyMakeBorder(crop_img,30,30,30,30,cv2.BORDER_CONSTANT,value = (255,255,255))
    crop_img = cv2.resize(crop_img,(200,400), interpolation=cv2.INTER_AREA)
    return crop_img

def UseHogGetSimilarity(img1, img2):
    hog1 = cv2.HOGDescriptor()
    des1 = hog1.compute(img1)
    
    hog2 = cv2.HOGDescriptor()
    des2 = hog2.compute(img2)
    
    dist = np.linalg.norm(des1 - des2)
    dist2 = distance.euclidean(des1, des2)
    
    #print(dist)
    #print('euclidean = ', dist2)
    return dist2

