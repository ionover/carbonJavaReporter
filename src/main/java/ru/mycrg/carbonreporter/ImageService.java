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
        if (!StringUtils.hasText(picture)) {
            System.out.println("Picture пустой, загружаем дефолтную картинку: " + DEFAULT_IMAGE);
            return loadImageAsBase64(DEFAULT_IMAGE);
        }

        if (picture.startsWith("data:image/")) {
            System.out.println("Picture уже в формате base64, используем как есть");
            return picture;
        }

        if (picture.startsWith("http://") || picture.startsWith("https://")) {
            System.out.println("Picture это URL, загружаем дефолтную картинку: " + DEFAULT_IMAGE);
            return loadImageAsBase64(DEFAULT_IMAGE);
        }

        System.out.println("Загружаем картинку из ресурсов: " + picture);
        String base64Image = loadImageAsBase64(picture);

        if (base64Image == null) {
            System.out.println("Картинка не найдена, загружаем дефолтную: " + DEFAULT_IMAGE);
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
