package com.glbpay.mock.model;

import java.util.Map;

/**
 * Created by shouhutsh on 6/29/2017.
 */
public class Channel {

    private String name;
    private String regex;
    private String format;
    private String template;
    private String specialField;    // FIXME 换个名字
    private Map<String, String> specialMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSpecialField() {
        return specialField;
    }

    public void setSpecialField(String specialField) {
        this.specialField = specialField;
    }

    public Map<String, String> getSpecialMap() {
        return specialMap;
    }

    public void setSpecialMap(Map<String, String> specialMap) {
        this.specialMap = specialMap;
    }
}
