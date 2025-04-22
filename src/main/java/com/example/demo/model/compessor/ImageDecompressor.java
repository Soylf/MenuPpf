package com.example.demo.model.compessor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

public class ImageDecompressor {
    public static byte[] decompress(byte[] compressedData) throws IOException {
        try (InflaterInputStream inflate = new InflaterInputStream(new ByteArrayInputStream(compressedData))) {
            return inflate.readAllBytes();
        }
    }
}
