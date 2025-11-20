package ru.mycrg.carbonreporter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

@Service
public class CarboneService {

    private static final String TEMPLATE_ID = "SOME_ID";
    private static final String CARBONE_URL = "http://carbone:4000"; // или localhost:4000

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate; // или WebClient
    private final String carboneBaseUrl;

    public CarboneService(ObjectMapper objectMapper,
                          RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.carboneBaseUrl = "http://10.10.10.61:4000";
    }

    public byte[] renderHelloReport(String text) {
        Map<String, Object> data = Map.of(
                "project", Map.of("name", "Test project"),
                "date", LocalDate.now().toString(),
                "text", text
        );

        Map<String, Object> body = Map.of(
                "data", data,
                "convertTo", "pdf"
        );

        String url = CARBONE_URL + "/render/" + TEMPLATE_ID;

        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, body, byte[].class);

        return response.getBody();
    }

    public String postTemplate(MultipartFile file) {
        try {
            // Формируем multipart/form-data с полем "template"
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            body.add("template", fileResource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            String url = carboneBaseUrl + "/template";

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, requestEntity, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("Carbone вернул некорректный ответ: " + response.getStatusCode());
            }

            // Ожидаем JSON вида {"templateId":"..."}
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode idNode = root.get("templateId");

            if (idNode == null || idNode.isNull()) {
                throw new IllegalStateException("В ответе Carbone нет поля templateId: " + response.getBody());
            }

            return idNode.asText();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла шаблона", e);
        }
    }
}