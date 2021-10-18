// BV Ue4 SS2020 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-16


package bv_ws20;

import bv_ws20.ImageAnalysisAppController.StatsProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Arrays;

public class Histogram {

	private static final int grayLevels = 256;
	
    private GraphicsContext gc;
    private int maxHeight;
    private int pixCounter;
	double level;
	int min;
	int max;
	int sum;
	double mean;
	int medSum;
	int median;
	double variance;
	double entropy;

	public int[] getHistogram() {
		return histogram;
	}

	public int getPixelCount() {
		return pixCounter;
	}

	private int[] histogram = new int[grayLevels];

	public Histogram(GraphicsContext gc, int maxHeight) {
		this.gc = gc;
		this.maxHeight = maxHeight;
	}
	
	public void update(RasterImage image, Point2D ellipseCenter, Dimension2D ellipseSize, int selectionMax, ObservableList<StatsProperty> statsData) {
		// TODO: calculate histogram[] out of the gray values of the image for pixels inside the given ellipse
//		
//		histogram[0] = 0; // remove this line when implementing the proper calculations
//				
//		// Remark: Please ignore selectionMax and statsData in Exercise 4. It will be used in Exercise 5.
		
		for(int i = 0; i < grayLevels; i++) {
			histogram[i] = 0;
		}
		pixCounter = 0;
		
		// Ellipse
		double ellipseCenterX = ellipseCenter.getX();
		double ellipseCenterY = ellipseCenter.getY();
		double ellipseRadX = ellipseSize.getWidth() / 2;
		double ellipseRadY = ellipseSize.getHeight() / 2;

		for(int x = 0; x < image.width;x++) {
			for(int y = 0; y < image.height;y++) {
				
				// position
				int pos = y * image.width + x;
								
				if(insideEllipse(x, y, ellipseCenterX, ellipseCenterY, ellipseRadX, ellipseRadY)) {
					
					int color = (int) (image.argb[pos] & 0xff);
					
					histogram[color]++;
					pixCounter++;
				}
			}
		}
		
		// alles 0 Setzen & nacheinander berechnen
		level = 0;
		min = 0;
		max = 0;
		sum = 0;
		mean = 0;
		medSum = 0;
		median = 0;
		variance = 0;
		entropy = 0;

		for(int i = 0; i < histogram.length; i++) {

			// sum
			if(i <= selectionMax) {
				sum = sum + histogram[i];
			}

			// mean
			mean = mean + (double) ( histogram[i] * i );

			// medSum
			medSum = medSum + histogram[i];
			
			// median
			if( ((medSum-histogram[i]) * 100.00) / image.argb.length < 50.00 && ((medSum * 100.00) / image.argb.length) >= 50.00) {
				median = i;
			}

			// entropy
			if (histogram[i] != 0) {
				double p = pixCounter/histogram[i];
				entropy = entropy + -1 * ((1 / p) * (((-1) * Math.log(p)) / Math.log(2)));
			}
		}

		for (StatsProperty choice : statsData) {
			
			switch (choice) {
				case Level:
					level = (sum * 100.00) / pixCounter / 100.00;
					choice.setValueInPercent(level);
					break;
					
				case Minimum:
					for (int i = 0; i < histogram.length; i++) {
						if(histogram[i] != 0){
							min = i;
							choice.setValue(min);
						}
					}
					break;
					
				case Maximum:
					for(int i = histogram.length - 1; i >= 0; i--) {
						if(histogram[i] != 0) {
							max = i;
							choice.setValue(max);
						}
					}
					break;
		
				case Mean:
					mean = mean/pixCounter;
					choice.setValue(mean);
					break;
					
				case Median:
					choice.setValue(median);
					break;
					
				case Variance:
					for (int i = 0; i < histogram.length; i++ ) {
						variance = variance + ((i - mean) * (i - mean) ) * histogram[i]/pixCounter;
					}
					choice.setValue(variance);
					break;
					
				case Entropy:
					choice.setValue(entropy);
					break;
	       }
		}
	}	
	    
	public void draw() {
		gc.clearRect(0, 0, grayLevels, maxHeight);
		gc.setLineWidth(1);

		// TODO: draw histogram[] into the gc graphic context

		// Remark: This is some dummy code to give you an idea for graphics drawing
		double shift = 0.5;
		int highestPixel = 200;
		
		// note that we need to add 0.5 to all coordinates to get a one pixel thin line
		
		gc.setStroke(Color.BLACK);
		int maxValue = Arrays.stream(histogram).max().getAsInt();
		
		for( int i = 0; i < grayLevels; i++ ) {
			if (maxValue != 0)
				gc.strokeLine(i + shift, highestPixel - (int) (histogram[i] * highestPixel / maxValue), i + shift, 200 + shift);
		}
	}
	
	public boolean insideEllipse(int x, int y, double centerX, double centerY, double rx, double ry) {
		
		double formula = ((x - centerX) * (x - centerX) / (rx * rx)) + ((y - centerY) * (y - centerY)/ (ry * ry));

		if(formula <= 1) {return true;}
		return false;
	}
    
}