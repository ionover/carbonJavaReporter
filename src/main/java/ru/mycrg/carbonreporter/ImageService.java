package ru.mycrg.carbonreporter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class ImageService {

    private static final String DEFAULT_IMAGE = "static_map_picture.png";

    public String prepareImageForCarbone(String picture) {
        if (!StringUtils.hasText(picture) || picture.startsWith("http://") || picture.startsWith("https://")) {
            return loadImageAsBase64(DEFAULT_IMAGE);
        }

        String imageToLoad = picture;
        String base64Image = loadImageAsBase64(imageToLoad);

        if (base64Image == null) {
            base64Image = loadImageAsBase64(DEFAULT_IMAGE);
        }

        return base64Image;
    }

    private String loadImageAsBase64(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            if (!resource.exists()) {
                return null;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                return "data:image/png;base64," + base64;
            }
        } catch (IOException e) {
            System.err.println("Не удалось загрузить изображение: " + fileName);
            return null;
        }
    }
}
