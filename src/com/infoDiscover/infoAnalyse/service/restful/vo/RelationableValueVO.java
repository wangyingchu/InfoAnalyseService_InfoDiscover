package com.infoDiscover.infoAnalyse.service.restful.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wangychu on 12/8/16.
 */
@XmlRootElement(name = "RelationableValueVO")
public class RelationableValueVO {
    private String id;
    private String discoverSpaceName;
    private String relationableTypeName;
    private String relationableTypeKind;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscoverSpaceName() {
        return discoverSpaceName;
    }

    public void setDiscoverSpaceName(String discoverSpaceName) {
        this.discoverSpaceName = discoverSpaceName;
    }

    public String getRelationableTypeName() {
        return relationableTypeName;
    }

    public void setRelationableTypeName(String relationableTypeName) {
        this.relationableTypeName = relationableTypeName;
    }

    public String getRelationableTypeKind() {
        return relationableTypeKind;
    }

    public void setRelationableTypeKind(String relationableTypeKind) {
        this.relationableTypeKind = relationableTypeKind;
    }
}
