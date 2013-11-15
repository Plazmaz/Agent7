package me.dylan.Agent7.res;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class ContentLoader {
	public static BufferedImage getImageFromInternalFile(String internalFilePath)
			throws IOException {
		return ImageIO.read(ContentLoader.class.getResource(internalFilePath));
		
	}
	public static InputStream getInternalFile(String internalFilePath) throws IOException {
		URL path = ContentLoader.class.getResource(internalFilePath);
		return path.openStream();
	}
}
