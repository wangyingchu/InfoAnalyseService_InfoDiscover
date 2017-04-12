package com.infoDiscover.infoAnalyse.service.restful.vo;

import java.util.List;

/**
 * Created by wangychu on 3/6/17.
 */
public class MeasurableVO {
    private List<PropertyVO> measurableProperties;

    public List<PropertyVO> getMeasurableProperties() {
        return measurableProperties;
    }

    public void setMeasurableProperties(List<PropertyVO> measurableProperties) {
        this.measurableProperties = measurableProperties;
    }
}
