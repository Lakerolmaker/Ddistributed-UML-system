package MainPackage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * 
 * A class for merging images,  does have an limit
 * 
 */


public class ImageMerger {

	public BufferedImage joinHorizontal(BufferedImage i1, BufferedImage i2, int mergeWidth) {
		if (i1.getHeight() != i2.getHeight())
			throw new IllegalArgumentException("Images i1 and i2 are not the same height");

		BufferedImage imgClone = new BufferedImage(mergeWidth, i2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D cloneG = imgClone.createGraphics();
		cloneG.drawImage(i2, 0, 0, null);
		cloneG.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN, 0.5f));
		cloneG.drawImage(i2, 0, 0, null);

		BufferedImage result = new BufferedImage(i1.getWidth() + i2.getWidth() - mergeWidth, i1.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.drawImage(i1, 0, 0, null);
		g.drawImage(i2.getSubimage(mergeWidth, 0, i2.getWidth() - mergeWidth, i2.getHeight()), i1.getWidth(), 0, null);
		g.drawImage(imgClone, i1.getWidth() - mergeWidth, 0, null);

		return result;
	}

	public BufferedImage joinVertical(BufferedImage i1, BufferedImage i2, int mergeWidth) {
		if (i1.getWidth() != i2.getWidth())
			throw new IllegalArgumentException("Images i1 and i2 are not the same width");

		BufferedImage imgClone = new BufferedImage(i2.getWidth(), mergeWidth, BufferedImage.TYPE_INT_ARGB);
		Graphics2D cloneG = imgClone.createGraphics();
		cloneG.drawImage(i2, 0, 0, null);
		cloneG.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN, 0.5f));
		cloneG.drawImage(i2, 0, 0, null);

		BufferedImage result = new BufferedImage(i1.getWidth(), i1.getHeight() + i2.getHeight() - mergeWidth,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		g.drawImage(i1, 0, 0, null);
		g.drawImage(i2.getSubimage(0, mergeWidth, i2.getWidth(), i2.getHeight() - mergeWidth), 0, i1.getHeight(), null);
		g.drawImage(imgClone, 0, i1.getHeight() - mergeWidth, null);

		return result;

	}

	public BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
		int offset = 2;
		int width = img1.getWidth() + img2.getWidth() + offset;
		int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth() + offset, 0);
		g2.dispose();
		return newImage;
	}

}