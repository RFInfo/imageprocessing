package edu.info.ip.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageUtil {

    public static BufferedImage loadImage(String filename){
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static void saveImage(BufferedImage img, String fileName, String formatName){
        try {
            ImageIO.write(img, formatName, new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayImage(BufferedImage img, String title){
        if(img == null)
            return;
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setFitToScreen(false);
        imagePanel.setImage(img);

        frame.setContentPane(imagePanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void displayImage(BufferedImage img) {
        displayImage(img, "");
    }
    public static BufferedImage applySettingsDlg(BufferedImage img,
                                                 AbstractSettingsDialog dialog) {
        if (img == null)
            return null;
        JFrame frame = new JFrame();
        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setFitToScreen(false);
        imagePanel.setImage(img);
        frame.setContentPane(new JScrollPane(imagePanel));
        frame.pack();
        frame.setVisible(true);
        dialog.setImagePanel(imagePanel);
        dialog.pack();
        dialog.setVisible(true);
        frame.dispose();
        return imagePanel.getImage();
    }
    public static BufferedImage generateRandomImage(int width, int height){
        BufferedImage img = null;

//        img = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                img.getRaster().setSample(x,y,0, rand.nextInt(256));
                img.getRaster().setSample(x,y,1, rand.nextInt(256));
                img.getRaster().setSample(x,y,2, rand.nextInt(256));
            }

        return img;
    }

    public static BufferedImage extractBand(BufferedImage inImg, char band){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < inImg.getHeight(); y++)
            for (int x = 0; x < inImg.getWidth(); x++) {
                int pixel = inImg.getRGB(x,y);

                int alpha = (pixel & 0xff000000) >> 24; // (pixel >> 24) & 0xff
                int red =   (pixel & 0x00ff0000) >> 16; // (pixel >> 16) & 0xff
                int green = (pixel & 0x0000ff00) >> 8;  // (pixel >> 8) & 0xff
                int blue =  (pixel & 0x000000ff);       // (pixel) & 0xff

                // how to recompose
                //pixel = 0x00000000 | (alpha << 24) | (red << 16) | (green << 8) | blue;

                // print first row
//                if(y == 0)
//                    System.out.println(alpha + " " + red + " " + green + " " + blue);

                switch (band){
                    case 'A' -> outImg.getRaster().setSample(x,y,0,alpha);
                    case 'R' -> outImg.getRaster().setSample(x,y,0,red);
                    case 'G' -> outImg.getRaster().setSample(x,y,0,green);
                    case 'B' -> outImg.getRaster().setSample(x,y,0,blue);
                }
            }
        return outImg;
    }

    public static BufferedImage extractBandV2(BufferedImage inImg, int band){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < inImg.getHeight(); y++)
            for (int x = 0; x < inImg.getWidth(); x++) {
                int pixel = inImg.getRaster().getSample(x,y,band);
                    outImg.getRaster().setSample(x,y,0,pixel);
            }
        return outImg;
    }

    public static BufferedImage grayLevelGenerator(int firstGrayLevel, int blockSize, int grayLevelStep, int imgHeight){

        int imgWidth = ((256 - firstGrayLevel) / grayLevelStep) * blockSize;

        BufferedImage outImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < outImg.getHeight(); y++) {
            int grayLevel = firstGrayLevel;
            for (int x = 0; x < outImg.getWidth(); x += blockSize) {
                for (int xi = 0; xi < blockSize; xi++) {
                    outImg.getRaster().setSample(x + xi, y, 0, grayLevel);
                }
                grayLevel += grayLevelStep;
            }
        }

        return outImg;
    }

    public static BufferedImage pixelate(BufferedImage inImg, int blockSize){
        BufferedImage outImg = null;

        if((inImg.getWidth() % blockSize) != 0 || (inImg.getHeight() % blockSize != 0)){
            System.out.println("Wrong image size!");
            return outImg;
        }

        outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(), inImg.getType());

        for (int band = 0; band < inImg.getRaster().getNumBands(); band++)
        for (int y = 0; y < outImg.getHeight(); y+=blockSize)
            for (int x = 0; x < outImg.getWidth(); x+=blockSize) {
                int grayLevelSum = 0;
                for(int yi = 0; yi <blockSize; yi ++)
                    for(int xi = 0; xi <blockSize; xi ++)
                        grayLevelSum+=inImg.getRaster().getSample(x+xi, y+yi, band);

                int avgGrayLevel = grayLevelSum / (blockSize * blockSize);

                for(int yi = 0; yi <blockSize; yi ++)
                    for(int xi = 0; xi <blockSize; xi ++)
                        outImg.getRaster().setSample(x+xi, y+yi,band, avgGrayLevel);
            }
        return outImg;
    }

    public static BufferedImage flipV(BufferedImage inImg) {
        BufferedImage outImg = new BufferedImage(inImg.getWidth(), inImg.getWidth(), inImg.getType());

        for (int y = 0; y < outImg.getHeight() / 2; y++)
            for (int x = 0; x < outImg.getWidth(); x++) {
//                int pixel = inImg.getRGB(x,y);
                outImg.setRGB(x, y, inImg.getRGB(x, (inImg.getHeight() - 1) - y));
                outImg.setRGB(x, (inImg.getHeight() - 1) - y, inImg.getRGB(x, y));
            }

        return outImg;
    }

    public static BufferedImage flipH(BufferedImage inImg) {
        BufferedImage outImg = new BufferedImage(inImg.getWidth(), inImg.getWidth(), inImg.getType());

        for (int y = 0; y < outImg.getHeight(); y++)
            for (int x = 0; x < outImg.getWidth() / 2; x++) {
//                int pixel = inImg.getRGB(x,y);
                outImg.setRGB(x, y, inImg.getRGB((inImg.getWidth() - 1) - x, y));
                outImg.setRGB((inImg.getWidth() - 1) - x, y, inImg.getRGB(x, y));
            }

        return outImg;
    }

    public static int constrain(int val, int min, int max){
        return val > max ? max : (val < min ? min : val);
    }
    public static int constrain(int val){
        return constrain(val,0,255);
    }

    public static BufferedImage toGray(BufferedImage input) {
        BufferedImage output;
        output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < input.getHeight(); y++)
            for (int x = 0; x < input.getWidth(); x++) {
                int r = input.getRaster().getSample(x, y, 0);
                int g = input.getRaster().getSample(x, y, 1);
                int b = input.getRaster().getSample(x, y, 2);
                output.getRaster().setSample(x, y, 0, (r + g + b) / 3);
            }
        return output;
    }

    public enum GrayTransforms {
        GRAY_TRANSFORMS_GREEN, GRAY_TRANSFORMS_SQRT,
        GRAY_TRANSFORMS_AVG, GRAY_TRANSFORMS_USUAL, GRAY_TRANSFORMS_PAL
    }
    public static BufferedImage colorToGray(BufferedImage inImg, GrayTransforms version) {
        BufferedImage outImg = new
                BufferedImage(inImg.getWidth(), inImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        if (inImg.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            inImg.copyData(outImg.getRaster());
            return outImg;
        }
        for (int y = 0; y < inImg.getHeight(); y++)
            for (int x = 0; x < inImg.getWidth(); x++) {
                int r = inImg.getRaster().getSample(x, y, 0);
                int g = inImg.getRaster().getSample(x, y, 1);
                int b = inImg.getRaster().getSample(x, y, 2);
                int grayLevel = 0;
                switch (version) {
                    case GRAY_TRANSFORMS_GREEN -> grayLevel = g;
                    case GRAY_TRANSFORMS_SQRT -> grayLevel =
                            constrain((int) Math.round(Math.sqrt(r * r + g * g + b * b)));
                    case GRAY_TRANSFORMS_AVG -> grayLevel =
                            constrain((int) Math.round((double) (r + g + b) / 3));
                    case GRAY_TRANSFORMS_USUAL -> grayLevel =
                            constrain((int) Math.round((double) (3 * r + 2 * g + 4 * b) / 9));
                    case GRAY_TRANSFORMS_PAL -> grayLevel =
                            constrain((int) Math.round(0.299 * r + 0.587 * g + 0.114 * b));
                }
                outImg.getRaster().setSample(x, y, 0, grayLevel);
            }
        return outImg;
    }

    public static BufferedImage brightnessV1(BufferedImage inImg, int offset){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),inImg.getType());

        for (int band = 0; band < inImg.getRaster().getNumBands(); band++)
        for (int y = 0; y < inImg.getHeight(); y++)
            for (int x = 0; x < inImg.getWidth(); x++) {
                int inGrayLevel = inImg.getRaster().getSample(x,y,band);
                int outGrayLevel = constrain(inGrayLevel + offset);
                outImg.getRaster().setSample(x,y, band, outGrayLevel);
            }

        return outImg;
    }

    public static BufferedImage brightnessV2(BufferedImage inImg, int offset){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),inImg.getType());

        short[] brightnessLUT = new short[256];
        for (int i = 0; i < brightnessLUT.length; i++) {
            brightnessLUT[i] = (short) constrain(i+offset);
            System.out.print(brightnessLUT[i]+" ");
        }

        for (int band = 0; band < inImg.getRaster().getNumBands(); band++)
            for (int y = 0; y < inImg.getHeight(); y++)
                for (int x = 0; x < inImg.getWidth(); x++) {
                    int inGrayLevel = inImg.getRaster().getSample(x,y,band);
                    int outGrayLevel = brightnessLUT[inGrayLevel];
                    outImg.getRaster().setSample(x,y, band, outGrayLevel);
                }

        return outImg;
    }
    public static BufferedImage brightnessV3(BufferedImage inImg, int offset){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),inImg.getType());

        short[] brightnessLUT = new short[256];
        for (int i = 0; i < brightnessLUT.length; i++) {
            brightnessLUT[i] = (short) constrain(i+offset);
            System.out.print(brightnessLUT[i]+" ");
        }

        ShortLookupTable shortLookupTable = new ShortLookupTable(0,brightnessLUT);
        LookupOp lookupOp = new LookupOp(shortLookupTable, null);
        lookupOp.filter(inImg,outImg);

        return outImg;
    }

    public static BufferedImage brightnessRGB(BufferedImage inImg, int offsetR, int offsetG, int offsetB){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),inImg.getHeight(),inImg.getType());

        short[] rLUT = new short[256];
        short[] gLUT = new short[256];
        short[] bLUT = new short[256];

        short[][] rgbLUT = {rLUT, gLUT, bLUT};

        for (int i = 0; i < 256; i++) {
            rLUT[i] = (short) constrain(i+offsetR);
            gLUT[i] = (short) constrain(i+offsetG);
            bLUT[i] = (short) constrain(i+offsetB);
        }

        ShortLookupTable shortLookupTable = new ShortLookupTable(0,rgbLUT);
        LookupOp lookupOp = new LookupOp(shortLookupTable, null);
        lookupOp.filter(inImg,outImg);

        return outImg;
    }

    public static BufferedImage contrastSimple(BufferedImage src, float scale){
        BufferedImage dst = null;
        dst = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
        short[] contrastLUT = new short[256];
        for(short i = 0; i<contrastLUT.length; i++){
            contrastLUT[i] = (short)constrain(Math.round(scale * i));
        }
        ShortLookupTable lut = new ShortLookupTable(0,contrastLUT);
        LookupOp op = new LookupOp(lut,null);
        op.filter(src,dst);
        return dst;
    }

    public static BufferedImage contrastGamma(BufferedImage src, double gamma){
        BufferedImage dst = null;
        dst = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
        short[] contrastLUT = new short[256];
        for(short i = 0; i<contrastLUT.length; i++){
            double a = i / 255.0; // scale to [0..1]
            double b = Math.pow(a,1.0/gamma);
            contrastLUT[i] = (short)constrain((int)Math.round(b * 255.0)); // scale to [0..255]
        }
        for (int i = 0; i < 256; i++)
            System.out.print(contrastLUT[i] + ", ");
        System.out.println();
        ShortLookupTable lut = new ShortLookupTable(0,contrastLUT);
        LookupOp op = new LookupOp(lut,null);
        op.filter(src,dst);
        return dst;
    }

    public static int normalize(int val, int oldMin, int oldMax, int newMin, int newMax){
        double c = 1.0 * (newMax - newMin)/(oldMax - oldMin);
        return (int)Math.round(c*(val - oldMin) + newMin);
    }
    public static BufferedImage contrastStretch(BufferedImage src){
        BufferedImage dst = new
                BufferedImage(src.getWidth(),src.getHeight(),src.getType());
        short[][] contrastStretchLUT = new short[src.getRaster().getNumBands()][256];
        for(int b = 0; b<src.getRaster().getNumBands(); b++) {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            int pixel;
            // find max and min
            for (int y = 0; y < src.getHeight(); y++)
                for (int x = 0; x < src.getWidth(); x++) {
                    pixel = src.getRaster().getSample(x, y, b);
                    if (pixel > max)
                        max = pixel;
                    if (pixel < min)
                        min = pixel;
                }
            System.out.println("min= " + min + " max= " + max);
            for (int i = min; i <= max; i++)
                contrastStretchLUT[b][i] = (short) normalize(i, min, max, 0, 255);
            for (int i = 0; i < 256; i++)
                System.out.print(contrastStretchLUT[b][i] + ", ");
            System.out.println();
        }
        ShortLookupTable lut;
        if(src.getRaster().getNumBands() < 3)
            lut = new ShortLookupTable(0,contrastStretchLUT[0]);
        else
            lut = new ShortLookupTable(0,contrastStretchLUT);
        LookupOp op = new LookupOp(lut,null);
        op.filter(src,dst);
        return dst;
    }

    public static BufferedImage negative(BufferedImage src){
        BufferedImage dst = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
        short[] negativeLUT = new short[256];
        for(short i = 0; i<negativeLUT.length; i++){
            negativeLUT[i] = (short)constrain(255 - i);
        }
        ShortLookupTable lut = new ShortLookupTable(0,negativeLUT);
        LookupOp op = new LookupOp(lut,null);
        op.filter(src,dst);
        return dst;
    }

    public static BufferedImage threshold(BufferedImage src, int value){
        BufferedImage dst = null;
        if(src.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            System.out.println("Not a gray image!");
            return dst;
        }
        dst = new
                BufferedImage(src.getWidth(),src.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        short[] thresholdLUT = new short[256];
        for(short i = 0; i<thresholdLUT.length; i++){
            thresholdLUT[i] = (short)((i < value) ? 0 : 255);
        }
        ShortLookupTable lut = new ShortLookupTable(0,thresholdLUT);
        LookupOp op = new LookupOp(lut,null);
        op.filter(src,dst);
        return dst;
    }

    public static BufferedImage applyMask(BufferedImage src, BufferedImage mask){
// src and mask == same size
        BufferedImage dst = null;
        dst = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                int pixel = src.getRGB(x,y);
                dst.setRGB(x,y,(mask.getRaster().getSample(x,y,0)>0) ? pixel : 0);
            }
        return dst;
    }

    static public BufferedImage getBitPlane(BufferedImage inImg, int bitLevel){
        BufferedImage outImg = new BufferedImage(inImg.getWidth(), inImg.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < inImg.getHeight(); y++)
            for (int x = 0; x < inImg.getWidth(); x++) {
                int pixel = inImg.getRaster().getSample(x,y,0);
                pixel = (pixel >>> bitLevel) & 1;
                outImg.getRaster().setSample(x,y,0,pixel);
            }
        return outImg;
    }

}
