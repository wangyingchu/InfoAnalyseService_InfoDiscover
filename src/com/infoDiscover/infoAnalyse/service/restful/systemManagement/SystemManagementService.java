package com.infoDiscover.infoAnalyse.service.restful.systemManagement;

import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationUtil;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by wangychu on 5/12/17.
 */

@Path("/systemManagementService")
@Produces("application/json")
public class SystemManagementService {

    @GET
    @Path("/refreshItemAliasNameCache/")
    @Produces("application/json")
    public boolean infoDiscoverSpaceFactTypeDataCount(){
        DiscoverSpaceOperationUtil.refreshItemAliasNameCache();
        return true;
    }

    @GET
    @Path("/refreshDiscoverSpaceDataMetaInfo/{discoverSpaceName}/")
    @Produces({"application/xml", "application/json"})
    public void refreshDiscoverSpaceDataMetaInfo(@PathParam("discoverSpaceName")String discoverSpaceName){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace(discoverSpaceName);
            if(targetSpace!=null){
                DiscoverSpaceOperationUtil.refreshTypeDefinitionInfo(targetSpace);
                DiscoverSpaceOperationUtil.refreshItemAliasNameCache();
            }
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }
}
