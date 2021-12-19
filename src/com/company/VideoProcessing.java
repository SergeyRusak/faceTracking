package com.company;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoProcessing {
    private FFmpegFrameGrabber grabber;
    private ArrayList<BufferedImage> rendered_video;
    private  double frame_rate;
    private  int grab_frame;

    public VideoProcessing(String filename) throws IOException {
        File output_dir = new File("prerender/");
        output_dir.mkdirs();
        preRender(new File(filename));
    }


    public boolean preRender(File file) throws IOException {
        grabber = new FFmpegFrameGrabber(file);
        grabber.start();
        int null_frames = 0;
        frame_rate = grabber.getFrameRate();
        Frame i;
        int frames = grabber.getLengthInVideoFrames();
        for (int j = 0; j<frames; j++){
            i = grabber.grabFrame(false,true,true,false);

            while(i.image== null){
                null_frames++;
                j++;
                i = grabber.grab();
            }
            BufferedImage bi = new Java2DFrameConverter().convert(i);
            String sj = ""+j;
            while(sj.length() != (""+frames).length()) sj= 0+sj;
            File output = new File("prerender/frame_"+sj+".jpg");
            int w = bi.getWidth();
            int h = bi.getHeight();
            int[] pixels = new int[w*h];
            bi.getRGB(0,0,w,h,pixels,0,w);
            BufferedImage bi2 = new BufferedImage(w,h,BufferedImage.TYPE_3BYTE_BGR);
            bi2.setRGB(0,0,w,h,pixels,0,w);
            ImageIO.write(bi2,"JPG",output);
            if((int)(((j*1.0f)/frames)*1000)%50 ==0){
                System.out.println("Render progress:"+j+"/"+frames+ "("+(int)(((j*1.0f)/frames)*100)+"%)");
            }


        }
        grabber.stop();
        System.out.println("Render finished with "+ null_frames+" frames");
        return  true;
    }

    public String getFramesPath() {
        return "prerender/";
    }

    public double getFrame_rate() {
        return frame_rate;
    }
}