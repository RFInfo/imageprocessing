package edu.info.ip.main;

import java.awt.image.BufferedImage;

import static edu.info.ip.util.ImageUtil.*;

public class BitPlaneTest {
    public static void main(String[] args) {
        
        BufferedImage inputImg= loadImage("./test_images/rice.bmp");

        displayImage(inputImg, "Original image");

        displayImage(getBitPlane(inputImg,0), "BitPlane 0");
        displayImage(getBitPlane(inputImg,1), "BitPlane 1");
        displayImage(getBitPlane(inputImg,2), "BitPlane 2");
        displayImage(getBitPlane(inputImg,3), "BitPlane 3");
        displayImage(getBitPlane(inputImg,4), "BitPlane 4");
        displayImage(getBitPlane(inputImg,5), "BitPlane 5");
        displayImage(getBitPlane(inputImg,6), "BitPlane 6");
        displayImage(getBitPlane(inputImg,7), "BitPlane 7");
    }
}
