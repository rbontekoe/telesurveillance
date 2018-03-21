package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CompareImages {

	public double compareImages(File fileNew, File fileOld) {
		System.out.println(fileNew + " / " + fileOld);

		double result = 0;
		
		try {
			BufferedImage img1 = ImageIO.read(fileNew);
			BufferedImage img2 = ImageIO.read(fileOld);
			result = compare(img1, img2);
		} catch (IOException e) {
//			e.printStackTrace();
			result = 10000.0;
		}

		return result;
	}

	public double compare(BufferedImage img1, BufferedImage img2) {
		int width1 = img1.getWidth();
		int width2 = img2.getWidth();
		int height1 = img1.getHeight();
		int height2 = img2.getHeight();
		if ((width1 != width2) || (height1 != height2)) {
			throw new IllegalArgumentException("Error: Images dimensions mismatch");
		}
		int diff2 = 0;
		for (int i = 0; i < height1; i++) {
			for (int j = 0; j < width1; j++) {
				int rgb1 = img1.getRGB(j, i);
				int rgb2 = img2.getRGB(j, i);
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = (rgb1) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = (rgb2) & 0xff;
				diff2 += Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
			}
		}
		return diff2 * 1.0 / (height1 * width1);
	}
}