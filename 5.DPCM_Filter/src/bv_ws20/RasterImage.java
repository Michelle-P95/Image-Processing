// BV Ue2 WS20/21 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws20;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
//import java.util.Random;

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
//		
//		for (int y=0; y<height; y++) {
//            for (int x=0; x<width; x++) {
//                int pos = y*width + x;
//                
//                //Lesen der Werte
//                int argbNew = argb[pos];
//                
//                int r = (argbNew >> 16) & 0xff;
//                int g = (argbNew >>  8) & 0xff;
//                int b =  argbNew        & 0xff;
//
//                //Berechnung der Korrektur
//                double rr = Math.pow(r / 255.0, 2.2);
//                double gg = Math.pow(g / 255.0, 2.2);
//                double bb = Math.pow(b / 255.0, 2.2);
//
//                //Luminanzwert
//                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;
//
//                // Skalierung fuer die bits
//                int gray = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
//               
//               argb[pos] = 0xFF000000 |(gray << 16)|(gray << 8) | gray;
//		
//		
		
		for ( int i = 0; i < argb.length; i++ ) {

			int r = (int)(argb[i] >> 16 & 0xff);
			int g = (int)(argb[i] >> 8 & 0xff);
			int b = (int)(argb[i] & 0xff);
			
			int gray = ( r + g + b ) / 3;

			argb[i] =  (0xff << 24) | (gray << 16) | (gray << 8) | (gray);
		
		
            }
        }
	}


