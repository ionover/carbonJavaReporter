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

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate; // или WebClient
    private final String carboneBaseUrl;

    public CarboneService(ObjectMapper objectMapper,
                          RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.carboneBaseUrl = "http://10.10.10.61:4000";
    }

    public byte[] renderHelloReport(String text, String templateId) {
        Map<String, Object> data = Map.of(
                "project", Map.of("name", "Test project"),
                "date", LocalDate.now().toString(),
                "text", text
        );

        Map<String, Object> body = Map.of(
                "data", data,
                "convertTo", "pdf"
        );

        String renderUrl = carboneBaseUrl + "/render/" + templateId;
        ResponseEntity<String> renderResponse = restTemplate.postForEntity(renderUrl, body, String.class);

        try {
            JsonNode root = objectMapper.readTree(renderResponse.getBody());
            JsonNode dataNode = root.get("data");
            JsonNode renderIdNode = dataNode.get("renderId");
            
            if (renderIdNode == null || renderIdNode.isNull()) {
                throw new IllegalStateException("В ответе Carbone нет renderId: " + renderResponse.getBody());
            }
            
            String renderId = renderIdNode.asText();
            
            String downloadUrl = carboneBaseUrl + "/render/" + renderId;
            ResponseEntity<byte[]> pdfResponse = restTemplate.getForEntity(downloadUrl, byte[].class);
            
            return pdfResponse.getBody();
            
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при рендеринге отчёта", e);
        }
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

            // Ожидаем JSON вида {"success":true,"data":{"templateId":"..."}}
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            if (dataNode == null || dataNode.isNull()) {
                throw new IllegalStateException("В ответе Carbone нет поля data: " + response.getBody());
            }

            JsonNode idNode = dataNode.get("templateId");

            if (idNode == null || idNode.isNull()) {
                throw new IllegalStateException("В ответе Carbone нет поля templateId: " + response.getBody());
            }

            return idNode.asText();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла шаблона", e);
        }
    }
}