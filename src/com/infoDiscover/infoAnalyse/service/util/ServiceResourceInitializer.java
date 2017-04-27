package com.infoDiscover.infoAnalyse.service.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by wangychu on 4/27/17.
 */
public class ServiceResourceInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DiscoverSpaceOperationUtil.refreshItemAliasNameCache();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DiscoverSpaceOperationUtil.clearItemAliasNameCache();
    }
}
