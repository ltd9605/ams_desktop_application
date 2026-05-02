package com.ams.ams_app.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CacheManager {
    private static final String CACHE_DIR = "local_cache/";

    static {
        // Created folder 
        File directory = new File(CACHE_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static void saveCache(String filename, String jsonContent) {
        try {
            Files.write(Paths.get(CACHE_DIR + filename + ".json"), jsonContent.getBytes());
            System.out.println("Đã lưu cache!");
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu cache: " + e.getMessage());
        }
    }
    public static String readCache(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(CACHE_DIR + filename + ".json")));
        } catch (Exception e) {
            System.err.println("Không tìm thấy file cache: " + filename);
            return null;
        }
    }
}