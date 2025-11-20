package ru.mycrg.carbonreporter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CarbonController {

    private final CarboneService carboneService;
    private final FileService fileService;

    public CarbonController(CarboneService carboneService, FileService fileService) {
        this.carboneService = carboneService;
        this.fileService = fileService;
    }

    @GetMapping("/give-me-pdf")
    public ResponseEntity<byte[]> generateSimpleReport(@RequestParam String text) {
        byte[] pdfBytes = carboneService.renderHelloReport(text);

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                             .contentType(MediaType.APPLICATION_PDF)
                             .body(pdfBytes);
    }

    @GetMapping("/save-me-pdf")
    public void saveSimpleReport(@RequestParam String text) {
        byte[] pdfBytes = carboneService.renderHelloReport(text);

        fileService.saveFile(pdfBytes);
    }

    @PostMapping("/postTemplate")
    public ResponseEntity<String> postReportFish(@RequestBody MultipartFile file) {
        String templateId = carboneService.postTemplate(file);

        return ResponseEntity.ok(templateId);
    }
}
