// BV Ue2 WS20/21 Vorgabe
//
// Copyright (C) 2017 by Klaus Jung
// All rights reserved.
// Date: 2017-07-15

package bv_ws20;


public class GeometricTransform {

	public enum InterpolationType { 
		NEAREST("Nearest Neighbour"), 
		BILINEAR("Bilinear");
		
		private final String name;       
	    private InterpolationType(String s) { name = s; }
	    public String toString() { return this.name; }
	};
	
	public void perspective(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion, InterpolationType interpolation) {
		switch(interpolation) {
		case NEAREST:
			perspectiveNearestNeighbour(src, dst, angle, perspectiveDistortion);
			break;
		case BILINEAR:
			perspectiveBilinear(src, dst, angle, perspectiveDistortion);
			break;
		default:
			break;	
		}
		
	}

	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	
	// weisser Hintergrund
	private static final int white = 0xFFFFFF;
	
	public void perspectiveNearestNeighbour(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {
		// TODO: implement the geometric transformation using nearest neighbour image rendering
		
		// NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radians
		
		
			for (int yd = 0; yd < dst.height; yd++) {
				for (int xd = 0; xd < dst.width; xd++) {
					double xdd = xd - (dst.width/2);
					double ydd = yd - (dst.height/2);
					
					//Position dst
					int pos = yd * dst.width + xd;
					
					//Transformationen von den Quellkoordinaten zu den Zielkoordinaten
					double xsd = xdd / (Math.cos(Math.toRadians(angle)) - xdd * perspectiveDistortion * Math.sin(Math.toRadians(angle)));
					double ysd = (ydd * ( perspectiveDistortion * Math.sin(Math.toRadians(angle)) * xsd + 1));

					int xss = (int)((src.width / 2) + xsd);
					int yss = (int)((src.height / 2) + ysd);

					//Position src
					int posSrc = yss * src.width + xss;

					//Hintergrund weiss
					if(posSrc < 0 || posSrc >= src.argb.length || xss < 0 || xss >= src.width) {
						dst.argb[pos] = (0xFF << 24) | (white << 16) | (white << 8) | white;
					}
					//neues Bild 
					else {
						dst.argb[pos] = src.argb[posSrc];
					}
				}
			}
			
		

	}


	/**
	 * @param src source image
	 * @param dst destination Image
	 * @param angle rotation angle in degrees
	 * @param perspectiveDistortion amount of the perspective distortion 
	 */
	
	public void perspectiveBilinear(RasterImage src, RasterImage dst, double angle, double perspectiveDistortion) {

		// TODO: implement the geometric transformation using bilinear interpolation
		
	    // NOTE: angle contains the angle in degrees, whereas Math trigonometric functions need the angle in radians
		
        for (int yd = 0; yd < dst.height; yd++) {
			for (int xd = 0; xd < dst.width; xd++) {
				double xdd = xd - (dst.width/2);
				double ydd = yd - (dst.height/2);
				
            	//Position dst
				int pos = yd * dst.width + xd;

                //Transformationen von den Quellkoordinaten zu den Zielkoordinaten
				double xsd = xdd / (Math.cos(Math.toRadians(angle)) - xdd * perspectiveDistortion * Math.sin(Math.toRadians(angle)));
				double ysd = (ydd * ( perspectiveDistortion * Math.sin(Math.toRadians(angle)) * xsd + 1));
                xsd += src.width / 2.0;
                ysd += src.height / 2.0;

                // groesster integer
                final double xsfinal = Math.floor(xsd); //u
                final double ysfinal = Math.floor(ysd); //v

                // Begrenzung
                if (ysfinal < 0 || ysfinal >= src.height || xsfinal < 0 || xsfinal >= src.width) {
                	dst.argb[pos] = (0xFF << 24) | (255 << 16) | (255 << 8) | 255;
                }
                else {
                    double a = (xsd - xsfinal);
                    double b = (ysd - ysfinal);
                    
                    // Pixel postitionen OBEN UNTEN RECHTS LINKS
                    // O L
                    int posA = (int)(ysfinal * src.width + xsfinal);            
                    int posB, posC, posD;

                    // O R
                    if(xsfinal < src.width-1) {
                        posB = (int)(ysfinal * src.width + xsfinal+1);          
                    } else {
                        posB = (int)(ysfinal * src.width + xsfinal);
                    }

                    // U L
                    if(ysfinal < src.height-1) {
                        posC = (int)((ysfinal+1) * src.width + xsfinal);        
                    } else {
                        posC = (int)(ysfinal * src.width + xsfinal);
                    }

                    // U R
                    if(xsfinal < src.width-1 && ysfinal < src.height-1) {
                        posD = (int)((ysfinal+1) * src.width + xsfinal+1);     
                    } else {
                        posD = (int)(ysfinal * src.width + xsfinal);
                    }

                    // RGB Werte
                    int A = src.argb[posA];                                             
                    int B = src.argb[posB];
                    int C = src.argb[posC];
                    int D = src.argb[posD];

                    // RGB trennen um R G B einzeln als G zurueck zu geben
                    // R G B fuer ABCD
                    int Ar = (A >> 16) & 0xff;	int Ag = (A >> 8) & 0xff;	int Ab =  A & 0xff;                                                 
                    int Br = (B >> 16) & 0xff;	int Bg = (B >> 8) & 0xff;	int Bb =  B & 0xff;
                    int Cr = (C >> 16) & 0xff;	int Cg = (C >> 8) & 0xff;	int Cb =  C & 0xff;
                    int Dr = (D >> 16) & 0xff;	int Dg = (D >> 8) & 0xff;	int Db =  D & 0xff;
                    
                    // Rechnung EFG
                    // R
                    double Er =(Ar + a*(Br-Ar));                                    
                    double Fr =(Cr + a*(Dr-Cr));
                    int Gr =(int)(Er + b*(Fr-Er));
                    // G
                    double Eg =(Ag + a*(Bg-Ag));
                    double Fg =(Cg + a*(Dg-Cg));
                    int Gg =(int)(Eg + b*(Fg-Eg));
                    // B
                    double Eb =(Ab + a*(Bb-Ab));
                    double Fb =(Cb + a*(Db-Cb));
                    int Gb =(int)(Eb + b*(Fb-Eb));

                  //neues Bild 
                    dst.argb[pos] = (0xFF<<24) | (Gr<<16) | (Gg<<8) | Gb;

// 1 -------------- VERSUCH
// Wie 1, allerdings ist hierbei die bilineare Interpolationstechnik verwenden
	
//			for (int yd = 0; yd < dst.height; yd++) {
//				for (int xd = 0; xd < dst.width; xd++) {
//					double xdd = xd - (dst.width/2);
//					double ydd = yd - (dst.height/2);
//
//					//Transformationen von den Quellkoordinaten zu den Zielkoordinaten
//					double xsd = xdd / (Math.cos(Math.toRadians(angle)) - xdd * perspectiveDistortion * Math.sin(Math.toRadians(angle)));
//					double ysd = (ydd * ( perspectiveDistortion * Math.sin(Math.toRadians(angle)) * xsd + 1));
//					
//					int xss = (int)((src.width / 2) + xsd);
//					int yss = (int)((src.height / 2) + ysd);
//					
//					//Position dst
//					int pos = yd * dst.width + xd;
//					
//					//Position src
//					int posSrc = (int) (yss * src.width + xss);
//					
//					
//					if(xss == (int)xss && yss == (int)yss) {		
//						//Hintergrund weiss
//						if(yss < 0 || yss >= src.height || xss < 0 || xss >= src.width) {
//							dst.argb[pos] = (0xFF << 24) | (white << 16) | (white << 8) | white;
//							
//						//neues Bild	
//						} else {
//							dst.argb[pos] = src.argb[posSrc];
//						}
//						
//					}else {
//						
//						
//						// groesster integer
//						int u = (int) Math.floor(xss);
//						int v = (int) Math.floor(yss);
//						
//						double a = xss - u;
//						double b = yss - v;
//						
//						int [] posA = {u, v};
//						int [] posB = {u+1, v};
//						int [] posC = {u, v+1};
//						int [] posD = {u+1, v+1};
//								
//						// 4 closest pixels
//						int A = src.argb[posA];
//						int B = src.argb[posB];
//						int C = src.argb[posC];
//						int D = src.argb[posD];
//						
//						// Distanz = interpolations punkt & horiz. raster u0
//						double E = A + a*(B-A);
//						double F = C + a*(D-C);
//						
//						// vertikal distanz
//						double G = E + b*(F-E);
//						
//					}
						// ip.get pix = farbwert des pixels bekommen
//						int A = ip.getPixel(u,v);
//						int B = ip.getPixel(u+1,v);
//						int C = ip.getPixel(u,v+1);
//						int D = ip.getPixel(u+1,v+1);

						// find neighbour position 
//						int[][] positions = getNeigbhourPosition
//
//						// array neigbour
//						int[] neighbouringPixels = new int[4];
//
//						// r g b werte
//						int r = 0;
//					    int g = 0;
//					    int b = 0;
//						
//						int[][] argb = new int[4][3];
//						
//					
//						// werte rueckgabe
//						dst.argb[pos] = (0xFF << 24) | (r << 16) | (g << 8) | b;
					}
				}
			}
	}
	
}
		
			
	 	



