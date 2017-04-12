package com.infoDiscover.infoAnalyse.service.restful.typeInstanceAnalyse;

import com.infoDiscover.infoAnalyse.service.restful.vo.MeasurableInstanceDetailInfoVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.TypeInstanceRelationsCycleVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.TypeInstanceRelationsDetailVO;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by wangychu on 2/13/17.
 */

@Path("/typeInstanceAnalyseService")
@Produces("application/json")
public class TypeInstanceAnalyseService {

    @GET
    @Path("/typeInstanceRelationsCycle/{discoverSpaceName}/{relationableId}")
    @Produces("application/json")
    public TypeInstanceRelationsCycleVO getTypeInstanceRelationsInfoCycle(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationableId")String relationableId){
        TypeInstanceRelationsCycleVO resultInfo=DiscoverSpaceOperationUtil.getRelationableRelationsCycleInfoById(discoverSpaceName,relationableId);
        return resultInfo;
    }

    @GET
    @Path("/typeInstanceRelationsExplore/{discoverSpaceName}/{relationableId}")
    @Produces("application/json")
    public TypeInstanceRelationsDetailVO getTypeInstanceRelationsDetailInfo(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationableId")String relationableId){
        TypeInstanceRelationsDetailVO resultInfo=DiscoverSpaceOperationUtil.getRelationableRelationsDetailInfoById(discoverSpaceName,relationableId);
        return resultInfo;
    }

    @GET
    @Path("/typeInstanceDetailInfo/{discoverSpaceName}/{measurableInstanceId}")
    @Produces("application/json")
    public MeasurableInstanceDetailInfoVO getTypeInstanceDetailInfo(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("measurableInstanceId")String measurableInstanceId){
        MeasurableInstanceDetailInfoVO resultInfo=DiscoverSpaceOperationUtil.getMeasurableInstanceDetailInfo(discoverSpaceName,measurableInstanceId);
        return resultInfo;
    }

    @GET
    @Path("/typeInstancesDetailInfo/{discoverSpaceName}/{measurableInstanceIds}")
    @Produces("application/json")
    public List<MeasurableInstanceDetailInfoVO> getTypeInstancesDetailInfo(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("measurableInstanceIds")String measurableInstanceIds){
        List<MeasurableInstanceDetailInfoVO> resultInfo=DiscoverSpaceOperationUtil.getMeasurableInstancesDetailInfo(discoverSpaceName,measurableInstanceIds);
        return resultInfo;
    }
}
