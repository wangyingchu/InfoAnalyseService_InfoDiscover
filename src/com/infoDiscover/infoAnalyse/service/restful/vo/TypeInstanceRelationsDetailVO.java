package com.infoDiscover.infoAnalyse.service.restful.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wangychu on 4/11/17.
 */

@XmlRootElement(name = "TypeInstanceRelationsDetailVO")
public class TypeInstanceRelationsDetailVO {

    private RelationableValueDetailVO sourceTypeInstance;
    private List<RelationInfoDetailVO> relationsInfo;

    public RelationableValueDetailVO getSourceTypeInstance() {
        return sourceTypeInstance;
    }

    public void setSourceTypeInstance(RelationableValueDetailVO sourceTypeInstance) {
        this.sourceTypeInstance = sourceTypeInstance;
    }

    public List<RelationInfoDetailVO> getRelationsInfo() {
        return relationsInfo;
    }

    public void setRelationsInfo(List<RelationInfoDetailVO> relationsInfo) {
        this.relationsInfo = relationsInfo;
    }
}
