// BV Ue3 WS2019/20 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws20;

public class MorphologicFilter {
	
	public enum FilterType { 
		DILATION("Dilation"),
		EROSION("Erosion");
		
		private final String name;       
	    private FilterType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	// filter implementations go here:
	
	public void copy(RasterImage src, RasterImage dst) {
		// 1 
		// TODO: just copy the image 
		
		 for (int i = 0; i < src.argb.length; i++) {
			 	dst.argb[i] = src.argb[i];
		 }
		
	}
	
	
	public void dilation(RasterImage src, RasterImage dst, double radius) {
		// TODO: dilate the image using a structure element that is a neighborhood with the given radius
		// 3
		for(int y = 0; y < src.height; y++) {
			for(int x = 0; x < src.width; x++) {

				//Position
				int pos = y * dst.width + x;
				
				// white background
				dst.argb[pos] = dst.white;
				
				// black condition
				if((src.argb[pos] & 0xff) != 255) {
		
					for ( int yr = (int)(y - radius); yr <= y + radius; yr++ ){

                       	// outside image y
						if(yr < 0 || yr >= src.height)
                       		continue;

                        for ( int xr = (int)(x - radius); xr <= x + radius; xr++ ) {

						      // outside image x
							   if(xr < 0 || xr >= src.width)
								   continue;

							   // circle within
							   if ((xr - x) * (xr - x) + (yr - y) * (yr - y) <= (radius * radius)){

							    // Position black 
							    int pos2 = yr * src.width + xr;

							    dst.argb[pos2] = src.black;

                                }
					         }
				      }
			    }
		
	        }
		}
	}	
		
	
	public void erosion(RasterImage src, RasterImage dst, double radius) {
		// TODO: erode the image using a structure element that is a neighborhood with the given radius
		// 3
		 src.invert();
	     dilation(src, dst, radius);
	     dst.invert();
	     src.invert();
	}

}
