package com.infoDiscover.infoAnalyse.service.restful.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wangychu on 5/2/17.
 */
@XmlRootElement(name = "TypeInstanceSimilarDataDetailVO")
public class TypeInstanceSimilarDataDetailVO {

    private MeasurableInstanceAndRelationsDetailInfoVO sourceTypeInstanceDetailInfo;
    private List<MeasurableInstanceAndRelationsDetailInfoVO> similarTypeInstancesDetailInfoList;

    public MeasurableInstanceAndRelationsDetailInfoVO getSourceTypeInstanceDetailInfo() {
        return sourceTypeInstanceDetailInfo;
    }

    public void setSourceTypeInstanceDetailInfo(MeasurableInstanceAndRelationsDetailInfoVO sourceTypeInstanceDetailInfo) {
        this.sourceTypeInstanceDetailInfo = sourceTypeInstanceDetailInfo;
    }

    public List<MeasurableInstanceAndRelationsDetailInfoVO> getSimilarTypeInstancesDetailInfoList() {
        return similarTypeInstancesDetailInfoList;
    }

    public void setSimilarTypeInstancesDetailInfoList(List<MeasurableInstanceAndRelationsDetailInfoVO> similarTypeInstancesDetailInfoList) {
        this.similarTypeInstancesDetailInfoList = similarTypeInstancesDetailInfoList;
    }
}
