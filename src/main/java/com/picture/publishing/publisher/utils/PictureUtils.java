package com.picture.publishing.publisher.utils;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PictureUtils {
	private static final List<String> allowedExtensions = new ArrayList<>(Arrays.asList("jpg", "png", "gif"));

	public boolean validateFile(MultipartFile file) {
		if (!(file == null || file.isEmpty())) {
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());

			if (allowedExtensions.contains(extension.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public Dimension getImageDimensions(MultipartFile file) throws IOException {
		BufferedImage image = ImageIO.read(file.getInputStream());
		int width = image.getWidth();
		int height = image.getHeight();
		return new Dimension(width, height);
	}
}
