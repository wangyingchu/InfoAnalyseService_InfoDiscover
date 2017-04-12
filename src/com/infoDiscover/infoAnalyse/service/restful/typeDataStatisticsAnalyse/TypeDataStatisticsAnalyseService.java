package com.infoDiscover.infoAnalyse.service.restful.typeDataStatisticsAnalyse;

import com.infoDiscover.infoAnalyse.service.restful.vo.MeasurableQueryResultSetVO;
import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationUtil;

import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by wangychu on 3/6/17.
 */

@Path("/typeDataStatisticsAnalyseService")
@Produces("application/json")
public class TypeDataStatisticsAnalyseService {

    @GET
    @Path("/dimensionTypeDataList/{discoverSpaceName}/{dimensionTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO getDimensionTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("dimensionTypeName")String dimensionTypeName){
        return DiscoverSpaceOperationUtil.queryDimensionTypeData(discoverSpaceName,dimensionTypeName);
    }

    @POST
    @Path("/dimensionTypeDataList/{discoverSpaceName}/{dimensionTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO postDimensionTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("dimensionTypeName")String dimensionTypeName,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.queryDimensionDataByQuerySQL(discoverSpaceName,dimensionTypeName,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/relationTypeDataList/{discoverSpaceName}/{relationTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO getRelationTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationTypeName")String relationTypeName){
        return DiscoverSpaceOperationUtil.queryRelationTypeData(discoverSpaceName,relationTypeName);
    }

    @POST
    @Path("/relationTypeDataList/{discoverSpaceName}/{relationTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO postRelationTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationTypeName")String relationTypeName,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.queryRelationDataByQuerySQL(discoverSpaceName,relationTypeName,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/factTypeDataList/{discoverSpaceName}/{factTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO getFactTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("factTypeName")String factTypeName){
        return DiscoverSpaceOperationUtil.queryFactTypeData(discoverSpaceName,factTypeName);
    }

    @POST
    @Path("/factTypeDataList/{discoverSpaceName}/{factTypeName}")
    @Produces("application/json")
    public MeasurableQueryResultSetVO postFactTypeDataList(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("factTypeName")String factTypeName,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.queryFactDataByQuerySQL(discoverSpaceName,factTypeName,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/factTypePropertiesCSV/{discoverSpaceName}/{factTypeName}/{properties}")
    @Produces("application/json")
    public String getFactTypePropertiesCSV(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("factTypeName")String factTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateFactTypePropertiesCSV(discoverSpaceName,factTypeName,properties);
    }

    @GET
    @Path("/dimensionTypePropertiesCSV/{discoverSpaceName}/{dimensionTypeName}/{properties}")
    @Produces("application/json")
    public String getDimensionTypePropertiesCSV(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("dimensionTypeName")String dimensionTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateDimensionTypePropertiesCSV(discoverSpaceName,dimensionTypeName,properties);
    }

    @GET
    @Path("/relationTypePropertiesCSV/{discoverSpaceName}/{relationTypeName}/{properties}")
    @Produces("application/json")
    public String getRelationTypePropertiesCSV(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationTypeName")String relationTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateRelationTypePropertiesCSV(discoverSpaceName,relationTypeName,properties);
    }

    @GET
    @Path("/factTypePropertiesJSON/{discoverSpaceName}/{factTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> getFactTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("factTypeName")String factTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateFactTypePropertiesJSON(discoverSpaceName,factTypeName,properties);
    }

    @POST
    @Path("/factTypePropertiesJSON/{discoverSpaceName}/{factTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> postFactTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("factTypeName")String factTypeName, @PathParam("properties")String properties,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.generateFactTypePropertiesJSONByQuerySQL(discoverSpaceName,factTypeName,properties,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/dimensionTypePropertiesJSON/{discoverSpaceName}/{dimensionTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> getDimensionTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("dimensionTypeName")String dimensionTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateDimensionTypePropertiesJSON(discoverSpaceName,dimensionTypeName,properties);
    }

    @POST
    @Path("/dimensionTypePropertiesJSON/{discoverSpaceName}/{dimensionTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> postDimensionTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("dimensionTypeName")String dimensionTypeName, @PathParam("properties")String properties,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.generateDimensionTypePropertiesJSONByQuerySQL(discoverSpaceName,dimensionTypeName,properties,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GET
    @Path("/relationTypePropertiesJSON/{discoverSpaceName}/{relationTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> getRelationTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationTypeName")String relationTypeName, @PathParam("properties")String properties){
        return DiscoverSpaceOperationUtil.generateRelationTypePropertiesJSON(discoverSpaceName,relationTypeName,properties);
    }

    @POST
    @Path("/relationTypePropertiesJSON/{discoverSpaceName}/{relationTypeName}/{properties}")
    @Produces("application/json")
    public List<Map<String,String>> postRelationTypePropertiesJSON(@PathParam("discoverSpaceName")String discoverSpaceName, @PathParam("relationTypeName")String relationTypeName, @PathParam("properties")String properties,@FormParam("querySQL") String querySQL){
        if(querySQL!=null) {
            byte[] base64CharArray=Base64.getDecoder().decode(querySQL);
            try {
                String orginalString=new String(base64CharArray,"utf-8");
                return DiscoverSpaceOperationUtil.generateRelationTypePropertiesJSONByQuerySQL(discoverSpaceName,relationTypeName,properties,orginalString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
