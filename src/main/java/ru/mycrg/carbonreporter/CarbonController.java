package ru.mycrg.carbonreporter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.mycrg.carbonreporter.dto.CoordinateReportDataDto;

@RestController
public class CarbonController {

    private final CarboneService carboneService;
    private final FileService fileService;

    public CarbonController(CarboneService carboneService, FileService fileService) {
        this.carboneService = carboneService;
        this.fileService = fileService;
    }

    @PostMapping("/give-me-pdf")
    public ResponseEntity<byte[]> generateSimpleReport(@RequestBody CoordinateReportDataDto dto) {
        byte[] pdfBytes = carboneService.renderCoordinateReport(dto);

        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                             .contentType(MediaType.APPLICATION_PDF)
                             .body(pdfBytes);
    }

    @PostMapping("/save-me-pdf")
    public void saveSimpleReport(@RequestBody CoordinateReportDataDto dto) {
        byte[] pdfBytes = carboneService.renderCoordinateReport(dto);

        fileService.saveFile(pdfBytes);
    }

    @PostMapping("/postTemplate")
    public ResponseEntity<String> postReportFish(@RequestBody MultipartFile file) {
        String templateId = carboneService.postTemplate(file);

        return ResponseEntity.ok(templateId);
    }
}
