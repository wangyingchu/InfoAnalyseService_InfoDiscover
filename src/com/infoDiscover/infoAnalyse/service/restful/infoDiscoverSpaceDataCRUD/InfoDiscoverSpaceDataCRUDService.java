package com.infoDiscover.infoAnalyse.service.restful.infoDiscoverSpaceDataCRUD;

import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDInputVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.MeasurableQueryResultSetVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDResultVO;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceDataCRUDUtil;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;

import javax.ws.rs.*;
import java.util.Date;

@Path("/infoDiscoverSpaceDataCRUDService")
@Produces("application/json")
public class InfoDiscoverSpaceDataCRUDService {

    @POST
    @Path("/queryMeasurable/")
    @Produces("application/json")
    public MeasurableQueryResultSetVO queryMeasurable(String discoverSpaceName,ExploreParameters exploreParameters){
        return null;
    }

    @PUT
    @Path("/createRelationable/")
    @Produces("application/json")
    public DataCRUDResultVO createRelationable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }else{
            DataCRUDResultVO.operationResultCodeValue resultCode=DiscoverSpaceDataCRUDUtil.createTypeDataFromJSON(dataCRUDInput.getDiscoverSpaceName(),dataCRUDInput.getDataPayload());
            dataCRUDResultVO.setOperationReturnCode(resultCode);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }

    @POST
    @Path("/updateMeasurable/")
    @Produces("application/json")
    public DataCRUDResultVO updateMeasurable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO;
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO=new DataCRUDResultVO();
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }else{
            dataCRUDResultVO=DiscoverSpaceDataCRUDUtil.updateTypeData(dataCRUDInput);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }

    @DELETE
    @Path("/deleteMeasurable/")
    @Produces("application/json")
    public DataCRUDResultVO deleteMeasurable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO;
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO=new DataCRUDResultVO();
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }else{
            dataCRUDResultVO=DiscoverSpaceDataCRUDUtil.deleteTypeData(dataCRUDInput);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }

    @DELETE
    @Path("/deleteMeasurableProperties/")
    @Produces("application/json")
    public DataCRUDResultVO deleteMeasurableProperties(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO;
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO=new DataCRUDResultVO();
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }else{
            dataCRUDResultVO=DiscoverSpaceDataCRUDUtil.deleteTypeDataProperties(dataCRUDInput);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }
}
