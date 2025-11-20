package ru.mycrg.carbonreporter.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReportData {

    private String picture;
    private String title;
    private String value;
    private String crs;
    private BigDecimal area;
    private List<CoordinateDto> coords;

    public ReportData() {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public List<CoordinateDto> getCoords() {
        return coords;
    }

    public void setCoords(List<CoordinateDto> coords) {
        this.coords = coords;
    }
}
