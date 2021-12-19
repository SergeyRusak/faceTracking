package com.company;

import org.opencv.core.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

public class Main {

    public static float compareImage(File fileA, File fileB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        System.out.println(percentage +"% similar");
        return percentage;
    }




    public static void main(String[] args) throws IOException {

        VideoProcessing vp = new VideoProcessing("videoplayback.mp4");
        FaceDetect fd = new FaceDetect();
        double frame_rate = vp.getFrame_rate();
        File prerender = new File(vp.getFramesPath());
        //File prerender = new File("prerender/");

        File[] frames = prerender.listFiles();
        int i = 0;
        Rect[] faces = fd.faceDetect(frames[i]);
        while (faces == null){
            i++;
            faces = fd.faceDetect(frames[i]);

        }



        for (; i <frames.length ; i++) {
            if (compareImage(frames[i-1],frames[i])<60){
               faces = fd.faceDetect(frames[i]);
                while (faces == null){
                    i++;
                    faces = fd.faceDetect(frames[i]);

                }


            }else{
                fd.draw(frames[i]);
            }
            if((int)(((i*1.0f)/frames.length)*1000)%50 ==0){
                System.out.println("Detection progress:"+i+"/"+frames.length+" ("+(int)(((i*1.0f)/frames.length)*100)+"%)");
            }


    }
        System.out.println("Process finished");
    }
}



