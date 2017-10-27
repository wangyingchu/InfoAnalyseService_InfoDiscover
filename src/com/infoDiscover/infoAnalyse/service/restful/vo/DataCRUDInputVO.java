package com.infoDiscover.infoAnalyse.service.restful.vo;

public class DataCRUDInputVO {

    private String discoverSpaceName;
    private DataPayloadWrapperVO dataPayload;
    private String measurableId;
    private String measurableTypeKind;

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getMeasurableId() {
        return measurableId;
    }

    public void setMeasurableId(String measurableId) {
        this.measurableId = measurableId;
    }

    public String getMeasurableTypeKind() {
        return measurableTypeKind;
    }

    public void setMeasurableTypeKind(String measurableTypeKind) {
        this.measurableTypeKind = measurableTypeKind;
    }

    public DataPayloadWrapperVO getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(DataPayloadWrapperVO dataPayload) {
        this.dataPayload = dataPayload;
    }
}
