

package bv_ws20;

import java.util.ArrayList;

public class MinFilter implements Filter {

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
             
        	  int pos = y * width + x;
        	  
              // pixel speichern
              ArrayList<Integer> pixelKernel = new ArrayList<>(kernelHeight*kernelWidth);
             
              // kernel
              int pixel = 0;
              for (int i = 0; i < kernelHeight; i++) {
                  for (int j = 0; j < kernelWidth; j++) {
                	  
                      final int ypixel = i - kernelHeight / 2;
                      final int xpixel = j - kernelWidth / 2;
                      
                      if (y + ypixel < 0 || y + ypixel >= height || x + xpixel < 0 || x + xpixel >= width) {
                          continue;
                      }
                      
                      pixel = (y + ypixel) * width + (x + xpixel);
                      pixelKernel.add(argb[pixel]);
                  }
              }
              
              
              int minValue = pixelKernel.get(0);
              
              final int size = pixelKernel.size();
              
              for (int a = 1; a < size-1; a++) {
            	  
                  if (pixelKernel.get(a) < minValue) {
                      minValue = pixelKernel.get(a);
                  }
                  
                  // werte zurueck
                  destinationImage.argb[pos] = (0xFF << 24) | (minValue << 16) | (minValue << 8) | minValue;
              }
		
		
	}
		}
	}
}