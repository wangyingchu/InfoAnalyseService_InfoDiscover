package com.infoDiscover.infoAnalyse.service.restful.systemManagement;

import com.infoDiscover.infoAnalyse.service.util.DiscoverSpaceOperationUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
}