package com.company;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.io.IOException;

public class FaceDetect {

    CascadeClassifier faceCascade,bodyCascade;
    MatOfRect faces,bodies, lastSuccesFace, lastSuccesBody;
    public FaceDetect() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceCascade = new CascadeClassifier();
        bodyCascade = new CascadeClassifier();
        faces = new MatOfRect();
        bodies = new MatOfRect();
        new File("prerender/edit/").mkdirs();
        faceCascade.load("src/haarcascade_frontalface_alt2.xml");
        bodyCascade.load("src/haarcascade_fullbody.xml");
    }

    public void draw(File file){

        Mat image = Imgcodecs.imread(file.getPath());
        for (Rect i:lastSuccesFace.toArray()
        ) {
            Imgproc.rectangle(image,i,new Scalar(0,0,255),3);
        }
        for (Rect i:bodies.toArray()
        ) {
            Imgproc.rectangle(image,i,new Scalar(0,255,0),5);
        }

        Imgcodecs.imwrite("prerender/edit/"+file.getName(),image);

    }


    public Rect[] faceDetect(File file) throws IOException {
        try{
        Mat image = Imgcodecs.imread(file.getPath());
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(image, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        int height = grayFrame.height();
        int absoluteFaceSize = 0;
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.2f);
        }
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        bodyCascade.detectMultiScale(grayFrame, bodies, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
           Rect[] faceArray = faces.toArray();
            for (Rect i:faceArray
                 ) {
                Imgproc.rectangle(image,i,new Scalar(0,0,255),3);
            }
            for (Rect i:bodies.toArray()
            ) {
                Imgproc.rectangle(image,i,new Scalar(0,255,0),5);
            }


            lastSuccesFace = faces;
            lastSuccesBody = bodies;
        Imgcodecs.imwrite("prerender/edit/"+file.getName(),image);
        return faces.toArray();
    }catch (CvException e){
            return null;
        }
    }


}
