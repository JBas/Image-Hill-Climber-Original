public class DNA {
	int numOfPolygons;
	int numOfVertices;
	int[][] xpoints;
	int[][] ypoints;
	int[] r, g, b;
	int[] a;
	
	public void mutate_soft() {
		double rand = Math.random();
		int randomPoly = (int)(Math.random()*(this.numOfPolygons));
		
		double delta = 10*Math.signum(10*(Math.random() - 0.5)); //5*Math.signum(10*(Math.random() - 0.5));
		
		if (rand < 0.5) {
			if (rand < 0.125) {
				int newR = (int)(this.r[randomPoly] + delta);
				if (newR > 255) {
					newR = 255;
				} else if (newR < 0) {
					newR = 0;
				}
				this.r[randomPoly] = newR;
			} else if (rand < 0.25) {
				int newG = (int)(this.g[randomPoly] + delta);
				if (newG > 255) {
					newG = 255;
				} else if (newG < 0) {
					newG = 0;
				}
				this.g[randomPoly] = newG;
			} else if (rand < 0.375) {
				int newB = (int)(this.b[randomPoly] + delta);
				if (newB > 255) {
					newB = 255;
				} else if (newB < 0) {
					newB = 0;
				}
				this.b[randomPoly] = newB;
			} else if (rand < 0.5) {
				int newA = (int)(this.a[randomPoly] + delta);
				if (newA > 255) {
					newA = 255;
				} else if (newA < 0) {
					newA = 0;
				}
				this.a[randomPoly] = newA;
			}
		} else {
			int randomPoint = (int)(Math.random()*(this.numOfVertices));
			if (rand < 0.75) {
				int newX = (int)(this.xpoints[randomPoly][randomPoint] + delta);
				if (newX > ImagesGA_FinalProduct.maxImgWidth) {
					newX = ImagesGA_FinalProduct.maxImgWidth;
				} else if (newX < 0) {
					newX = 0;
				}
				this.xpoints[randomPoly][randomPoint] = newX;
			} else {
				int newY = (int)(this.ypoints[randomPoly][randomPoint] + delta);
				if (newY > ImagesGA_FinalProduct.maxImgHeight) {
					newY = ImagesGA_FinalProduct.maxImgHeight;
				} else if (newY < 0) {
					newY = 0;
				}
				this.ypoints[randomPoly][randomPoint] = newY;
			}
		}
	}
	
	public void mutate_medium() {
		double rand = Math.random();
		int randomPoly = (int)(Math.random()*(this.numOfPolygons));
		
		if (rand < 0.5) {
			int randomRGBA = (int)(Math.random()*255);
			if (rand < 0.125) {
				this.r[randomPoly] = randomRGBA;
			} else if (rand < 0.25) {
				this.g[randomPoly] = randomRGBA;
			} else if (rand < 0.375) {
				this.b[randomPoly] = randomRGBA;
			} else if (rand < 0.5) {
				this.a[randomPoly] = randomRGBA;
			}
		} else {
			int randomXY = (int)(Math.random()*Math.min(ImagesGA_FinalProduct.maxImgWidth, ImagesGA_FinalProduct.maxImgHeight));
			int randomPoint = (int)(Math.random()*this.numOfVertices);
			if (rand < 0.75) {
				this.xpoints[randomPoly][randomPoint] = randomXY;
			} else {
				this.ypoints[randomPoly][randomPoint] = randomXY;
			}
		}
	}
	
	public void mutate_hard() {
		int randomPoly = (int)(Math.random()*(this.numOfPolygons));
		int randomPoint = (int)(Math.random()*this.numOfVertices);
		
		this.r[randomPoly] = (int)(Math.random()*255);
		this.g[randomPoly] = (int)(Math.random()*255);
		this.b[randomPoly] = (int)(Math.random()*255);
		this.a[randomPoly] = (int)(Math.random()*255);
		
		this.xpoints[randomPoly][randomPoint] = (int)(Math.random()*ImagesGA_FinalProduct.maxImgWidth);
		this.ypoints[randomPoly][randomPoint] = (int)(Math.random()*ImagesGA_FinalProduct.maxImgHeight);
	}
	
	public void mutate_Laplace() {
		double rand = Math.random();
		int randomPoly = (int)(Math.random()*(this.numOfPolygons));
		
		if (rand < 0.5) {
			if (rand < 0.125) {
				int lapLaceRGBA = (int)this.laplace(this.r[randomPoly], 4);
				
				if (lapLaceRGBA < 0) {
					lapLaceRGBA = 0;
				} else if (lapLaceRGBA > 255) {
					lapLaceRGBA = 255;
				}
				
				this.r[randomPoly] = lapLaceRGBA;
			} else if (rand < 0.25) {
				int lapLaceRGBA = (int)this.laplace(this.g[randomPoly], 4);
				
				if (lapLaceRGBA < 0) {
					lapLaceRGBA = 0;
				} else if (lapLaceRGBA > 255) {
					lapLaceRGBA = 255;
				}
				
				this.g[randomPoly] = lapLaceRGBA;
			} else if (rand < 0.375) {
				int lapLaceRGBA = (int)this.laplace(this.b[randomPoly], 4);
				
				if (lapLaceRGBA < 0) {
					lapLaceRGBA = 0;
				} else if (lapLaceRGBA > 255) {
					lapLaceRGBA = 255;
				}
				
				this.b[randomPoly] = lapLaceRGBA;
			} else if (rand < 0.5) {
				int lapLaceRGBA = (int)this.laplace(this.a[randomPoly], 4);
				
				if (lapLaceRGBA < 0) {
					lapLaceRGBA = 0;
				} else if (lapLaceRGBA > 255) {
					lapLaceRGBA = 255;
				}
				
				this.a[randomPoly] = lapLaceRGBA;
			}
		} else {
			int randomPoint = (int)(Math.random()*this.numOfVertices);
			if (rand < 0.75) {
				int lapLaceXY = (int)this.laplace(this.xpoints[randomPoly][randomPoint], 4);
				
				if (lapLaceXY < 0) {
					lapLaceXY = 0;
				} else if (lapLaceXY > ImagesGA_FinalProduct.maxImgWidth) {
					lapLaceXY = ImagesGA_FinalProduct.maxImgWidth;
				}
				
				this.xpoints[randomPoly][randomPoint] = lapLaceXY;
			} else {
				int lapLaceXY = (int)this.laplace(this.ypoints[randomPoly][randomPoint], 4);
				
				if (lapLaceXY < 0) {
					lapLaceXY = 0;
				} else if (lapLaceXY > ImagesGA_FinalProduct.maxImgHeight) {
					lapLaceXY = ImagesGA_FinalProduct.maxImgHeight;
				}
				
				this.ypoints[randomPoly][randomPoint] = lapLaceXY;
			}
		}
	}
	
	private double laplace(int mu, double b) {
		double randomX = (Math.random()*(1000) - 500);
		double product;
		if (randomX < mu) {
			double power = -(mu - randomX)/b;
			product = (1/(2*b)) * Math.pow(Math.E, power);
		} else {
			double power = -(randomX - mu)/b;
			product = (1/(2*b)) * Math.pow(Math.E, power);
		}
		return product;
	}
	
	public DNA(int popCount, int vertCount) {
		this.numOfPolygons = popCount;
		this.numOfVertices = vertCount;
		
		this.xpoints = new int[this.numOfPolygons][this.numOfVertices];
		this.ypoints = new int[this.numOfPolygons][this.numOfVertices];
		this.r = new int[this.numOfPolygons];
		this.g = new int[this.numOfPolygons];
		this.b = new int[this.numOfPolygons];
		this.a = new int[this.numOfPolygons];
		
		for (int i = 0; i < this.numOfPolygons; i++) {
			int[] xp = new int[this.numOfVertices];
			int[] yp = new int[this.numOfVertices];
			for (int j = 0; j < this.numOfVertices; j++) {
				int x = (int)((Math.random()*(ImagesGA_FinalProduct.maxImgWidth) + 1));
				int y = (int)((Math.random()*(ImagesGA_FinalProduct.maxImgHeight) + 1));
				xp[j] = x;
				yp[j] = y;
			}
			
			this.xpoints[i] = xp;
			this.ypoints[i] = yp;
			this.r[i] = 1; //(int)Math.random()*255;
			this.g[i] = 2; //(int)Math.random()*255;
			this.b[i] = 3; //(int)Math.random()*255; 
			this.a[i] = 4; //(int)Math.random()*255;
		}
	}
	
	public static DNA clone(DNA cloneFrom) {
		DNA clone = new DNA(cloneFrom.numOfPolygons, cloneFrom.numOfVertices);
		for (int i = 0; i < cloneFrom.numOfPolygons; i++) {
			for (int j = 0; j < cloneFrom.numOfVertices; j++) {
				clone.xpoints[i][j] = cloneFrom.xpoints[i][j];
				clone.ypoints[i][j] = cloneFrom.ypoints[i][j];
			}
			clone.r[i] = cloneFrom.r[i];
			clone.g[i] = cloneFrom.g[i];
			clone.b[i] = cloneFrom.b[i];
			clone.a[i] = cloneFrom.a[i];
		}
		return clone;
	}
	
	public String save() {
		String dnaString = "{ ";
		
		dnaString += "poly: " + Integer.toString(this.numOfPolygons) + "; ";
		dnaString += "vert: " + Integer.toString(this.numOfVertices) + "; ";
		dnaString += "bestCount: " + Integer.toString(Population.bestCount) + ";";
		
		for (int i = 0; i < this.numOfPolygons; i++) {
			dnaString += " xpoints:";
			for (int j = 0; j < this.numOfVertices; j++) {
				dnaString +=  " " + Integer.toString(this.xpoints[i][j]);
			}
			dnaString += ";";
			
			dnaString += " ypoints:";
			for (int j = 0; j < this.numOfVertices; j++) {
				dnaString +=  " " + Integer.toString(this.ypoints[i][j]);
			}
			dnaString += ";";
			
			dnaString += " r: " + Integer.toString(this.r[i]) + ";";
			dnaString += " g: " + Integer.toString(this.g[i]) + ";";
			dnaString += " b: " + Integer.toString(this.b[i]) + ";";
			dnaString += " a: " + Integer.toString(this.a[i]) + ";";
		}
		
		dnaString += "}";
		
		System.out.println(dnaString);
		
		return dnaString;
	}
	
	public static DNA importDNA(String dnaString) {
		int numPoly = -1;
		int numVert = -1;
		int bestCount = -1;
		int popCount = -1;
		int[][] xpoints = new int[1][3];
		int[][] ypoints = new int[1][3];
		int[] r = new int[1], g = new int[1], b = new int[1], a = new int[1];
		
		int xCount = 0, yCount = 0, xVertCount = 0, yVertCount = 0;
		int rCount = 0, gCount = 0, bCount = 0, aCount = 0;
		
		for (int i = 0; i < dnaString.length(); i++) {
			if (numPoly == -1) {
				if (dnaString.substring(i, i+5).equals("poly:")) {
//					System.out.println(dnaString.substring(i, i+5));
					
					int k = i + 5; // whitespace index
					
					// find semicolon index
					while (!dnaString.substring(k, k + 1).equals(";")) {
						k += 1;
					}
					numPoly = Integer.parseInt(dnaString.substring(i + 6, k));
				}
			} else if (numVert == -1){
				if (dnaString.substring(i, i+5).equals("vert:")) {					
					int k = i + 5; // whitespace index
					
					// find semicolon index
					while (!dnaString.substring(k, k + 1).equals(";")) {
						k += 1;
					}
					numVert = Integer.parseInt(dnaString.substring(i + 6, k));
					
					xpoints = new int[numPoly][numVert];
					ypoints = new int[numPoly][numVert];
					r = new int[numPoly];
					g = new int[numPoly];
					b = new int[numPoly];
					a = new int[numPoly];
					
				}
			} else if (bestCount == -1) {
				if (dnaString.substring(i, i+10).equals("bestCount:")) {
					
					int k = i + 10;
					while (!dnaString.substring(k, k + 1).equals(";")) {
						k += 1;
					}
					bestCount = Integer.parseInt(dnaString.substring(i + 11, k));
					System.out.println(bestCount);
				}
			} else {
				// takes care of xpoints
				if ((i + 8 < dnaString.length()) && (dnaString.substring(i, i+8).equals("xpoints:"))) {
//					System.out.println(dnaString.substring(i, i+8));
					int[] xp = new int[numVert];
					
					int k = i + 9; // whitespace index
					int off = i + 9;
					
					while ( xVertCount < numVert) {
						while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
							k += 1;
						}
						int x = Integer.parseInt(dnaString.substring(off, k + 1));
//						System.out.println(x);
						xp[xVertCount] = x;
						xVertCount++;
						off = k+2;
						k = off;
					}
					xpoints[xCount] = xp;
					xCount++;
					xVertCount = 0;
				} else if ((i + 8 < dnaString.length()) && (dnaString.substring(i, i+8).equals("ypoints:"))) {
//					System.out.println(dnaString.substring(i, i+8));
					int[] yp = new int[numVert];
					
					int k = i + 9; // whitespace index
					int off = i + 9;
					
					while (yVertCount < numVert) {
						while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
							k += 1;
						}
						int y = Integer.parseInt(dnaString.substring(off, k + 1));
//						System.out.println(y);
						yp[yVertCount] = y;
						yVertCount++;
						off = k+2;
						k = off;
					}
					ypoints[yCount] = yp;
					yCount++;
					yVertCount = 0;
				} else if ((i + 2 < dnaString.length()) && (dnaString.substring(i, i+2).equals("r:"))) {
					int k = i + 3; // white space index
					int off = i + 3;
					
					while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
						k += 1;
					}
					int rLocal = Integer.parseInt(dnaString.substring(off, k + 1));
//					System.out.println("r: " + rLocal);
					r[rCount] = rLocal;
					rCount++;
					
				} else if ((i + 2 < dnaString.length()) && (dnaString.substring(i, i+2).equals("g:"))) {
					int k = i + 3; // white space index
					int off = i + 3;
					
					while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
						k += 1;
					}
					int gLocal = Integer.parseInt(dnaString.substring(off, k + 1));
//					System.out.println("g: " + gLocal);
					g[gCount] = gLocal;
					gCount++;
					
				} else if ((i + 2 < dnaString.length()) && (dnaString.substring(i, i+2).equals("b:"))) {
					int k = i + 3; // white space index
					int off = i + 3;
					
					while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
						k += 1;
					}
					int bLocal = Integer.parseInt(dnaString.substring(off, k + 1));
//					System.out.println("b: " + bLocal);
					b[bCount] = bLocal;
					bCount++;
				
				} else if ((i + 2 < dnaString.length()) && (dnaString.substring(i, i+2).equals("a:"))) {
					int k = i + 3; // white space index
					int off = i + 3;
					
					while ((!dnaString.substring(k + 1, k + 2).equals(" ")) && (!dnaString.substring(k + 1, k + 2).equals(";"))) {
						k += 1;
					}
					int aLocal = Integer.parseInt(dnaString.substring(off, k + 1));
//					System.out.println("a: " + aLocal);
					a[aCount] = aLocal;
					aCount++;
					
				}
			}
		}
		
		
		DNA newDNA = new DNA(numPoly, numVert);
		newDNA.xpoints = xpoints;
		newDNA.ypoints = ypoints;
		newDNA.r = r;
		newDNA.g = g;
		newDNA.b = b;
		newDNA.a = a;
		
		Population.bestCount = bestCount;
		
		return newDNA;
	}
}
