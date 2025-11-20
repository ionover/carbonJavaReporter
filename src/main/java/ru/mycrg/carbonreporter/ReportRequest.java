package ru.mycrg.carbonreporter;

public class ReportRequest {

    private String text;
    private String templateId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ReportRequest() {
    }

    public ReportRequest(String text, String templateId) {
        this.text = text;
        this.templateId = templateId;
    }
}
