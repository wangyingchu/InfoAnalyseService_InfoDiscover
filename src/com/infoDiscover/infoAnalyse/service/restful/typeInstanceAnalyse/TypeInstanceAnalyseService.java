package com.infoDiscover.infoAnalyse.service.restful.typeInstanceAnalyse;

import com.infoDiscover.infoAnalyse.service.restful.vo.*;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
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
    @Path("/relationRelatedTypeInstancesExplore/{discoverSpaceName}/{relationId}")
    @Produces("application/json")
    public RelationDetailInfoVO getRelationRelatedTypeInstancesDetailInfo(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationId")String relationId){
        RelationDetailInfoVO resultInfo=DiscoverSpaceOperationUtil.getRelationDetailInfoById(discoverSpaceName,relationId);
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

    @GET
    @Path("/similarTypeInstancesExplore/{discoverSpaceName}/{relationableId}/{dimensionsIdList}/{filteringPattern}")
    @Produces("application/json")
    public TypeInstanceSimilarDataDetailVO getSimilarRelationableInfoConnectedSameDimensions(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationableId")String relationableId,
                                                                                             @PathParam("dimensionsIdList")String dimensionsIdList, @PathParam("filteringPattern")String filteringPattern){
        TypeInstanceSimilarDataDetailVO resultInfo=DiscoverSpaceOperationUtil.getSimilarRelationableConnectedSameDimensions(discoverSpaceName,relationableId,dimensionsIdList,filteringPattern);
        return resultInfo;
    }

    @GET
    @Path("/typeInstancesShortestPathExplore/{discoverSpaceName}/{relationableAId}/{relationableBId}")
    @Produces("application/json")
    public ShortestPathBetweenTwoMeasurablesDetailInfoVO getShortestPathBetweenTwoTypeInstances(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationableAId")String relationableAId,
                                                                                             @PathParam("relationableBId")String relationableBId){
        ShortestPathBetweenTwoMeasurablesDetailInfoVO resultInfo=DiscoverSpaceOperationUtil.getShortestPathBetweenTwoRelationable(discoverSpaceName,relationableAId,relationableBId);
        return resultInfo;
    }

    @GET
    @Path("/typeInstancesAllPathsExplore/{discoverSpaceName}/{relationableAId}/{relationableBId}")
    @Produces("application/json")
    public AllPathsBetweenTwoMeasurablesDetailInfoVO getAllPathsBetweenTwoTypeInstances(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationableAId")String relationableAId,
                                                                                                           @PathParam("relationableBId")String relationableBId){
        AllPathsBetweenTwoMeasurablesDetailInfoVO resultInfo=DiscoverSpaceOperationUtil.getAllPathsBetweenTwoRelationable(discoverSpaceName,relationableAId,relationableBId);
        return resultInfo;
    }
}
