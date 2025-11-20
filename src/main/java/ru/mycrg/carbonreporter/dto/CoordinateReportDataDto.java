package ru.mycrg.carbonreporter.dto;

public class CoordinateReportDataDto {

    private String templateId;
    private ReportData d;

    public CoordinateReportDataDto() {
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ReportData getD() {
        return d;
    }

    public void setD(ReportData d) {
        this.d = d;
    }
}
