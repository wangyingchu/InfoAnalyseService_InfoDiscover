package com.infoDiscover.infoAnalyse.service.restful.infoDiscoverSpaceDataCRUD;

import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDInputVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.MeasurableQueryResultSetVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDResultVO;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceDataCRUDUtil;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationConstant;
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

    @POST
    @Path("/createRelationable/")
    @Produces("application/json")
    public DataCRUDResultVO createRelationable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALIDINPUT);
        }else{
            DataCRUDResultVO.operationResultCodeValue resultCode=DiscoverSpaceDataCRUDUtil.createTypeDataFromJSON(dataCRUDInput.getDiscoverSpaceName(),dataCRUDInput.getDataPayload());
            dataCRUDResultVO.setOperationReturnCode(resultCode);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }

    @POST
    @Path("/updateRelationable/")
    @Produces("application/json")
    public DataCRUDResultVO updateRelationable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getDataPayload()==null){
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALIDINPUT);
        }else{
            DataCRUDResultVO.operationResultCodeValue resultCode=DiscoverSpaceDataCRUDUtil.updateTypeDataFromJSON(dataCRUDInput.getDiscoverSpaceName(),dataCRUDInput.getDataPayload());
            dataCRUDResultVO.setOperationReturnCode(resultCode);
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }

    @DELETE
    @Path("/deleteMeasurable/")
    @Produces("application/json")
    public DataCRUDResultVO deleteMeasurable(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        if(dataCRUDInput.getDiscoverSpaceName()==null||dataCRUDInput.getMeasurableId()==null||dataCRUDInput.getMeasurableTypeKind()==null){
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALIDINPUT);
        }else{
            if(!DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION.equals(dataCRUDInput.getMeasurableTypeKind().trim())&&
                    !DiscoverSpaceOperationConstant.TYPEKIND_FACT.equals(dataCRUDInput.getMeasurableTypeKind().trim())&&
                    !DiscoverSpaceOperationConstant.TYPEKIND_RELATION.equals(dataCRUDInput.getMeasurableTypeKind().trim())){
                dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALIDINPUT);
            }else{
                DataCRUDResultVO.operationResultCodeValue resultCode=DiscoverSpaceDataCRUDUtil.deleteMeasurable(dataCRUDInput.getDiscoverSpaceName(),dataCRUDInput.getMeasurableTypeKind().trim(),dataCRUDInput.getMeasurableId());
                dataCRUDResultVO.setOperationReturnCode(resultCode);
            }
        }
        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }


    @POST
    @Path("/batchCreateRelationable/")
    @Produces("application/json")
    public DataCRUDResultVO batchCreateRelationable(String discoverSpaceName, String measurableTypeKind, String measurableName, String dataContent){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        if(discoverSpaceName==null||measurableTypeKind==null||measurableName==null||dataContent==null){
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALIDINPUT);
        }
        if(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION.equals(measurableTypeKind.trim())){}
        if(DiscoverSpaceOperationConstant.TYPEKIND_FACT.equals(measurableTypeKind.trim())){}

        dataCRUDResultVO.setOperationExecuteTime(new Date().getTime());
        return dataCRUDResultVO;
    }
}
