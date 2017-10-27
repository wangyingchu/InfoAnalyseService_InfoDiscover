package com.infoDiscover.infoAnalyse.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDInputVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataPayloadWrapperVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataTypePropertyInfoVO;
import com.infoDiscover.infoAnalyse.service.restful.vo.DataCRUDResultVO;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.solution.template.DataImporter;

import java.util.*;

public class DiscoverSpaceDataCRUDUtil {

    public static String transferTypeDataToJson(String dataTypeKind,String dataTypeName,List<DataTypePropertyInfoVO> dataPropertiesList){
        Map<String, Object> rootMap = new HashMap<String, Object>();
        List<Map> dataList=new ArrayList<Map>();
        rootMap.put("data", dataList);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataList.add(dataMap);

        if(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION.equals(dataTypeKind)){
            dataMap.put("type","Dimension");
        }
        if(DiscoverSpaceOperationConstant.TYPEKIND_FACT.equals(dataTypeKind)){
            dataMap.put("type","Fact");
        }
        dataMap.put("typeName",dataTypeName);

        List<Map> propertiesMapList=new ArrayList<Map>();
        dataMap.put("properties", propertiesMapList);

        if(dataPropertiesList!=null){
            for(DataTypePropertyInfoVO currentProperty:dataPropertiesList){
                String propertyType=getPropertyTypeCode(currentProperty.getPropertyValue());
                if(propertyType!=null){
                    Map<String,Object> projectNameMap=new HashMap<String, Object>();
                    projectNameMap.put("propertyType", propertyType);
                    projectNameMap.put("propertyName", currentProperty.getPropertyName());

                    if(currentProperty.getPropertyValue() instanceof Date){
                        Long longDateValue=((Date)currentProperty.getPropertyValue()).getTime();
                        projectNameMap.put("propertyValue", longDateValue);
                    }else{
                        projectNameMap.put("propertyValue", currentProperty.getPropertyValue());
                    }
                    propertiesMapList.add(projectNameMap);
                }
            }
        }

        ObjectMapper mapper=new ObjectMapper();
        try {
            String jsonString=mapper.writeValueAsString(rootMap);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPropertyTypeCode(Object propertyValue){
        if(propertyValue instanceof String){
            return "String";
        }
        if(propertyValue instanceof Integer){
            return "Int";
        }
        if(propertyValue instanceof Long){
            return "Long";
        }
        if(propertyValue instanceof Double){
            return "Double";
        }
        if(propertyValue instanceof Float){
            return "Float";
        }
        if(propertyValue instanceof Boolean){
            return "Boolean";
        }
        if(propertyValue instanceof Date){
            return "Date";
        }
        if(propertyValue instanceof Short){
            return "Short";
        }
        return null;
    }

    public static boolean createTypeDate(String discoverSpaceName,String dataTypeKind,String dataTypeName,List<DataTypePropertyInfoVO> dataPropertiesList){
        DataImporter importer = new DataImporter(discoverSpaceName);
        String jsonContent=transferTypeDataToJson(dataTypeKind,dataTypeName,dataPropertiesList);
        try {
            importer.importData(jsonContent,true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static DataCRUDResultVO.operationResultCodeValue createTypeDataFromJSON(String discoverSpaceName, DataPayloadWrapperVO dataPayloadWrapper){
        DataImporter importer = new DataImporter(discoverSpaceName);
        try {
            ObjectMapper mapper=new ObjectMapper();
            String jsonDataContent=mapper.writeValueAsString(dataPayloadWrapper);

            System.out.println(jsonDataContent);
            System.out.println(jsonDataContent);

            importer.importData(jsonDataContent,true);
            return DataCRUDResultVO.operationResultCodeValue.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return DataCRUDResultVO.operationResultCodeValue.FAILURE;
        }
    }

    public static DataCRUDResultVO.operationResultCodeValue updateTypeDataFromJSON(String discoverSpaceName, DataPayloadWrapperVO dataPayloadWrapper){
        DataImporter importer = new DataImporter(discoverSpaceName);
        try {
           // importer.importData(jsonDataContent,true);
            return DataCRUDResultVO.operationResultCodeValue.SUCCESS;
        } catch (Exception e) {
           // System.out.println("Error Json Content: "+jsonDataContent);
            e.printStackTrace();
            return DataCRUDResultVO.operationResultCodeValue.FAILURE;
        }
    }

    public static DataCRUDResultVO.operationResultCodeValue deleteMeasurable(String discoverSpaceName, String measurableIdTypeKind, String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            boolean deleteResult=false;
            if(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION.equals(measurableIdTypeKind)){
                deleteResult=targetSpace.removeDimension(measurableIdTypeKind);
            }
            if(DiscoverSpaceOperationConstant.TYPEKIND_FACT.equals(measurableIdTypeKind)){
                deleteResult=targetSpace.removeFact(measurableIdTypeKind);
            }
            if(DiscoverSpaceOperationConstant.TYPEKIND_RELATION.equals(measurableIdTypeKind)){
                deleteResult=targetSpace.removeRelation(measurableIdTypeKind);
            }
            if(deleteResult){
                return DataCRUDResultVO.operationResultCodeValue.SUCCESS;
            }else{
                return DataCRUDResultVO.operationResultCodeValue.FAILURE;
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
            return DataCRUDResultVO.operationResultCodeValue.FAILURE;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }




}
