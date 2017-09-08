import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ImagesGA_FinalProduct extends JFrame {
	public static int width = 600, height = 600;
	private Population population;
	private static JPanel controlPane;
	private static JPanel mainPane;
	private static JButton startButton;
	private static JButton pauseButton;
	private static JButton saveButton;
	private static JButton importButton;
	
	public static ReferenceImg referenceImgPane;
	public static BestImg bestImgPane;
	public static Img imgPane;
	
	private static JPanel radioButtonPane;
	private static ButtonGroup mutationButtonGroup;
	private static JRadioButton mutationSoft;
	private static JRadioButton mutationMedium;
	private static JRadioButton mutationHard;
	private static JRadioButton mutationLapLace;
	
	private static JToggleButton polyToggle;
	private static JToggleButton hillClimbToggle;
	
	private static JSlider popSlider;
	public static int popCount = 5;
	private static JSlider vertSlider;
	public static int vertCount = 0;
	
	public static boolean pause = false;
	
	public static boolean mrS = true;
	public static boolean mrH = false;
	public static boolean mrM = false;
	public static boolean mrL = false;
	public static boolean polyOn = true;
	public static boolean hillClimbOn = true;
	public static boolean save = false;
	public static boolean importDNA = false;
	
	public static String path = "src/img.png"; //"src/ml.bmp";
	private static String iconPath = "src/icon.png";
	private static BufferedImage icon;
	public static String folderName = "bestImages";
	public static String dnaFile = "currentDNA.txt";
	public static BufferedImage targetImage;
	public static int[][] targetPixels;
	public static int[][] targetHistogram;
	public static int maxImgWidth, maxImgHeight;
	
	public ImagesGA_FinalProduct() {
		targetImage = ImagesGA_FinalProduct.getImage(path);
		maxImgWidth = targetImage.getWidth();
		maxImgHeight = targetImage.getHeight();
		targetPixels = ImagesGA_FinalProduct.getPixels(targetImage);
		targetHistogram = ImagesGA_FinalProduct.getHistogram(targetPixels);
		icon = ImagesGA_FinalProduct.getImage(iconPath);
		ImagesGA_FinalProduct.makeFolder(folderName);
		
		
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("ImageGA");
		this.setIconImage(icon);
		this.addWindowListener();
		this.addKeyListener();
		
		this.prepareImagePanes();
		this.prepareSliders();
		this.prepareButtons();
		this.prepareRadioButtons();
		this.prepareToggleButtons();
		this.prepareControlPane();
		this.prepareMainPane();
		
		this.add(mainPane);
		
		this.setVisible(true);
	}
	
	public static BufferedImage getImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return image;
	}
	
	public static void saveImage(BufferedImage img, String filename) {
		try {
			File output = new File(filename);
			ImageIO.write(img, "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void makeFolder(String folder) {
		File directory = new File(folder);
		if (directory.exists()) {
			return;
		} else {
			directory.mkdirs();
		}
	}
	
	public static int[][] getPixels(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		int[][] pixels = new int[h][w];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixelValue = img.getRGB(x, y);
				pixels[x][y] = pixelValue;
			}
		}
		
		return pixels;
	}
	
	/* pretty much dead code at the moment
	public static int[][] getHistogram(int[][] pixels) {
		int w = pixels.length;
		int h = pixels[0].length;
		int[][] histogram = new int[4][5];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixelValue = pixels[x][y];
				
				int r = (pixelValue >> 16) & 0xFF;
				int g = (pixelValue >> 6) & 0xFF;
				int b = (pixelValue) & 0xFF;
				int a = (pixelValue >> 24) & 0xFF;
				
//				sets up r histogram
				if (r <= 51) {
					histogram[0][0] += 1;
				} else if ((r > 51) && (r <= 102)) {
					histogram[0][1] += 1;
				} else if ((r > 102) && (r <= 153)) {
					histogram[0][2] += 1;
				} else if ((r > 153) && (r <= 204)) {
					histogram[0][3] += 1;
				} else if ((r > 204) && (r <= 255)) {
					histogram[0][4] += 1;
				}
				
//				sets up g histogram
				if (g <= 51) {
					histogram[1][0] += 1;
				} else if ((g > 51) && (g <= 102)) {
					histogram[1][1] += 1;
				} else if ((g > 102) && (g <= 153)) {
					histogram[1][2] += 1;
				} else if ((g > 153) && (g <= 204)) {
					histogram[1][3] += 1;
				} else if ((g > 204) && (g <= 255)) {
					histogram[1][4] += 1;
				}
				
//				sets up b histogram
				if (b <= 51) {
					histogram[2][0] += 1;
				} else if ((b > 51) && (b <= 102)) {
					histogram[2][1] += 1;
				} else if ((b > 102) && (b <= 153)) {
					histogram[2][2] += 1;
				} else if ((b > 153) && (b <= 204)) {
					histogram[2][3] += 1;
				} else if ((b > 204) && (b <= 255)) {
					histogram[2][4] += 1;
				}
				
//				sets up a histogram
				if (a <= 51) {
					histogram[3][0] += 1;
				} else if ((a > 51) && (a <= 102)) {
					histogram[3][1] += 1;
				} else if ((a > 102) && (a <= 153)) {
					histogram[3][2] += 1;
				} else if ((a > 153) && (a <= 204)) {
					histogram[3][3] += 1;
				} else if ((a > 204) && (a <= 255)) {
					histogram[3][4] += 1;
				}
				
			}
		}
		return histogram;
	}
	*/
	
	private void prepareImagePanes() {
		referenceImgPane = new ReferenceImg();
		
		bestImgPane = new BestImg();
		
		imgPane = new Img();
	}
	
	private void prepareSliders() {
		popSlider = new JSlider();
		popSlider.setFocusable(false);
		popSlider.setValue(5);
		popSlider.setMinimum(1);
		popSlider.setMaximum(500);
		popSlider.setMajorTickSpacing(99);
		popSlider.setMinorTickSpacing(11);
		popSlider.setPaintTicks(true);
		popCount = popSlider.getValue();
		this.addChangeListener(popSlider);
		
		
		vertSlider = new JSlider();
		vertSlider.setFocusable(false);
		vertSlider.setMinimum(3);
		vertSlider.setMaximum(20);
		vertSlider.setMajorTickSpacing(1);
		vertSlider.setPaintTicks(true);
		vertCount = vertSlider.getValue();
		this.addChangeListener(vertSlider);
	}
	
	private void prepareButtons() {
		startButton = new JButton("Start");
		startButton.setFocusable(false);
		this.addActionListenerButton(startButton);
		
		pauseButton = new JButton("Pause");
		pauseButton.setFocusable(false);
		this.addActionListenerButton(pauseButton);
		
		saveButton = new JButton("Save");
		saveButton.setFocusable(false);
		this.addActionListenerButton(saveButton);
		
		importButton = new JButton("Import");
		importButton.setFocusable(false);
		this.addActionListenerButton(importButton);
	}
	
	private void prepareRadioButtons() {
		radioButtonPane = new JPanel();
//		radioButtonPane.setBackground(Color.GRAY);
		radioButtonPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		mutationButtonGroup = new ButtonGroup();
		
		mutationSoft = new JRadioButton("Soft Mutation");
		mutationSoft.setSelected(true);
		mutationSoft.setFocusable(false);
		this.addActionListenerRadio(mutationSoft);
		
		mutationHard = new JRadioButton("Hard Mutation");
		mutationHard.setFocusable(false);
		this.addActionListenerRadio(mutationHard);
		
		mutationMedium = new JRadioButton("Medium Mutation");
		mutationMedium.setFocusable(false);
		this.addActionListenerRadio(mutationMedium);
		
		mutationLapLace = new JRadioButton("LapLace Mutation");
		mutationLapLace.setFocusable(false);
		this.addActionListenerRadio(mutationLapLace);
		
		mutationButtonGroup.add(mutationSoft);
		mutationButtonGroup.add(mutationMedium);
		mutationButtonGroup.add(mutationHard);
		mutationButtonGroup.add(mutationLapLace);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		radioButtonPane.add(mutationSoft, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		radioButtonPane.add(mutationHard, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		radioButtonPane.add(mutationMedium, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		radioButtonPane.add(mutationLapLace, gbc);
	}
	
	private void prepareToggleButtons() {
		polyToggle = new JToggleButton("Polygon / Ellipse");
		polyToggle.setSelected(true);
		polyToggle.setFocusable(false);
		this.addActionListenerToggle(polyToggle);
		
		hillClimbToggle = new JToggleButton("Hill Climb / GA");
		hillClimbToggle.setSelected(true);
		hillClimbToggle.setFocusable(false);
		this.addActionListenerToggle(hillClimbToggle);
	}
	
	private void prepareControlPane() {
		controlPane = new JPanel();
		controlPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		controlPane.add(referenceImgPane, gbc);
		
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		controlPane.add(popSlider, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		controlPane.add(vertSlider, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		controlPane.add(bestImgPane, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.CENTER;
		controlPane.add(saveButton, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.CENTER;
		controlPane.add(importButton, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		controlPane.add(imgPane, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		controlPane.add(startButton, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		controlPane.add(pauseButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 6;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		controlPane.add(radioButtonPane, gbc);
		
//		gbc.gridx = 1;
//		gbc.gridy = 8;
//		gbc.gridheight = 1;
//		gbc.fill = GridBagConstraints.VERTICAL;
//		controlPane.add(polyToggle, gbc);
//		
//		gbc.gridx = 1;
//		gbc.gridy = 9;
//		gbc.gridheight = 1;
//		gbc.fill = GridBagConstraints.VERTICAL;
//		controlPane.add(hillClimbToggle, gbc);

	}
	
	private void prepareMainPane() {
		mainPane = new JPanel();
		mainPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.CENTER;
		mainPane.add(controlPane, gbc);
		
//		this.add(mainPane);
//		this.pack();
	}
	
	private void addActionListenerButton(JButton button) {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Button Pressed");
				
				if (button == startButton) {
					init();
					startButton.setEnabled(false);
					popSlider.setEnabled(false);
					vertSlider.setEnabled(false);
					polyToggle.setEnabled(false);
					hillClimbToggle.setEnabled(false);
				} else if (button == pauseButton) {
					if (pause) {
						pause = false;
						pauseButton.setText("Pause");
					} else {
						pause = true;
						pauseButton.setText("Resume");
					}
					
				} else if (button == saveButton) {
					save = true;
				} else if (button == importButton) {
					importDNA = true;
				}
			}
			
		});
	}
	
	private void addActionListenerRadio(JRadioButton radiobutton) {
		radiobutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (radiobutton == mutationSoft) {
					mrS = true;
					mrH = false;
					mrM = false;
					mrL = false;
					System.out.println("Soft Mutation");
				} else if (radiobutton == mutationHard) {
					mrS = false;
					mrH = true;
					mrM = false;
					mrL = false;
					System.out.println("Hard Mutation");
				} else if (radiobutton == mutationMedium) {
					mrS = false;
					mrH = false;
					mrM = true;
					mrL = false;
					System.out.println("Medium Mutation");
				} else if (radiobutton == mutationLapLace) {
					mrS = false;
					mrH = false;
					mrM = false;
					mrL = true;
					System.out.println("LapLace Mutation");
				}
			}
			
		});
	}
	
	private void addActionListenerToggle(JToggleButton toggle) {
		toggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (toggle == polyToggle) {
					if (toggle.isSelected()) {
						polyOn = true;
					} else {
						polyOn = false;
					}
				
				} else if (toggle == hillClimbToggle) {
					if (toggle.isSelected()) {
						hillClimbOn = true;
					} else {
						hillClimbOn = false;
					}
				}
			}
			
		});
	}
	
	private void addChangeListener(JSlider slider) {
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if (slider == popSlider) {
					System.out.println("popSlider " + popSlider.getValue());
					popCount = popSlider.getValue();
				} else if (slider == vertSlider) {
					System.out.println("vertSlider " + vertSlider.getValue());
					vertCount = vertSlider.getValue();
				}
			}
			
		});
	}
	
	private void addWindowListener() {
		ImagesGA_FinalProduct frame = this;
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (frame.population != null) {
					frame.population.saveCurrentDNA();
				}
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void addKeyListener() {
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Key Pressed");
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void init() {
		this.population = new Population(ImagesGA_FinalProduct.popCount, ImagesGA_FinalProduct.vertCount);
		UpdateImages update = new UpdateImages(this.population);
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		executor.scheduleAtFixedRate(new Redraw(update), 0L, 25, TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ImagesGA_FinalProduct();
	}

}

///////////////////////////////////////////////////////////////////////////////
@SuppressWarnings("serial")
class ReferenceImg extends JPanel {
	private BufferedImage referenceImg;
	private JLabel referenceImgLabel;
	
	public ReferenceImg() {
		this.referenceImg = ImagesGA_FinalProduct.getImage(ImagesGA_FinalProduct.path);
		this.referenceImgLabel = new JLabel(new ImageIcon(this.referenceImg));
//		this.setBackground(Color.RED);
		this.add(this.referenceImgLabel);
	}
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.drawImage(this.referenceImg, 0, 0, null);
//		graphics.dispose();
		
	}
}

@SuppressWarnings("serial")
class BestImg extends JPanel {
	private BufferedImage bestImg;
	private JLabel bestImgLabel;
	
	public BestImg() {
		this.bestImg = ImagesGA_FinalProduct.getImage(ImagesGA_FinalProduct.path);
		this.bestImgLabel = new JLabel(new ImageIcon(this.bestImg));
//		this.setBackground(Color.CYAN);
		this.setSize(this.bestImg.getWidth(), this.bestImg.getHeight());
		this.add(this.bestImgLabel);
	}
	
	public void updateImage(BufferedImage newImage) {
		this.bestImg = newImage;
	}
	
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.drawImage(this.bestImg, 0, 0, null);
//		graphics.dispose();
	}
}

@SuppressWarnings("serial")
class Img extends JPanel {
	private BufferedImage img;
	private JLabel imgLabel;
	
	public Img() {
		this.img = ImagesGA_FinalProduct.getImage(ImagesGA_FinalProduct.path);
		this.imgLabel = new JLabel(new ImageIcon(this.img));
//		this.setBackground(Color.GREEN);
		this.setSize(this.img.getWidth(), this.img.getHeight());
		this.add(this.imgLabel);
	}
	
	public void updateImage(BufferedImage newImage) {
		this.img = newImage;
	}
	
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.drawImage(this.img, 0, 0, null);
//		graphics.dispose();
	}
}

///////////////////////////////////////////////////////////////////////////////
class Redraw implements Runnable {
	UpdateImages window;
	
	public Redraw(UpdateImages window) {
		this.window = window;
	}
	
	public void run() {
		this.window.updateImages();
	}
}


///////////////////////////////////////////////////////////////////////////////
class UpdateImages {
	private Population population;
	
	public UpdateImages(Population population) {
		this.population = population;
	}
	
	public void updateImages() {
		if (!ImagesGA_FinalProduct.pause) {
			
			this.population.run();
			
			
			ImagesGA_FinalProduct.bestImgPane.updateImage(this.population.getCurrentImage());
			ImagesGA_FinalProduct.imgPane.updateImage(this.population.getNewImage());
			
//			ImagesGA_FinalProduct.referenceImgPane.repaint();
			ImagesGA_FinalProduct.bestImgPane.repaint();
			ImagesGA_FinalProduct.imgPane.repaint();
		}
	}
}
