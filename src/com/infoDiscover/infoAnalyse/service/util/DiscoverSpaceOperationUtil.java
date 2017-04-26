package com.infoDiscover.infoAnalyse.service.util;

import com.infoDiscover.infoAnalyse.service.restful.vo.*;
import com.infoDiscover.infoDiscoverEngine.dataMart.*;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.ExploreParameters;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationExplorer;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationFiltering.EqualFilteringItem;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.InformationType;
import com.infoDiscover.infoDiscoverEngine.dataWarehouse.SQLBuilder;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.InfoDiscoverEngineConstant;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineInfoExploreException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;
import com.infoDiscover.infoDiscoverEngine.util.helper.DataTypeStatisticMetrics;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticHelper;
import com.infoDiscover.infoDiscoverEngine.util.helper.DiscoverSpaceStatisticMetrics;

import java.util.*;

/**
 * Created by wangychu on 2/15/17.
 */
public class DiscoverSpaceOperationUtil {

    public static TypeInstanceRelationsCycleVO getRelationableRelationsCycleInfoById(String spaceName, String relationableId){
        TypeInstanceRelationsCycleVO typeInstanceRelationsCycleVO=new TypeInstanceRelationsCycleVO();
        List<RelationInfoVO> resultRelationValueList=new ArrayList<RelationInfoVO>();
        typeInstanceRelationsCycleVO.setRelationsInfo(resultRelationValueList);
        RelationableValueVO sourceTypeInstanceVO=new RelationableValueVO();
        typeInstanceRelationsCycleVO.setSourceTypeInstance(sourceTypeInstanceVO);

        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable= targetSpace.getMeasurableById(relationableId);
            List<Relation> relationsList=null;
            Relationable targetRelationable=null;

            if(targetMeasurable instanceof Dimension){
                targetRelationable=(Dimension)targetMeasurable;
                sourceTypeInstanceVO.setDiscoverSpaceName(spaceName);
                sourceTypeInstanceVO.setId(targetRelationable.getId());
                sourceTypeInstanceVO.setRelationableTypeName(((Dimension)targetRelationable).getType());
                sourceTypeInstanceVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,((Dimension)targetRelationable).getType()));
                sourceTypeInstanceVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
            }
            if(targetMeasurable instanceof Fact){
                targetRelationable=(Fact)targetMeasurable;
                sourceTypeInstanceVO.setDiscoverSpaceName(spaceName);
                sourceTypeInstanceVO.setId(targetRelationable.getId());
                sourceTypeInstanceVO.setRelationableTypeName(((Fact)targetRelationable).getType());
                sourceTypeInstanceVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,((Fact)targetRelationable).getType()));
                sourceTypeInstanceVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
            }
            if(targetRelationable!=null){
                relationsList= targetRelationable.getAllRelations();
            }
            if(relationsList!=null){
                for(Relation currentRelation:relationsList){
                    RelationInfoVO currentRelationValueVO=new RelationInfoVO();
                    currentRelationValueVO.setId(currentRelation.getId());
                    currentRelationValueVO.setRelationTypeName(currentRelation.getType());
                    currentRelationValueVO.setDiscoverSpaceName(spaceName);
                    currentRelationValueVO.setRelationTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_RELATION,(currentRelation.getType())));

                    Relationable fromRelationable=currentRelation.getFromRelationable();
                    if(fromRelationable!=null){
                        RelationableValueVO fromRelationableValueVO=new RelationableValueVO();
                        fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                        fromRelationableValueVO.setId(fromRelationable.getId());
                        if(fromRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,(dimensionRelationable.getType())));
                            fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                        }
                        if(fromRelationable instanceof Fact){
                            Fact factRelationable=(Fact)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,(factRelationable.getType())));
                            fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                    }

                    Relationable toRelationable=currentRelation.getToRelationable();
                    if(toRelationable!=null){
                        RelationableValueVO toRelationableValueVO=new RelationableValueVO();
                        toRelationableValueVO.setDiscoverSpaceName(spaceName);
                        toRelationableValueVO.setId(toRelationable.getId());
                        if(toRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,(dimensionRelationable.getType())));
                            toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                        }
                        if(toRelationable instanceof Fact){
                            Fact factRelationable=(Fact)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,(factRelationable.getType())));
                            toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                        }
                        currentRelationValueVO.setToRelationable(toRelationableValueVO);
                    }
                    resultRelationValueList.add(currentRelationValueVO);
                }
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return typeInstanceRelationsCycleVO;
    }

    public static TypeInstanceRelationsDetailVO getRelationableRelationsDetailInfoById(String spaceName, String relationableId){
        TypeInstanceRelationsDetailVO typeInstanceRelationsDetailVO=new TypeInstanceRelationsDetailVO();
        List<RelationInfoDetailVO> resultRelationValueList=new ArrayList<RelationInfoDetailVO>();
        typeInstanceRelationsDetailVO.setRelationsInfo(resultRelationValueList);
        RelationableValueDetailVO sourceTypeInstanceVO=new RelationableValueDetailVO();
        typeInstanceRelationsDetailVO.setSourceTypeInstance(sourceTypeInstanceVO);

        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Measurable targetMeasurable= targetSpace.getMeasurableById(relationableId);
            List<Relation> relationsList=null;
            Relationable targetRelationable=null;

            if(targetMeasurable instanceof Dimension){
                targetRelationable=(Dimension)targetMeasurable;
                sourceTypeInstanceVO.setDiscoverSpaceName(spaceName);
                sourceTypeInstanceVO.setId(targetRelationable.getId());
                sourceTypeInstanceVO.setRelationableTypeName(((Dimension)targetRelationable).getType());
                sourceTypeInstanceVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,((Dimension)targetRelationable).getType()));
                sourceTypeInstanceVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
            }
            if(targetMeasurable instanceof Fact){
                targetRelationable=(Fact)targetMeasurable;
                sourceTypeInstanceVO.setDiscoverSpaceName(spaceName);
                sourceTypeInstanceVO.setId(targetRelationable.getId());
                sourceTypeInstanceVO.setRelationableTypeName(((Fact)targetRelationable).getType());
                sourceTypeInstanceVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,((Fact)targetRelationable).getType()));
                sourceTypeInstanceVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
            }

            List measurableProperties=targetMeasurable.getProperties();
            if(measurableProperties!=null){
                List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,targetMeasurable,measurableProperties);
                sourceTypeInstanceVO.setPropertiesValueList(propertiesValueList);
            }

            if(targetRelationable!=null){
                relationsList= targetRelationable.getAllRelations();
            }
            if(relationsList!=null){
                for(Relation currentRelation:relationsList){
                    RelationInfoDetailVO currentRelationValueVO=new RelationInfoDetailVO();
                    currentRelationValueVO.setId(currentRelation.getId());
                    currentRelationValueVO.setRelationTypeName(currentRelation.getType());
                    currentRelationValueVO.setRelationTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_RELATION,currentRelation.getType()));
                    currentRelationValueVO.setDiscoverSpaceName(spaceName);

                    List relationProperties=currentRelation.getProperties();
                    if(relationProperties!=null){
                        List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,currentRelation,relationProperties);
                        currentRelationValueVO.setPropertiesValueList(propertiesValueList);
                    }

                    Relationable fromRelationable=currentRelation.getFromRelationable();
                    if(fromRelationable!=null){
                        RelationableValueDetailVO fromRelationableValueVO=new RelationableValueDetailVO();
                        fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                        fromRelationableValueVO.setId(fromRelationable.getId());
                        if(fromRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,dimensionRelationable.getType()));
                            fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                        }
                        if(fromRelationable instanceof Fact){
                            Fact factRelationable=(Fact)fromRelationable;
                            fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,factRelationable.getType()));
                            fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                        }

                        List fromProperties=fromRelationable.getProperties();
                        if(fromProperties!=null){
                            List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,fromRelationable,fromProperties);
                            fromRelationableValueVO.setPropertiesValueList(propertiesValueList);
                        }
                        currentRelationValueVO.setFromRelationable(fromRelationableValueVO);
                    }

                    Relationable toRelationable=currentRelation.getToRelationable();
                    if(toRelationable!=null){
                        RelationableValueDetailVO toRelationableValueVO=new RelationableValueDetailVO();
                        toRelationableValueVO.setDiscoverSpaceName(spaceName);
                        toRelationableValueVO.setId(toRelationable.getId());
                        if(toRelationable instanceof Dimension){
                            Dimension dimensionRelationable=(Dimension)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                            toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,dimensionRelationable.getType()));
                            toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                        }
                        if(toRelationable instanceof Fact){
                            Fact factRelationable=(Fact)toRelationable;
                            toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                            toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,factRelationable.getType()));
                            toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                        }
                        List toProperties=toRelationable.getProperties();
                        if(toProperties!=null){
                            List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,toRelationable,toProperties);
                            toRelationableValueVO.setPropertiesValueList(propertiesValueList);
                        }
                        currentRelationValueVO.setToRelationable(toRelationableValueVO);
                    }

                    resultRelationValueList.add(currentRelationValueVO);
                }
            }
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return typeInstanceRelationsDetailVO;
    }

    public static RelationInfoDetailVO getRelationDetailInfoById(String spaceName, String relationId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            Relation targetRelation=targetSpace.getRelationById(relationId);
            if(targetRelation==null){
                return null;
            }

            RelationInfoDetailVO targetRelationValueVO=new RelationInfoDetailVO();
            targetRelationValueVO.setId(targetRelation.getId());
            targetRelationValueVO.setRelationTypeName(targetRelation.getType());
            targetRelationValueVO.setRelationTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_RELATION,targetRelation.getType()));
            targetRelationValueVO.setDiscoverSpaceName(spaceName);

            List relationProperties=targetRelation.getProperties();
            if(relationProperties!=null){
                List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,targetRelation,relationProperties);
                targetRelationValueVO.setPropertiesValueList(propertiesValueList);
            }

            Relationable fromRelationable=targetRelation.getFromRelationable();
            if(fromRelationable!=null){
                RelationableValueDetailVO fromRelationableValueVO=new RelationableValueDetailVO();
                fromRelationableValueVO.setDiscoverSpaceName(spaceName);
                fromRelationableValueVO.setId(fromRelationable.getId());
                if(fromRelationable instanceof Dimension){
                    Dimension dimensionRelationable=(Dimension)fromRelationable;
                    fromRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                    fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,dimensionRelationable.getType()));
                    fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                }
                if(fromRelationable instanceof Fact){
                    Fact factRelationable=(Fact)fromRelationable;
                    fromRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                    fromRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,factRelationable.getType()));
                    fromRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                }

                List fromProperties=fromRelationable.getProperties();
                if(fromProperties!=null){
                    List<PropertyVO> propertiesValueList=loadMeasurablePropertyVOList(spaceName,fromRelationable,fromProperties);
                    fromRelationableValueVO.setPropertiesValueList(propertiesValueList);
                }
                targetRelationValueVO.setFromRelationable(fromRelationableValueVO);
            }

            Relationable toRelationable=targetRelation.getToRelationable();
            if(toRelationable!=null) {
                RelationableValueDetailVO toRelationableValueVO = new RelationableValueDetailVO();
                toRelationableValueVO.setDiscoverSpaceName(spaceName);
                toRelationableValueVO.setId(toRelationable.getId());
                if (toRelationable instanceof Dimension) {
                    Dimension dimensionRelationable = (Dimension) toRelationable;
                    toRelationableValueVO.setRelationableTypeName(dimensionRelationable.getType());
                    toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,dimensionRelationable.getType()));
                    toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
                }
                if (toRelationable instanceof Fact) {
                    Fact factRelationable = (Fact) toRelationable;
                    toRelationableValueVO.setRelationableTypeName(factRelationable.getType());
                    toRelationableValueVO.setRelationableTypeAliasName(getTypeKindAliasName(spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,factRelationable.getType()));
                    toRelationableValueVO.setRelationableTypeKind(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
                }
                List toProperties = toRelationable.getProperties();
                if (toProperties != null) {
                    List<PropertyVO> propertiesValueList = loadMeasurablePropertyVOList(spaceName,toRelationable,toProperties);
                    toRelationableValueVO.setPropertiesValueList(propertiesValueList);
                }
                targetRelationValueVO.setToRelationable(toRelationableValueVO);
            }
            return targetRelationValueVO;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static List<FactTypeInfoVO> getDiscoverSpaceFactTypesDataCountInfo(String spaceName){
        List<FactTypeInfoVO> factTypeInfoVOList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

            refreshTypeDefinitionInfo(targetSpace);

            List<String> factTypesList=targetSpace.getFactTypesList();
            if(factTypesList!=null) {
                DiscoverSpaceStatisticHelper discoverSpaceStatisticHelper = DiscoverEngineComponentFactory.getDiscoverSpaceStatisticHelper();
                DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics = discoverSpaceStatisticHelper.getDiscoverSpaceStatisticMetrics(spaceName);
                List<DataTypeStatisticMetrics> factsStatisticMetrics = discoverSpaceStatisticMetrics.getFactsStatisticMetrics();
                for(String currentFactTypeName:factTypesList){
                    FactTypeInfoVO currentFactTypeInfoVO=new FactTypeInfoVO();
                    currentFactTypeInfoVO.setTypeName(currentFactTypeName);
                    currentFactTypeInfoVO.setTypeDataRecordCount(getFactTypeRecordNumber(currentFactTypeName,factsStatisticMetrics));
                    factTypeInfoVOList.add(currentFactTypeInfoVO);
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return factTypeInfoVOList;
    }

    private static long getFactTypeRecordNumber(String targetFactTypeName, List<DataTypeStatisticMetrics> factsStatisticMetricsList ){
        for(DataTypeStatisticMetrics currentMetrics:factsStatisticMetricsList){
            String factTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_FACT,"");
            if(factTypeName.equals(targetFactTypeName)){
                return currentMetrics.getTypeDataCount();
            }
        }
        return 0;
    }

    public List<DimensionTypeInfoVO> getDiscoverSpaceDimensionTypesTreeDataCountInfo(String spaceName){
        List<DimensionTypeInfoVO> dimensionTypeInfoVOList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

            refreshTypeDefinitionInfo(targetSpace);

            List<String> rootDimensionTypesList=targetSpace.getRootDimensionTypesList();
            if(rootDimensionTypesList!=null){
                DiscoverSpaceStatisticHelper discoverSpaceStatisticHelper= DiscoverEngineComponentFactory.getDiscoverSpaceStatisticHelper();
                DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics=discoverSpaceStatisticHelper.getDiscoverSpaceStatisticMetrics(spaceName);
                List<DataTypeStatisticMetrics> dimensionsStatisticMetrics=discoverSpaceStatisticMetrics.getDimensionsStatisticMetrics();
                for(String currentDimensionTypeName:rootDimensionTypesList){
                    DimensionType currentDimensionType=targetSpace.getDimensionType(currentDimensionTypeName);
                    DimensionTypeInfoVO currentDimensionTypeInfoVO=new DimensionTypeInfoVO();
                    currentDimensionTypeInfoVO.setTypeName(currentDimensionTypeName);
                    currentDimensionTypeInfoVO.setDescendantDimensionTypesNumber(currentDimensionType.getDescendantDimensionTypes().size());
                    currentDimensionTypeInfoVO.setTypeDataRecordCount(getDimensionTypeRecordNumber(currentDimensionTypeName,dimensionsStatisticMetrics));
                    List<DimensionTypeInfoVO> childDimensionTypeInfoVOList=new ArrayList<>();
                    currentDimensionTypeInfoVO.setChildDimensionTypeInfosVOList(childDimensionTypeInfoVOList);
                    List<DimensionType> childDimensionTypeList=currentDimensionType.getChildDimensionTypes();
                    if(childDimensionTypeList!=null){
                        for(DimensionType currentRootDimensionType:childDimensionTypeList){
                            DimensionTypeInfoVO currentChildDimensionTypeInfoVO=getDimensionTypeInfoVO(currentRootDimensionType,dimensionsStatisticMetrics);
                            childDimensionTypeInfoVOList.add(currentChildDimensionTypeInfoVO);
                        }
                    }
                    dimensionTypeInfoVOList.add(currentDimensionTypeInfoVO);
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return dimensionTypeInfoVOList;
    }

    private static long getDimensionTypeRecordNumber(String targetDimensionTypeName, List<DataTypeStatisticMetrics> dimensionsStatisticMetricsList ){
        for(DataTypeStatisticMetrics currentMetrics:dimensionsStatisticMetricsList){
            String dimensionTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_DIMENSION,"");
            if(dimensionTypeName.equals(targetDimensionTypeName)){
                return currentMetrics.getTypeDataCount();
            }
        }
        return 0;
    }

    private static DimensionTypeInfoVO getDimensionTypeInfoVO(DimensionType targetDimensionType,List<DataTypeStatisticMetrics> dimensionsStatisticMetrics){
        DimensionTypeInfoVO currentDimensionTypeInfoVO=new DimensionTypeInfoVO();
        currentDimensionTypeInfoVO.setTypeName(targetDimensionType.getTypeName());
        currentDimensionTypeInfoVO.setDescendantDimensionTypesNumber(targetDimensionType.getDescendantDimensionTypes().size());
        currentDimensionTypeInfoVO.setTypeDataRecordCount(getDimensionTypeRecordNumber(targetDimensionType.getTypeName(),dimensionsStatisticMetrics));
        List<DimensionTypeInfoVO> childDimensionTypeInfoVOList=new ArrayList<>();
        currentDimensionTypeInfoVO.setChildDimensionTypeInfosVOList(childDimensionTypeInfoVOList);
        List<DimensionType> childDimensionTypeList=targetDimensionType.getChildDimensionTypes();
        if(childDimensionTypeList!=null){
            for(DimensionType currentChildDimensionType:childDimensionTypeList){
                DimensionTypeInfoVO currentChildDimensionTypeInfoVO=getDimensionTypeInfoVO(currentChildDimensionType,dimensionsStatisticMetrics);
                childDimensionTypeInfoVOList.add(currentChildDimensionTypeInfoVO);
            }
        }
        return currentDimensionTypeInfoVO;
    }

    public static List<RelationTypeInfoVO> getDiscoverSpaceRelationTypesTreeDataCountInfo(String spaceName){
        List<RelationTypeInfoVO> relationTypeInfoVOList=new ArrayList<>();
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);

            refreshTypeDefinitionInfo(targetSpace);

            List<String> rootRelationTypesList=targetSpace.getRootRelationTypesList();
            if(rootRelationTypesList!=null){
                DiscoverSpaceStatisticHelper discoverSpaceStatisticHelper= DiscoverEngineComponentFactory.getDiscoverSpaceStatisticHelper();
                DiscoverSpaceStatisticMetrics discoverSpaceStatisticMetrics=discoverSpaceStatisticHelper.getDiscoverSpaceStatisticMetrics(spaceName);
                List<DataTypeStatisticMetrics> relationsStatisticMetrics=discoverSpaceStatisticMetrics.getRelationsStatisticMetrics();

                for(String currentRelationTypeName:rootRelationTypesList){
                    RelationType currentRelationType=targetSpace.getRelationType(currentRelationTypeName);
                    RelationTypeInfoVO currentRelationTypeInfoVO=new RelationTypeInfoVO();
                    currentRelationTypeInfoVO.setTypeName(currentRelationTypeName);
                    currentRelationTypeInfoVO.setDescendantRelationTypesNumber(currentRelationType.getDescendantRelationTypes().size());
                    currentRelationTypeInfoVO.setTypeDataRecordCount(getRelationTypeRecordNumber(currentRelationTypeName,relationsStatisticMetrics));
                    List<RelationTypeInfoVO> childRelationTypeInfoVOList=new ArrayList<>();
                    currentRelationTypeInfoVO.setChildRelationTypeInfosVOList(childRelationTypeInfoVOList);
                    List<RelationType> childRelationTypeList=currentRelationType.getChildRelationTypes();
                    if(childRelationTypeList!=null){
                        for(RelationType currentRootRelationType:childRelationTypeList){
                            RelationTypeInfoVO currentChildRelationTypeInfoVO=getRelationTypeInfoVO(currentRootRelationType,relationsStatisticMetrics);
                            childRelationTypeInfoVOList.add(currentChildRelationTypeInfoVO);
                        }
                    }
                    relationTypeInfoVOList.add(currentRelationTypeInfoVO);
                }
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return relationTypeInfoVOList;
    }

    private static long getRelationTypeRecordNumber(String targetRelationTypeName, List<DataTypeStatisticMetrics> relationsStatisticMetricsList ){
        for(DataTypeStatisticMetrics currentMetrics:relationsStatisticMetricsList){
            String relationTypeName=currentMetrics.getDataTypeName().replaceFirst(InfoDiscoverEngineConstant.CLASSPERFIX_RELATION,"");
            if(relationTypeName.equals(targetRelationTypeName)){
                return currentMetrics.getTypeDataCount();
            }
        }
        return 0;
    }

    private static RelationTypeInfoVO getRelationTypeInfoVO(RelationType targetRelationType,List<DataTypeStatisticMetrics> relationsStatisticMetrics){
        RelationTypeInfoVO currentRelationTypeInfoVO=new RelationTypeInfoVO();
        currentRelationTypeInfoVO.setTypeName(targetRelationType.getTypeName());
        currentRelationTypeInfoVO.setDescendantRelationTypesNumber(targetRelationType.getDescendantRelationTypes().size());
        currentRelationTypeInfoVO.setTypeDataRecordCount(getRelationTypeRecordNumber(targetRelationType.getTypeName(),relationsStatisticMetrics));
        List<RelationTypeInfoVO> childRelationTypeInfoVOList=new ArrayList<>();
        currentRelationTypeInfoVO.setChildRelationTypeInfosVOList(childRelationTypeInfoVOList);
        List<RelationType> childRelationTypeList=targetRelationType.getChildRelationTypes();
        if(childRelationTypeList!=null){
            for(RelationType currentChildRelationType:childRelationTypeList){
                RelationTypeInfoVO currentChildRelationTypeInfoVO=getRelationTypeInfoVO(currentChildRelationType,relationsStatisticMetrics);
                childRelationTypeInfoVOList.add(currentChildRelationTypeInfoVO);
            }
        }
        return currentRelationTypeInfoVO;
    }

    private static void refreshTypeDefinitionInfo(InfoDiscoverSpace targetSpace) throws InfoDiscoveryEngineDataMartException {
        /* Workaround start - used to commit and reopen transaction, so that can get Type data changed by other applications*/
        String tempRelationTypeName="tempRelationType"+new Date().getTime();
        targetSpace.addDimensionType(tempRelationTypeName);
        targetSpace.removeDimensionType(tempRelationTypeName);
        /* Workaround finish */
    }

    private static List<TypePropertyVO> loadTypePropertyVOList(List<TypeProperty> typePropertyList){
        List<TypePropertyVO> typePropertyVOList=new ArrayList<>();
        if(typePropertyList!=null){
            for(TypeProperty currentTypeProperty:typePropertyList){
                TypePropertyVO currentTypePropertyVO=new TypePropertyVO();
                currentTypePropertyVO.setPropertyName(currentTypeProperty.getPropertyName());
                currentTypePropertyVO.setPropertyType(""+currentTypeProperty.getPropertyType());
                currentTypePropertyVO.setPropertySourceOwner(currentTypeProperty.getPropertySourceOwner());
                currentTypePropertyVO.setMandatoryProperty(currentTypeProperty.isMandatory());
                currentTypePropertyVO.setNullableProperty(currentTypeProperty.isNullable());
                currentTypePropertyVO.setReadOnlyProperty(currentTypeProperty.isReadOnly());
                typePropertyVOList.add(currentTypePropertyVO);
            }
        }
        return typePropertyVOList;
    }

    public static DimensionTypeSummaryVO getDimensionTypeDetail(String spaceName,String dimensionTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean dimensionTypeExist=targetSpace.hasDimensionType(dimensionTypeName);
            if(!dimensionTypeExist){
                refreshTypeDefinitionInfo(targetSpace);
            }
            dimensionTypeExist=targetSpace.hasDimensionType(dimensionTypeName);
            if(dimensionTypeExist){
                DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                List<TypeProperty> typePropertyList=targetDimensionType.getTypeProperties();
                long allDataCount=targetDimensionType.countContainedDimensions(true);
                long selfDataCount=targetDimensionType.countContainedDimensions(false);

                DimensionTypeSummaryVO typePropertyableVO=new DimensionTypeSummaryVO();
                typePropertyableVO.setTypeName(targetDimensionType.getTypeName());
                typePropertyableVO.setDimensionSelfDataCount(selfDataCount);
                typePropertyableVO.setDimensionDescendantDataCount(allDataCount-selfDataCount);

                DimensionType parentDimensionType=targetDimensionType.getParentDimensionType();
                if(parentDimensionType!=null) {
                    typePropertyableVO.setParentDimensionTypeName(parentDimensionType.getTypeName());
                }

                List<String> childDimensionTypesList=new ArrayList<>();
                typePropertyableVO.setChildDimensionTypes(childDimensionTypesList);

                List<DimensionType> childDimensionTypeList=targetDimensionType.getChildDimensionTypes();
                if(childDimensionTypeList!=null){
                    for(DimensionType currentDimensionType:childDimensionTypeList){
                        childDimensionTypesList.add(currentDimensionType.getTypeName());

                    }
                }
                typePropertyableVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));
                return typePropertyableVO;
            }else{
                return null;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static FactTypeSummaryVO getFactTypeDetail(String spaceName,String factTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean factTypeExist=targetSpace.hasFactType(factTypeName);
            if(!factTypeExist){
                refreshTypeDefinitionInfo(targetSpace);
            }
            factTypeExist=targetSpace.hasFactType(factTypeName);
            if(factTypeExist){
                FactType targetFactType=targetSpace.getFactType(factTypeName);
                List<TypeProperty> typePropertyList=targetFactType.getTypeProperties();
                long allDataCount=targetFactType.countContainedFacts();

                FactTypeSummaryVO typePropertyableVO=new FactTypeSummaryVO();
                typePropertyableVO.setTypeName(targetFactType.getTypeName());
                typePropertyableVO.setFactDataCount(allDataCount);
                typePropertyableVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));
                return typePropertyableVO;

            }else{
                return null;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static RelationTypeSummaryVO getRelationTypeDetail(String spaceName,String relationTypeName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean relationTypeExist=targetSpace.hasRelationType(relationTypeName);
            if(!relationTypeExist){
                refreshTypeDefinitionInfo(targetSpace);
            }
            relationTypeExist=targetSpace.hasRelationType(relationTypeName);
            if(relationTypeExist){
                RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
                List<TypeProperty> typePropertyList=targetRelationType.getTypeProperties();
                long allDataCount=targetRelationType.countContainedRelations(true);
                long selfDataCount=targetRelationType.countContainedRelations(false);

                RelationTypeSummaryVO typePropertyableVO=new RelationTypeSummaryVO();
                typePropertyableVO.setTypeName(targetRelationType.getTypeName());
                typePropertyableVO.setRelationSelfDataCount(selfDataCount);
                typePropertyableVO.setRelationDescendantDataCount(allDataCount-selfDataCount);

                RelationType parentRelationType=targetRelationType.getParentRelationType();
                if(parentRelationType!=null) {
                    typePropertyableVO.setParentRelationTypeName(parentRelationType.getTypeName());
                }

                List<String> childRelationTypesList=new ArrayList<>();
                typePropertyableVO.setChildRelationTypes(childRelationTypesList);

                List<RelationType> childRelationTypeList=targetRelationType.getChildRelationTypes();
                if(childRelationTypeList!=null){
                    for(RelationType currentRelationType:childRelationTypeList){
                        childRelationTypesList.add(currentRelationType.getTypeName());
                    }
                }
                typePropertyableVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));
                return typePropertyableVO;
            }else{
                return null;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    private static MeasurableVO getMeasurableVO(String spaceName,Measurable typeMeasurable){
        MeasurableVO currentMeasurableVO=new MeasurableVO();
        List<Property> currentMeasurableProperties=typeMeasurable.getProperties();
        currentMeasurableVO.setMeasurableProperties(loadMeasurablePropertyVOList(spaceName,typeMeasurable,currentMeasurableProperties));
        return currentMeasurableVO;
    }

    public static MeasurableQueryResultSetVO queryDimensionTypeData(String spaceName,String dimensionTypeName) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(dimensionTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);

                DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetDimensionType.countContainedDimensions(true));

                List<TypeProperty> typePropertyList=targetDimensionType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(dimensionTypeName);

                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);

                List<Dimension> resultDimensionList=ie.discoverDimensions(ep);

                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultDimensionList!=null){
                    for(Measurable currentMeasurable:resultDimensionList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);

                String sql= SQLBuilder.buildQuerySQL(InformationType.DIMENSION, ep);
                measurableQueryResultSetVO.setQuerySQL(sql);

                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static MeasurableQueryResultSetVO queryDimensionDataByQuerySQL(String spaceName,String dimensionTypeName,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(dimensionTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);

                DimensionType targetDimensionType=targetSpace.getDimensionType(dimensionTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetDimensionType.countContainedDimensions(true));

                List<TypeProperty> typePropertyList=targetDimensionType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.DIMENSION,dimensionTypeName,querySQL);
                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultMeasurableList!=null){
                    for(Measurable currentMeasurable:resultMeasurableList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);
                measurableQueryResultSetVO.setQuerySQL(querySQL);
                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static MeasurableQueryResultSetVO queryRelationTypeData(String spaceName,String relationTypeName) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(relationTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_RELATION);

                RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetRelationType.countContainedRelations(true));

                List<TypeProperty> typePropertyList=targetRelationType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(relationTypeName);

                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);

                List<Relation> resultRelationList=ie.discoverRelations(ep);

                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultRelationList!=null){
                    for(Measurable currentMeasurable:resultRelationList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);

                String sql= SQLBuilder.buildQuerySQL(InformationType.RELATION, ep);
                measurableQueryResultSetVO.setQuerySQL(sql);

                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static MeasurableQueryResultSetVO queryRelationDataByQuerySQL(String spaceName,String relationTypeName,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(relationTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_RELATION);

                RelationType targetRelationType=targetSpace.getRelationType(relationTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetRelationType.countContainedRelations(true));

                List<TypeProperty> typePropertyList=targetRelationType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.RELATION,relationTypeName,querySQL);
                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultMeasurableList!=null){
                    for(Measurable currentMeasurable:resultMeasurableList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);
                measurableQueryResultSetVO.setQuerySQL(querySQL);
                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static MeasurableQueryResultSetVO queryFactTypeData(String spaceName,String factTypeName) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(factTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_FACT);

                FactType targetFactType=targetSpace.getFactType(factTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetFactType.countContainedFacts());

                List<TypeProperty> typePropertyList=targetFactType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(factTypeName);

                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);

                List<Fact> resultFactList=ie.discoverFacts(ep);

                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultFactList!=null){
                    for(Measurable currentMeasurable:resultFactList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);

                String sql= SQLBuilder.buildQuerySQL(InformationType.FACT, ep);
                measurableQueryResultSetVO.setQuerySQL(sql);

                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static MeasurableQueryResultSetVO queryFactDataByQuerySQL(String spaceName,String factTypeName,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                return null;
            }else{
                MeasurableQueryResultSetVO measurableQueryResultSetVO=new MeasurableQueryResultSetVO();
                measurableQueryResultSetVO.setCurrentPage(1);
                measurableQueryResultSetVO.setPageSize(-1);
                measurableQueryResultSetVO.setDiscoverSpaceName(spaceName);
                measurableQueryResultSetVO.setMeasurableName(factTypeName);
                measurableQueryResultSetVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_FACT);

                FactType targetFactType=targetSpace.getFactType(factTypeName);
                measurableQueryResultSetVO.setMeasurableRecordCount(targetFactType.countContainedFacts());

                List<TypeProperty> typePropertyList=targetFactType.getTypeProperties();
                measurableQueryResultSetVO.setTypeProperties(loadTypePropertyVOList(typePropertyList));

                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.FACT,factTypeName,querySQL);
                List<MeasurableVO> resultMeasurableVOList=new ArrayList<>();
                if(resultMeasurableList!=null){
                    for(Measurable currentMeasurable:resultMeasurableList){
                        MeasurableVO measurableVO =getMeasurableVO(spaceName,currentMeasurable);
                        resultMeasurableVOList.add(measurableVO);
                    }
                }
                measurableQueryResultSetVO.setMeasurableValues(resultMeasurableVOList);
                measurableQueryResultSetVO.setQuerySQL(querySQL);
                return measurableQueryResultSetVO;
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    private static List<PropertyVO> loadMeasurablePropertyVOList(String spaceName,Measurable targetMeasurable,List<Property> measurableProperties){
        List<PropertyVO> currentMeasurableVOPropertiesList=new ArrayList<>();
        if(measurableProperties!=null){
            for(Property currentProperty:measurableProperties){
                PropertyVO currentPropertyVO=new PropertyVO();
                currentMeasurableVOPropertiesList.add(currentPropertyVO);

                currentPropertyVO.setPropertyType(""+currentProperty.getPropertyType());
                currentPropertyVO.setPropertyName(currentProperty.getPropertyName());
                if(targetMeasurable instanceof Dimension){
                    currentPropertyVO.setPropertyAliasName(getTypePropertyAliasName(
                            spaceName,DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION,((Dimension) targetMeasurable).getType(),currentProperty.getPropertyName()));
                }
                if(targetMeasurable instanceof Fact){
                    currentPropertyVO.setPropertyAliasName(getTypePropertyAliasName(
                            spaceName,DiscoverSpaceOperationConstant.TYPEKIND_FACT,((Fact) targetMeasurable).getType(),currentProperty.getPropertyName()));
                }
                if(targetMeasurable instanceof Relation){
                    currentPropertyVO.setPropertyAliasName(getTypePropertyAliasName(
                            spaceName,DiscoverSpaceOperationConstant.TYPEKIND_RELATION,((Relation) targetMeasurable).getType(),currentProperty.getPropertyName()));
                }

                Object currentPropertyValue=currentProperty.getPropertyValue();
                switch(currentProperty.getPropertyType()) {
                    case DATE:
                        Date propertyValue=(Date)currentPropertyValue;
                        currentPropertyVO.setPropertyValue(""+propertyValue.getTime());
                        break;
                    default:
                        currentPropertyVO.setPropertyValue(currentPropertyValue.toString());
                        break;
                }
            }
        }
        return currentMeasurableVOPropertiesList;
    }

    public static MeasurableInstanceDetailInfoVO getMeasurableInstanceDetailInfo(String spaceName,String measurableId){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            return getMeasurableInstanceDetailInfoVO(targetSpace,measurableId);
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static MeasurableInstanceDetailInfoVO getMeasurableInstanceDetailInfoVO(InfoDiscoverSpace targetSpace,String measurableId){
        Measurable targetMeasurable=targetSpace.getMeasurableById(measurableId);
        if(targetMeasurable==null){
            return null;
        }
        MeasurableInstanceDetailInfoVO measurableInstanceDetailInfoVO=new MeasurableInstanceDetailInfoVO();
        measurableInstanceDetailInfoVO.setDiscoverSpaceName(targetSpace.getSpaceName());
        measurableInstanceDetailInfoVO.setMeasurableId(measurableId);

        List<Property> currentMeasurableProperties=targetMeasurable.getProperties();
        measurableInstanceDetailInfoVO.setMeasurableProperties(loadMeasurablePropertyVOList(targetSpace.getSpaceName(),targetMeasurable,currentMeasurableProperties));

        if(targetMeasurable instanceof Fact){
            measurableInstanceDetailInfoVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_FACT);
            String measurableType=((Fact)targetMeasurable).getType();
            measurableInstanceDetailInfoVO.setMeasurableName(measurableType);

            FactType measurableFactType=targetSpace.getFactType(measurableType);
            List<TypeProperty> typePropertyList=measurableFactType.getTypeProperties();
            measurableInstanceDetailInfoVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));

        }
        if(targetMeasurable instanceof Dimension){
            measurableInstanceDetailInfoVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_DIMENSION);
            String measurableType=((Dimension)targetMeasurable).getType();
            measurableInstanceDetailInfoVO.setMeasurableName(measurableType);

            DimensionType measurableDimensionType=targetSpace.getDimensionType(measurableType);
            List<TypeProperty> typePropertyList=measurableDimensionType.getTypeProperties();
            measurableInstanceDetailInfoVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));

        }
        if(targetMeasurable instanceof Relation){
            measurableInstanceDetailInfoVO.setMeasurableType(DiscoverSpaceOperationConstant.TYPEKIND_RELATION);
            String measurableType=((Relation)targetMeasurable).getType();
            measurableInstanceDetailInfoVO.setMeasurableName(measurableType);

            RelationType measurableRelationType=targetSpace.getRelationType(measurableType);
            List<TypeProperty> typePropertyList=measurableRelationType.getTypeProperties();
            measurableInstanceDetailInfoVO.setTypePropertyList(loadTypePropertyVOList(typePropertyList));
        }
        return measurableInstanceDetailInfoVO;
    }

    public static List<MeasurableInstanceDetailInfoVO> getMeasurableInstancesDetailInfo(String spaceName,String measurableIds){
        InfoDiscoverSpace targetSpace=null;
        List<MeasurableInstanceDetailInfoVO> measurableInstanceDetailInfoVOList=new ArrayList<>();
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            String[] measurableIdArray=measurableIds.split(",");
            for(String currentIdString:measurableIdArray){
                MeasurableInstanceDetailInfoVO currentMeasurableInstanceDetailInfoVO=getMeasurableInstanceDetailInfoVO(targetSpace,currentIdString);
                if(currentMeasurableInstanceDetailInfoVO!=null){
                    measurableInstanceDetailInfoVOList.add(currentMeasurableInstanceDetailInfoVO);
                }
            }
            return measurableInstanceDetailInfoVOList;
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static String generateFactTypePropertiesCSV(String spaceName,String factTypeName,String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(factTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Fact> resultFactList=ie.discoverFacts(ep);
                return generateMeasurableTypePropertiesCSV(resultFactList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static String generateDimensionTypePropertiesCSV(String spaceName,String dimensionTypeName,String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(dimensionTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Dimension> resultDimensionList=ie.discoverDimensions(ep);
                return generateMeasurableTypePropertiesCSV(resultDimensionList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static String generateRelationTypePropertiesCSV(String spaceName,String relationTypeName,String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(relationTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Relation> resultRelationList=ie.discoverRelations(ep);
                return generateMeasurableTypePropertiesCSV(resultRelationList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    private static String generateMeasurableTypePropertiesCSV(List<? extends Measurable> resultMeasurableList,String properties){
        String[] propertyNameList=properties.split(",");
        if(propertyNameList.length==0){
            return null;
        }
        String titleLineValue=null;
        for(int i=0;i<propertyNameList.length;i++){
            String currentProperty=propertyNameList[i];
            if(i!=0){
                titleLineValue=titleLineValue+","+currentProperty;
            }else{
                titleLineValue=currentProperty;
            }
        }
        StringBuffer csvStringBuffer=new StringBuffer();
        csvStringBuffer.append(titleLineValue+"\r");

        if(resultMeasurableList!=null){
            String currentString=null;
            for(Measurable currentMeasurable:resultMeasurableList){
                for(int i=0;i<propertyNameList.length;i++){
                    String currentPropertyName=propertyNameList[i];
                    Property currentProperty=currentMeasurable.getProperty(currentPropertyName);
                    if(currentProperty==null){
                        currentString=currentString+",";
                    }else {
                        if (currentProperty.getPropertyType().equals(PropertyType.DATE)) {
                            Date propertyValue = (Date) currentProperty.getPropertyValue();
                            if (i == 0) {
                                currentString = "" + propertyValue.getTime();
                            } else {
                                currentString = currentString + "," + propertyValue.getTime();
                            }
                        } else {
                            if (i == 0) {
                                currentString = currentProperty.getPropertyValue().toString();
                            } else {
                                currentString = currentString + "," + currentProperty.getPropertyValue().toString();
                            }
                        }
                    }
                }
                csvStringBuffer.append(currentString+"\r");
            }
        }
        return csvStringBuffer.toString();
    }

    public static List<Map<String,String>> generateFactTypePropertiesJSON(String spaceName, String factTypeName, String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(factTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Fact> resultFactList=ie.discoverFacts(ep);
                return generateMeasurableTypePropertiesJSON(resultFactList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<Map<String,String>> generateFactTypePropertiesJSONByQuerySQL(String spaceName, String factTypeName, String properties,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasFactType(factTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.FACT,factTypeName,querySQL);
                return generateMeasurableTypePropertiesJSON(resultMeasurableList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<Map<String,String>> generateDimensionTypePropertiesJSON(String spaceName,String dimensionTypeName,String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(dimensionTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Dimension> resultDimensionList=ie.discoverDimensions(ep);
                return generateMeasurableTypePropertiesJSON(resultDimensionList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<Map<String,String>> generateDimensionTypePropertiesJSONByQuerySQL(String spaceName,String dimensionTypeName,String properties,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasDimensionType(dimensionTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.DIMENSION,dimensionTypeName,querySQL);
                return generateMeasurableTypePropertiesJSON(resultMeasurableList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<Map<String,String>> generateRelationTypePropertiesJSON(String spaceName,String relationTypeName,String properties) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                ExploreParameters ep=new ExploreParameters();
                ep.setType(relationTypeName);
                ep.setPageSize(1000);
                ep.setStartPage(1);
                ep.setEndPage(6);
                List<Relation> resultRelationList=ie.discoverRelations(ep);
                return generateMeasurableTypePropertiesJSON(resultRelationList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    public static List<Map<String,String>> generateRelationTypePropertiesJSONByQuerySQL(String spaceName,String relationTypeName,String properties,String querySQL) {
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(spaceName);
            boolean hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                refreshTypeDefinitionInfo(targetSpace);
            }
            hasTargetType=targetSpace.hasRelationType(relationTypeName);
            if(!hasTargetType){
                return null;
            }else{
                InformationExplorer ie=targetSpace.getInformationExplorer();
                List<Measurable> resultMeasurableList= ie.discoverMeasurablesByQuerySQL(InformationType.RELATION,relationTypeName,querySQL);
                return generateMeasurableTypePropertiesJSON(resultMeasurableList,properties);
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
        return null;
    }

    private static List<Map<String,String>> generateMeasurableTypePropertiesJSON(List<? extends Measurable> resultMeasurableList,String properties){
        String[] propertyNameList=properties.split(",");
        if(propertyNameList.length==0){
            return null;
        }
        String titleLineValue=null;
        for(int i=0;i<propertyNameList.length;i++){
            String currentProperty=propertyNameList[i];
            if(i!=0){
                titleLineValue=titleLineValue+","+currentProperty;
            }else{
                titleLineValue=currentProperty;
            }
        }
        List<Map<String,String>> resultDataList=new ArrayList<>();
        if(resultMeasurableList!=null){
            for(Measurable currentMeasurable:resultMeasurableList){
                Map<String,String> valueMap=new HashMap<>();
                resultDataList.add(valueMap);
                for(int i=0;i<propertyNameList.length;i++){
                    String currentPropertyName=propertyNameList[i];
                    Property currentProperty=currentMeasurable.getProperty(currentPropertyName);
                    if(currentProperty==null){
                        valueMap.put(currentPropertyName,null);
                    }else {
                        if (currentProperty.getPropertyType().equals(PropertyType.DATE)) {
                            Date propertyValue = (Date) currentProperty.getPropertyValue();
                            valueMap.put(currentPropertyName,"" + propertyValue.getTime());
                        } else {
                            valueMap.put(currentPropertyName,currentProperty.getPropertyValue().toString());
                        }
                    }
                }
            }
        }
        return resultDataList;
    }

    // get type and property alias name logic
    public static final String TYPEKIND_AliasNameFactType="TypeKind_AliasName";
    public static final String TYPEPROPERTY_AliasNameFactType="TypeProperty_AliasName";
    public static final String MetaConfig_PropertyName_DiscoverSpace="discoverSpace";
    public static final String MetaConfig_PropertyName_TypeKind="typeKind";
    public static final String MetaConfig_PropertyName_TypeName="typeName";
    public static final String MetaConfig_PropertyName_TypeAliasName="typeAliasName";
    public static final String MetaConfig_PropertyName_TypePropertyName="typePropertyName";
    public static final String MetaConfig_PropertyName_TypePropertyAliasName="typePropertyAliasName";

    private static HashMap<String,String> TYPEKIND_AliasNameMap=new HashMap<>();
    private static HashMap<String,String> TypeProperty_AliasNameMap=new HashMap<>();

    private static String getTypeKindAliasName(String spaceName,String typeKind,String typeName){
        String typeKindRecordKey=spaceName+"_"+typeKind+"_"+typeName;
        if(TYPEKIND_AliasNameMap.get(typeKindRecordKey)!=null){
            return TYPEKIND_AliasNameMap.get(typeKindRecordKey);
        }
        String metaConfigSpaceName = InfoAnalyseServicePropertyHandler.getPropertyValue(InfoAnalyseServicePropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEKIND_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typeAliasRecordFactsList = ie.discoverFacts(typeKindRecordEP);
            if(typeAliasRecordFactsList!=null) {
                if(typeAliasRecordFactsList.size()>0){
                    Fact targetFact=typeAliasRecordFactsList.get(0);
                    String typeKindAliasName=targetFact.getProperty(MetaConfig_PropertyName_TypeAliasName).getPropertyValue().toString();
                    TYPEKIND_AliasNameMap.put(typeKindRecordKey,typeKindAliasName);
                    return typeKindAliasName;
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return null;
    }

    private static String getTypePropertyAliasName(String spaceName,String typeKind,String typeName,String typePropertyName) {
        String propertyRecordKey=spaceName+"_"+typeKind+"_"+typeName+"_"+typePropertyName;
        if(TypeProperty_AliasNameMap.get(propertyRecordKey)!=null){
            return TypeProperty_AliasNameMap.get(propertyRecordKey);
        }
        String metaConfigSpaceName = InfoAnalyseServicePropertyHandler.getPropertyValue(InfoAnalyseServicePropertyHandler.META_CONFIG_DISCOVERSPACE);
        InfoDiscoverSpace metaConfigSpace = null;
        try {
            metaConfigSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(metaConfigSpaceName);
            ExploreParameters typeKindRecordEP = new ExploreParameters();
            typeKindRecordEP.setType(TYPEPROPERTY_AliasNameFactType);
            typeKindRecordEP.setDefaultFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_DiscoverSpace, spaceName));
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeKind, typeKind), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypeName, typeName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.addFilteringItem(new EqualFilteringItem(MetaConfig_PropertyName_TypePropertyName, typePropertyName), ExploreParameters.FilteringLogic.AND);
            typeKindRecordEP.setResultNumber(1);
            InformationExplorer ie = metaConfigSpace.getInformationExplorer();
            List<Fact> typePropertyAliasRecordFact = ie.discoverFacts(typeKindRecordEP);
            if(typePropertyAliasRecordFact!=null) {
                if(typePropertyAliasRecordFact.size()>0){
                    Fact targetFact=typePropertyAliasRecordFact.get(0);
                    String typeKindAliasName=targetFact.getProperty(MetaConfig_PropertyName_TypePropertyAliasName).getPropertyValue().toString();
                    TypeProperty_AliasNameMap.put(propertyRecordKey,typeKindAliasName);
                    return typeKindAliasName;
                }
            }
        } catch (InfoDiscoveryEngineInfoExploreException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        }finally {
            if(metaConfigSpace!=null){
                metaConfigSpace.closeSpace();
            }
        }
        return null;
    }
}
