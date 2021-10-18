// BV Ue1 WS2020/21 Vorgabe
//
// Copyright (C) 2019-2020 by Klaus Jung
// All rights reserved.
// Date: 2020-10-08

package bv_ws20;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.util.Random;

public class RasterImage {
	
	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels
	
	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}
	
	public RasterImage(RasterImage src) {
		// copy constructor
		this.width = src.width;
		this.height = src.height;
		argb = src.argb.clone();
	}
	
	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}
	
	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}
	
	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}
	
	
	// image point operations to be added here
	
	public void convertToGray() {
		// TODO: convert the image to grayscale
		
		for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;
                
                //Lesen der Werte
                int argbNew = argb[pos];
                
                int r = (argbNew >> 16) & 0xff;
                int g = (argbNew >>  8) & 0xff;
                int b =  argbNew        & 0xff;

                //Berechnung der Korrektur
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                //Luminanzwert
                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                // Skalierung fuer die bits
                int gray = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
               
               argb[pos] = 0xFF000000 |(gray << 16)|(gray << 8) | gray;
            }
        }
	}
	
	/**
	 * @param quantity The fraction of pixels that need to be modified
	 * @param strength The brightness to be added or subtracted from a pixel's gray level
	 */
	public void addNoise(double quantity, int strength) {
		// TODO: add noise with the given quantity and strength
		
//	    1 Slider = prozentualeAnteil der zu verandernden Pixel festgelegt. (Bereich: 0% bis 60%)
//      Verwenden Sie dazu java.util.Random.
		Random random1 = new Random();
		
//		· Mit einer weiteren Zufallszahl bestimmen Sie, ob der Pixel dunkler oder heller werden soll.
		Random random2 = new Random();
			
		for (int i = 0; i < (argb.length * quantity); i++ ) {
			int pix = random1.nextInt(argb.length);
			int pixRGB = argb[pix];
			
//			· Der zweite Slider bestimmt, wieviel Sie der Graustufe des Pixels dazuaddieren bzw. abziehen mu ̈ssen.
			int zahl = random2.nextInt(2);
			if (zahl == 0) zahl = -1;
						
			int r = (pixRGB >> 16) & 0xff;
		    int g = (pixRGB >>  8) & 0xff;
		    int b =  pixRGB & 0xff;
						
		    int color = ((r + g + b) / 3) + (zahl*strength);
		    
		    // zuweisung
		    if (color<0) color = 0;
		    if (color>255) color = 255;

		    // werte zureck
			argb[pix] = 0xFF000000 |(color << 16)|(color << 8) | color;
		
	   }
   }
}	
		

		
		

	
	


