package com.infoDiscover.infoAnalyse.service.restful.vo;

import java.util.List;

public class DataPayloadVO {

    private String type;
    private String typeName;
    private List<DataPropertyPayloadVO> properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<DataPropertyPayloadVO> getProperties() {
        return properties;
    }

    public void setProperties(List<DataPropertyPayloadVO> properties) {
        this.properties = properties;
    }
}


