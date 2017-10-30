package com.infoDiscover.infoAnalyse.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoDiscover.infoAnalyse.service.restful.vo.*;
import com.infoDiscover.infoDiscoverEngine.dataMart.Dimension;
import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.Measurable;
import com.infoDiscover.infoDiscoverEngine.dataMart.Relation;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
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

    private static Map<String,Object> getPropertyValueMap( List<DataPropertyPayloadVO> propertyValuePayloadList){
        Map<String,Object> propertyValueMap=new HashMap<>();
        if(propertyValuePayloadList!=null){
            for(DataPropertyPayloadVO currentDataPropertyPayloadVO:propertyValuePayloadList){
                String propertyName=currentDataPropertyPayloadVO.getPropertyName();
                String propertyType=currentDataPropertyPayloadVO.getPropertyType();
                Object propertyValue=currentDataPropertyPayloadVO.getPropertyValue();
                if("String".equals(propertyType)){
                    propertyValueMap.put(propertyName,propertyValue.toString());
                }
                if("Int".equals(propertyType)){
                    propertyValueMap.put(propertyName,(Integer)propertyValue);
                }
                if("Long".equals(propertyType)){
                    Long targetPropertyValue=null;
                    if(propertyValue instanceof Integer){
                        targetPropertyValue=new Long((Integer)propertyValue);
                    }else if(propertyValue instanceof Long){
                        targetPropertyValue=(Long)propertyValue;
                    }
                    if(targetPropertyValue!=null){
                        propertyValueMap.put(propertyName,targetPropertyValue);
                    }
                }
                if("Double".equals(propertyType)){
                    propertyValueMap.put(propertyName,(Double)propertyValue);
                }
                if("Float".equals(propertyType)){
                    Float  targetPropertyValue=null;
                    if(propertyValue instanceof Double){
                        targetPropertyValue=new Float((Double)propertyValue);
                    }else if(propertyValue instanceof Float){
                        targetPropertyValue=(Float)propertyValue;
                    }
                    if(targetPropertyValue!=null){
                        propertyValueMap.put(propertyName,targetPropertyValue);
                    }
                }
                if("Boolean".equals(propertyType)){
                    propertyValueMap.put(propertyName,(Boolean)propertyValue);
                }
                if("Date".equals(propertyType)){
                    Date dateValue=new Date((Long)propertyValue);
                    propertyValueMap.put(propertyName,dateValue);
                }
                if("Short".equals(propertyType)){
                    Short targetPropertyValue=null;
                    if(propertyValue instanceof Integer){
                        targetPropertyValue=((Integer)propertyValue).shortValue();
                    }else if(propertyValue instanceof Short){
                        targetPropertyValue=(Short)propertyValue;
                    }
                    if(targetPropertyValue!=null){
                        propertyValueMap.put(propertyName,targetPropertyValue);
                    }
                }
            }
        }
        return propertyValueMap;
    }
/*
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
*/
    public static DataCRUDResultVO.operationResultCodeValue createTypeDataFromJSON(String discoverSpaceName, DataPayloadWrapperVO dataPayloadWrapper){
        DataImporter importer = new DataImporter(discoverSpaceName);
        try {
            ObjectMapper mapper=new ObjectMapper();
            String jsonDataContent=mapper.writeValueAsString(dataPayloadWrapper);
            importer.importData(jsonDataContent,false);
            return DataCRUDResultVO.operationResultCodeValue.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return DataCRUDResultVO.operationResultCodeValue.FAILURE;
        }
    }

    public static DataCRUDResultVO updateTypeData(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        String discoverSpaceName=dataCRUDInput.getDiscoverSpaceName();
        List<DataPayloadVO> dataPayLoadList=dataCRUDInput.getDataPayload().getData();
        int modifiedDataCount=0;
        if(dataPayLoadList!=null){
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for(DataPayloadVO currentDataPayload:dataPayLoadList){
                    String payloadType=currentDataPayload.getType();
                    //String currentPayloadTypeName=currentDataPayload.getTypeName();
                    String recordId=currentDataPayload.getRecordId();
                    if(recordId!=null){
                        List<DataPropertyPayloadVO> propertyList=currentDataPayload.getProperties();
                        if(propertyList!=null){
                            Map<String,Object> propertyValueMap=getPropertyValueMap(propertyList);
                            if(DiscoverSpaceOperationConstant.PAYLOAD_DIMENSION.equals(payloadType)){
                                Dimension targetDimension=targetSpace.getDimensionById(recordId);
                                targetDimension.addNewOrUpdateProperties(propertyValueMap);
                                modifiedDataCount++;
                            }
                            if(DiscoverSpaceOperationConstant.PAYLOAD_FACT.equals(payloadType)){
                                Fact targetFact=targetSpace.getFactById(recordId);
                                targetFact.addNewOrUpdateProperties(propertyValueMap);
                                modifiedDataCount++;
                            }
                            if(DiscoverSpaceOperationConstant.PAYLOAD_RELATION.equals(payloadType)){
                                Relation targetRelation=targetSpace.getRelationById(recordId);
                                targetRelation.addNewOrUpdateProperties(propertyValueMap);
                                modifiedDataCount++;
                            }
                        }
                    }
                }
                if(modifiedDataCount>0){
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.SUCCESS);
                    dataCRUDResultVO.setModifiedDataCount(modifiedDataCount);
                }else{
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.DATA_NOT_MODIFIED);
                }
            } catch (InfoDiscoveryEngineRuntimeException e) {
                e.printStackTrace();
                dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.FAILURE);
            } finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
        }else {
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }
        return dataCRUDResultVO;
    }

    public static DataCRUDResultVO deleteTypeData(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        String discoverSpaceName=dataCRUDInput.getDiscoverSpaceName();
        List<DataPayloadVO> dataPayLoadList=dataCRUDInput.getDataPayload().getData();
        int modifiedDataCount=0;
        if(dataPayLoadList!=null){
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for(DataPayloadVO currentDataPayload:dataPayLoadList){
                    String payloadType=currentDataPayload.getType();
                    String recordId=currentDataPayload.getRecordId();
                    if(recordId!=null){
                        if(DiscoverSpaceOperationConstant.PAYLOAD_DIMENSION.equals(payloadType)){
                            boolean removeResult=targetSpace.removeDimension(recordId);
                            if(removeResult){
                                modifiedDataCount++;
                            }
                        }
                        if(DiscoverSpaceOperationConstant.PAYLOAD_FACT.equals(payloadType)){
                            boolean removeResult=targetSpace.removeFact(recordId);
                            if(removeResult){
                                modifiedDataCount++;
                            }
                        }
                        if(DiscoverSpaceOperationConstant.PAYLOAD_RELATION.equals(payloadType)){
                            boolean removeResult=targetSpace.removeRelation(recordId);
                            if(removeResult){
                                modifiedDataCount++;
                            }
                        }
                    }
                }
                if(modifiedDataCount>0){
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.SUCCESS);
                    dataCRUDResultVO.setModifiedDataCount(modifiedDataCount);
                }else{
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.DATA_NOT_MODIFIED);
                }
            } catch (InfoDiscoveryEngineRuntimeException e) {
                e.printStackTrace();
                dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.FAILURE);
            } finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
        }else {
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }
        return dataCRUDResultVO;
    }

    public static DataCRUDResultVO deleteTypeDataProperties(DataCRUDInputVO dataCRUDInput){
        DataCRUDResultVO dataCRUDResultVO =new DataCRUDResultVO();
        String discoverSpaceName=dataCRUDInput.getDiscoverSpaceName();
        List<DataPayloadVO> dataPayLoadList=dataCRUDInput.getDataPayload().getData();
        int modifiedDataCount=0;
        if(dataPayLoadList!=null){
            InfoDiscoverSpace targetSpace=null;
            try {
                targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
                for(DataPayloadVO currentDataPayload:dataPayLoadList){
                    //String payloadType=currentDataPayload.getType();
                    String recordId=currentDataPayload.getRecordId();
                    String propertyNames=currentDataPayload.getPropertyNames();
                    if(recordId!=null&&propertyNames!=null){
                        Measurable targetMeasurable=targetSpace.getMeasurableById(recordId);
                        if(targetMeasurable!=null){
                            String[] propertyNameArray=propertyNames.split(",");
                            for(String currentProperty:propertyNameArray){
                                if(targetMeasurable.hasProperty(currentProperty)){
                                    boolean removeResult=targetMeasurable.removeProperty(currentProperty);
                                    if(removeResult){
                                        modifiedDataCount++;
                                    }
                                }
                            }
                        }
                    }
                }
                if(modifiedDataCount>0){
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.SUCCESS);
                    dataCRUDResultVO.setModifiedDataCount(modifiedDataCount);
                }else{
                    dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.DATA_NOT_MODIFIED);
                }
            } catch (InfoDiscoveryEngineRuntimeException e) {
                e.printStackTrace();
                dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.FAILURE);
            } finally {
                if(targetSpace!=null){
                    targetSpace.closeSpace();
                }
            }
        }else {
            dataCRUDResultVO.setOperationReturnCode(DataCRUDResultVO.operationResultCodeValue.INVALID_INPUT);
        }
        return dataCRUDResultVO;
    }
}
