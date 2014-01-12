package com.nokia.ticToe;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.m2g.SVGImage;
import javax.microedition.m2g.ScalableImage;

import com.sun.lwuit.Image;

public class ImageLoader {

    private static ImageLoader self;
    private ImageLoader() {

    }
    public static ImageLoader getInstance() {
        if(self == null) {
            self = new ImageLoader();
        }
        return self;
    }

    public Image loadImage(String imagepath) throws IOException{
        InputStream in = this.getClass().getResourceAsStream(imagepath);
        Image image = Image.createImage(in);
        return image;
    }
    public SVGImage loadSVGImage(String imagepath) throws IOException {
        InputStream in = this.getClass().getResourceAsStream(imagepath);
        SVGImage svg_image = (SVGImage) ScalableImage.createImage(in, null);
        return svg_image;
    }

}
