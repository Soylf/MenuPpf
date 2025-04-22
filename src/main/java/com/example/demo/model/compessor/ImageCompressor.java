package com.example.demo.model.compessor;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.zip.DeflaterOutputStream;

public class ImageCompressor {

    public static byte[] compressImage(byte[] originalBytes) throws IOException {
        InputStream input = new ByteArrayInputStream(originalBytes);
        BufferedImage image = ImageIO.read(input);

        String formatName = getImageFormat(originalBytes);
        if (formatName == null) {
            formatName = "jpg";
        }

        ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, imageOutput);

        ByteArrayOutputStream compressedOut = new ByteArrayOutputStream();
        try (DeflaterOutputStream deflater = new DeflaterOutputStream(compressedOut)) {
            deflater.write(imageOutput.toByteArray());
        }

        return compressedOut.toByteArray();
    }

    private static String getImageFormat(byte[] imageBytes) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageBytes))) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {
                return iter.next().getFormatName().toLowerCase();
            }
        }
        return null;
    }
}
