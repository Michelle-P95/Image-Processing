

package bv_ws20;

import java.util.ArrayList;
import java.util.Collections;

public class MedianFilter implements Filter {

	RasterImage image, destinationImage;
	int kernelWidth, kernelHeight;
	
	@Override
	public void setSourceImage(RasterImage sourceImage) {
		this.image = sourceImage;
	}

	@Override
	public void setDestinationImage(RasterImage destinationImage) {
		this.destinationImage = destinationImage;
	}

	@Override
	public void setKernelWidth(int kernelWidth) {
		this.kernelWidth = kernelWidth;
	}

	@Override
	public void setKernelHeight(int kernelHeight) {
		this.kernelHeight = kernelHeight;
	}

	@Override
	public void apply() {
			
	 // attribute	
	  int width = image.width;
      int height = image.height;
      int[] argb = image.argb;
      

      // schleife ueber pixel
      for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
             
        	  //Position pixel 
        	  int pos = y * width + x;
        	  
        	  // pixel kernel sepeichern
        	  ArrayList<Integer> pixKernel = new ArrayList<>(kernelHeight*kernelWidth);
        	  
        	  		// ueber jedes pixel gehen
        			  for (int i = 0; i < kernelHeight; i++) {
        				  for (int j = 0; j < kernelWidth; j++) {
        					  
        					  //  pixel um den mittelpixel des kernels
        					  int xPixel = i - kernelWidth / 2;
        					  int yPixel = i - kernelHeight / 2;
        					 
        					  // ausserhalb image
        					  if (x + xPixel < 0 || x + xPixel >= width || y + yPixel < 0 || y + yPixel >= height) {
        						  continue;
        					  }
        					  
        					  // pixel in kernel
        					  // position in kernel array
        					  int pixel = (y + yPixel) * width + (x + xPixel);
        					  pixKernel.add(argb[pixel]&0xff);
        					 
        				  }
        			  }
        	
        	//sort
        	Collections.sort(pixKernel);
        	int medianValue = pixKernel.get(pixKernel.size()/2);
        	
        	//werte zurueck
            destinationImage.argb[pos] = (0xFF << 24) | (medianValue << 16) | (medianValue << 8) | medianValue;
        	
            
// -------------------          
//        	  ArrayList<Integer> pixelKernel = null;
//             
//              // kernel
//              int pixel = 0;
//              for (int i = 0; i < kernelHeight; i++) {
//                  for (int j = 0; j < kernelWidth; j++) {
//                	  
//                	  // pixel speichern
//                      pixelKernel = new ArrayList<>(kernelHeight*kernelWidth);
//                	  
//                      final int ypixel = i - kernelHeight / 2;
//                      final int xpixel = j - kernelWidth / 2;
//                      
//                      if (y + ypixel < 0 || y + ypixel >= height || x + xpixel < 0 || x + xpixel >= width) {
//                          continue;
//                      }
//                      
//                      pixel = (y + ypixel) * width + (x + xpixel);
//                  
//                      pixelKernel.add(argb[pixel]);
//                  }
//              }
//              
//              Collections.sort(pixelKernel);
//              int medianValue = pixelKernel.get(pixelKernel.size()/2);
//              
//              // kernel als array auflisten
//              // sortieren
//              // mittleres element nehmen
//              
//              final int size = pixelKernel.size();
//              
////              for (int a = 1; a < size-1; a++) {
////            	  
////                  if (pixelKernel.get(a) > maxValue) {
////                      maxValue = pixelKernel.get(a);
////                  }             
            
         // werte zurueck
//            destinationImage.argb[pos] = (0xFF << 24) | (medianValue << 16) | (medianValue << 8) | medianValue;
//                  
                  
              }
		
		
	}
   }
}
