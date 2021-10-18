package bv_ws20;

//import java.awt.image.Raster;

public class DPCM {

    public enum FilterType {
        A ("A (horizontal)"),
        B ("B (vertical)"),
        C ("C (diagonal)"),
        ABC ("A+B-C"),
        adaptiv ("adaptiv");

        private final String name;
        FilterType(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }
    }
    
    private float MSE = 0;
    
    public float getMSE() {
        return MSE;
    }
    
    public void horizontal (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        
    	MSE = 0;
    	
    	for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int pos = y * originalImage.width + x;
               
                int posA = pos - 1;
                int colorA = 128;
               
                if ( x > 0 ) {  
                colorA = originalImage.argb[posA] & 0xff;     
                }
                
                int colorX = originalImage.argb[pos] & 0xff;
                int e = colorX - colorA;
              
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                
                calculateImage(pos, predictionImage, e);                    
               
                
                posA = pos - 1;
                colorA = 128;
                
                if ( x > 0 ) {  
                    colorA = originalImage.argb[posA] & 0xff; 
                }
               
                colorX = colorA + e;  
                 
                reconstructedImage.argb[pos] =  (0xff << 24) | (colorX << 16) | (colorX << 8) | colorX;
                
//              MSE = MSE + (e * e);
//              MSE = MSE + ((reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff) * (reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff));
            }
        }
//      MSE = MSE/originalImage.argb.length;
   }
 

    
    public void vertical (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        
    	MSE = 0;
    	
    	for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int pos = y * originalImage.width + x;
                
                int posB = pos - originalImage.width;
                int colorB = 128;
            
                if( (pos >= 0 && posB >= 0) && (pos < originalImage.argb.length && posB < originalImage.argb.length) ) {
                  colorB = originalImage.argb[posB] & 0xff;
            
                  int colorX = originalImage.argb[pos] & 0xff;
                  int e = colorX - colorB;
            
                  e = (int) (Math.round(e / quantizationValue) * quantizationValue);
           
                  calculateImage(pos, predictionImage, e);
                }
            
                posB = pos - originalImage.width;
                colorB = 128;
            
                int colorX = originalImage.argb[pos] & 0xff;
                int e = colorX - colorB;
            
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
             
                calculateImage(pos, reconstructedImage, e);
//              MSE = MSE + (e * e);
//              MSE = MSE + ((reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff) * (reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff));
            }
        }
//      MSE = MSE/originalImage.argb.length;
    }

    public void diagonal (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
        
    	MSE = 0;
    	
    	for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int pos = y * originalImage.width + x;
                
                int posC = pos - originalImage.width - 1;
                int colorC = 128;
                
                if((pos >= 0 && posC >= 0) && (pos < originalImage.argb.length && posC < originalImage.argb.length)) {
                    colorC = originalImage.argb[posC] & 0xff;
                
                int colorX = originalImage.argb[pos] & 0xff;
                int e = colorX - colorC;
                
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                
                calculateImage(pos, predictionImage, e);
                }
                
                posC = pos - originalImage.width - 1;
                colorC = 128;
                
                int colorX = originalImage.argb[pos] & 0xff;
                int e = colorX - colorC;
                
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                
                calculateImage(pos, reconstructedImage, e);
                
//              MSE = MSE + (e * e);
//              MSE = MSE + ((reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff) * (reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff));
            }
        }
//      MSE = MSE/originalImage.argb.length;
    }
                                

    public void abc (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
       
    	MSE = 0;
    	
    	for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int pos = y * originalImage.width + x;
                
//                int posA = pos -1;
//                int colorA = 128;
//                if((pos >= 0 && posA >= 0)&&(pos < originalImage.argb.length &&
//                		posA < originalImage.argb.length )) {
//                    colorA = originalImage.argb[posA] &0xff;
//                }
//                int posB = pos -originalImage.width;
//                int colorB = 128;
//                if((pos >= 0 && posB >= 0) && (pos < originalImage.argb.length&& 
//                	   posB < originalImage.argb.length)){
//                    colorB = originalImage.argb[posB] & 0xff;
//                }
//                int posC = pos- originalImage.width -1;
//                int colorC = 128;
//                if((pos >= 0 &&posC >= 0)&&(pos < originalImage.argb.length &&
//                		posC < originalImage.argb.length) ){
//                    colorC = originalImage.argb[posC] & 0xff ;
//                }
                
                int posA = pos - 1;
                int posB = pos - originalImage.width;
                int posC = pos - originalImage.width - 1;
                
                int colorA = 128;
                int colorB = 128;
                int colorC = 128;
           
                if((pos >= 0 && posA >= 0) && 
                		(pos < originalImage.argb.length && posA < originalImage.argb.length)) {
                    colorA = originalImage.argb[posA] & 0xff;
                }
                if((pos >= 0 && posB >= 0) &&
                		(pos < originalImage.argb.length && posB < originalImage.argb.length)) {
                    colorB = originalImage.argb[posB] & 0xff;
                }
                if((pos >= 0 && posC >= 0) &&
                		(pos < originalImage.argb.length && posC < originalImage.argb.length)) {
                    colorC = originalImage.argb[posC] & 0xff;
                }
      
                int colorX = originalImage.argb[pos] & 0xff;
                
                int e = colorX - (colorA + colorB - colorC);
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                
                
                calculateImage(pos, predictionImage, e);
                
                posA = pos - 1;
                posB = pos - originalImage.width;
                posC = pos - originalImage.width - 1;
                
                colorA = 128;
                colorB = 128;
                colorC = 128;

                colorX = originalImage.argb[pos] & 0xff;
                
                e = colorX - (colorA + colorB - colorC);
                e = (int) (Math.round(e / quantizationValue) * quantizationValue);
                
                calculateImage(pos, reconstructedImage, e);
              
//              MSE = MSE + (e * e);
//              MSE = MSE + ((reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff) * (reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff));
            }
        }
//      MSE = MSE/originalImage.argb.length;
    }
      
 

    public void adaptiv (RasterImage originalImage, RasterImage predictionImage, float quantizationValue, RasterImage reconstructedImage) {
       
    	MSE = 0;
    	
    	for (int x = 0; x < originalImage.width; x++) {
            for(int y = 0; y < originalImage.height; y++) {
                int pos = y * originalImage.width + x;
                
                int posA = pos - 1;
                int posB = pos - originalImage.width;
                int posC = pos - originalImage.width - 1;
                
                int colorA = 128;
                int colorB = 128;
                int colorC = 128;
           
                if((pos >= 0 && posA >= 0) && 
                		(pos < originalImage.argb.length && posA < originalImage.argb.length)) {
                    colorA = originalImage.argb[posA] & 0xff;
                }
                if((pos >= 0 && posB >= 0) &&
                		(pos < originalImage.argb.length && posB < originalImage.argb.length)) {
                    colorB = originalImage.argb[posB] & 0xff;
                }
                if((pos >= 0 && posC >= 0) &&
                		(pos < originalImage.argb.length && posC < originalImage.argb.length)) {
                    colorC = originalImage.argb[posC] & 0xff;
                }
                
                int colorX = originalImage.argb[pos] & 0xff;
                int e;
                
                if(Math.abs(colorA - colorC) < Math.abs(colorB - colorC)) {
                    e = colorX - colorB; 
                }
                else {
                    e = colorX - colorA;
                }
                
                e = (int) (Math.round(e / quantizationValue));
                e = (int) Math.round(e * quantizationValue);
                
                calculateImage(pos, predictionImage, e);
                
                posA = pos - 1;
                posB = pos - originalImage.width;
                posC = pos - originalImage.width - 1;
                
                colorA = 128;
                colorB = 128;
                colorC = 128;
                
                colorX = originalImage.argb[pos] & 0xff;
                
                if(Math.abs(colorA - colorC) < Math.abs(colorB - colorC)) {
                    e = colorX - colorB; 
                }
                else {
                    e = colorX - colorA;
                }
                
                e = (int) (Math.round(e / quantizationValue));
                e = (int) Math.round(e * quantizationValue);
                
                calculateImage(pos, reconstructedImage, e);
                
//              MSE = MSE + (e * e);
//              MSE = MSE + ((reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff) * (reconstructedImage.argb[pos] & 0xff - originalImage.argb[pos] & 0xff));
            }
        }
//      MSE = MSE/originalImage.argb.length;
    }
    
//    private int Ueberlauf(int color) {
//        if(color >= 255) {
//            color = 255;
//        }
//        if(color <= 0) {
//            color = 0;
//        }
//        return color;
//    }
    
    
    private int calculateOverflow (int rgb) {
    	 if(rgb >= 255) {
           rgb = 255;
       }
       if(rgb <= 0) {
           rgb = 0;
       }
       return rgb;
    }
    
    
    public void calculateImage(int i, RasterImage predictionImage, int e) {
        int color = calculateOverflow(e + 128);
        
        predictionImage.argb[i] = 0xff << 24 | color << 16 | color << 8 | color;
    } 

    public double entropy (RasterImage image) {
        double entropy = 0;
        
        int[] histogram = new int[256];
        
        for(int i = 0; i < image.argb.length; i++) {
            histogram[image.argb[i] & 0xff]++;
        }

        for(int i = 0; i < histogram.length; i++) {
            if (histogram[i] != 0) {
                double pj = image.argb.length/histogram[i];
                entropy = entropy + -1 * ((1 / pj) * (((-1) * Math.log(pj)) / Math.log(2)));
            }
        }
        return entropy;
    }
}
