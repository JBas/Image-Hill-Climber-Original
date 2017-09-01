import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Population {
	private DNA currentDNA;
	private BufferedImage currentImg;
	private double currentScore = -1;
	
	private DNA newDNA;
	private BufferedImage newImg;
	private int[][] newImgPixels;
	private double newScore;
	private int newW;
	private int newH;
	
	public static int bestCount = 0;
	public static int count = 0;
	
	public Population(int popCount, int vertCount) {
		this.currentDNA = new DNA(popCount, vertCount);
	}
	
	public void run() {
		if (ImagesGA_FinalProduct.save) {
			this.saveCurrentDNA();
		} else if (ImagesGA_FinalProduct.importDNA) {
			this.importDNA();
		}
		
		this.copyAndMutate();
		this.implementDNA();
		this.fitness();
		this.compare();
	}
	
	public void copyAndMutate() {
		this.newDNA = DNA.clone(currentDNA);
		
		if (ImagesGA_FinalProduct.mrS) {
			this.newDNA.mutate_soft();
		} else if (ImagesGA_FinalProduct.mrM) {
			this.newDNA.mutate_medium();
		} else if (ImagesGA_FinalProduct.mrH) {
			this.newDNA.mutate_hard();
		} else if (ImagesGA_FinalProduct.mrL) {
			this.newDNA.mutate_Laplace();
		}
	}
	
	public void implementDNA() {
		BufferedImage context = new BufferedImage(ImagesGA_FinalProduct.maxImgWidth, ImagesGA_FinalProduct.maxImgHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gc = context.createGraphics();
		for (int i = 0; i < this.newDNA.numOfPolygons; i++) {
			Polygon poly = new Polygon(this.newDNA.xpoints[i], this.newDNA.ypoints[i], this.newDNA.numOfVertices);
			int r = this.newDNA.r[i];
			int g = this.newDNA.g[i];
			int b = this.newDNA.b[i];
			int a = this.newDNA.a[i];
			gc.setColor(new Color(r, g, b, a));
			gc.fillPolygon(poly);
		}
		this.newImg = context;
		this.newImgPixels = ImagesGA_FinalProduct.getPixels(context);
		this.newW = context.getWidth();
		this.newH = context.getHeight();
		gc.dispose();
	}
	
	public void fitness() {
		// pixel by pixel
		double sum = 0;
		for (int y = 0; y < this.newH; y++) {
			for (int x = 0; x < this.newW; x++) {
				int rgbaTarget = ImagesGA_FinalProduct.targetPixels[x][y];
				int aT  = (rgbaTarget >> 24) & 0xFF;
				int rT = (rgbaTarget >> 16) & 0xFF;
				int gT = (rgbaTarget >> 8) & 0xFF;
				int bT = (rgbaTarget) & 0xFF;
				
				int rgbaImage = this.newImgPixels[x][y];
				int aI  = (rgbaImage >> 24) & 0xFF;
				int rI = (rgbaImage >> 16) & 0xFF;
				int gI = (rgbaImage >> 8) & 0xFF;
				int bI = (rgbaImage) & 0xFF;
				
				double diffR = (rT - rI);
				double diffG = (gT - gI);
				double diffB = (bT - bI);
				double diffA = (aT - aI);
				double smallerSum = (Math.abs(diffR) + Math.abs(diffG) + Math.abs(diffB) + Math.abs(diffA));
				sum += smallerSum;

			}
		}
		this.newScore = 100 * (1 - (sum/(this.newW*this.newH*3*255)));
	}
	
	public void compare() {
		if (this.newScore > this.currentScore) {
			this.currentDNA = DNA.clone(newDNA);
			this.currentImg = this.newImg;
			this.currentScore = this.newScore;
			
//			System.out.println(this.currentScore);
			Graphics2D gc = this.currentImg.createGraphics();
			gc.setColor(Color.BLUE);
			gc.drawString(String.format("%.4f", this.currentScore) + "%", 10, 10);
//			gc.dispose();
			
			if (count % 25 == 0) {
				String filename = ImagesGA_FinalProduct.folderName + "/best_" + bestCount + ".png";
				
				ImagesGA_FinalProduct.saveImage(this.currentImg, filename);
				bestCount++;
			}
			count++;
			
			if (this.currentScore > 99.99999) {
				System.exit(0);
			}
		}
		this.newDNA = null;
		this.newImgPixels = null;
		this.newScore = -1;
		this.newW = -1;
		this.newH = -1;
	}
	
//	private static double map(double X, double A, double B, double C, double D) {
//		double answer = (X-A)/(B-A) * (D-C) + C;
//		return answer;
//	}
	
	public BufferedImage getCurrentImage() {
		return this.currentImg;
	}
	
	public BufferedImage getNewImage() {
		return this.newImg;
	}
	
	public void saveCurrentDNA() {
		
		try {
			FileWriter file = new FileWriter(ImagesGA_FinalProduct.dnaFile);
			BufferedWriter bufferedWriter = new BufferedWriter(file);
			bufferedWriter.write(this.currentDNA.save());
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImagesGA_FinalProduct.save = false;
		
	}
	
	private void importDNA() {
		
		String line = null;
		String dnaString = "";
		
		try {

			FileReader file = new FileReader(ImagesGA_FinalProduct.dnaFile);
			BufferedReader bufferedReader = new BufferedReader(file);
		
           try {
			while((line = bufferedReader.readLine()) != null) {
			        dnaString += line;
			        
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.currentDNA = DNA.importDNA(dnaString);
		ImagesGA_FinalProduct.importDNA = false;
	}
	
}
