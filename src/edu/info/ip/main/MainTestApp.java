package edu.info.ip.main;

import java.awt.image.BufferedImage;

import static edu.info.ip.util.ImageUtil.*;

public class MainTestApp {
    public static void main(String[] args) {

        String imageFile = "./test_images/lena_color_512.bmp";
//        String imageFile = "./test_images/lena_gray_512.bmp";
//        String imageFile = "./test_images/halloween.png";

        BufferedImage img = loadImage(imageFile);

//        displayImage(img, "Original Image");

//        ImageUtil.saveImage(img, "lena.jpg", "jpg");
//        ImageUtil.saveImage(img, "lena.bmp", "bmp");
//        ImageUtil.saveImage(img, "lena.png", "png");

        // Lab02

//        ImageUtil.displayImage(ImageUtil.generateRandomImage(800,800),"Random Gray Image");
//        ImageUtil.displayImage(ImageUtil.extractBand(img,'R'), "Banda R");
//        ImageUtil.displayImage(ImageUtil.extractBand(img,'G'), "Banda G");
//        ImageUtil.displayImage(ImageUtil.extractBand(img,'B'), "Banda B");
//        ImageUtil.displayImage(ImageUtil.extractBand(img,'A'), "Banda A");

//        ImageUtil.displayImage(ImageUtil.extractBandV2(img,0), "Banda R");
//        ImageUtil.displayImage(ImageUtil.extractBandV2(img,1), "Banda G");
//        ImageUtil.displayImage(ImageUtil.extractBandV2(img,2), "Banda B");
//        ImageUtil.displayImage(ImageUtil.extractBandV2(img,3), "Banda A");

        // Lab03

//            displayImage(grayLevelGenerator(0, 40,50, 400), "Gray Level Generator");

        displayImage(pixelate(img,128),"Pixelate");
    }
}
