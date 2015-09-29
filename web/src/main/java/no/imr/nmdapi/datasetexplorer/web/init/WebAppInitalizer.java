/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.imr.nmdapi.datasetexplorer.web.init;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import no.imr.framework.logging.logback.initalize.InitalizeLogbackHandler;
import no.imr.framework.logging.slf4j.exceptions.LoggerInitalizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 *
 * @author a5119
 */
public class WebAppInitalizer extends AbstractDispatcherServletInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAppInitalizer.class);
    
    
  @Override
    protected WebApplicationContext createServletApplicationContext() {
        //What do we need to scan here?
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        return ctx;
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.scan("no.imr.nmdapi.datasetexplorer.web.config");
        ctx.scan("no.imr.nmdapi.datasetexplorer.web.controller");
        ctx.scan("no.imr.nmdapi.datasetexplorer.service.config");
        ctx.scan("no.imr.nmdapi.datasetexplorer.dao.config");
  
        return ctx;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    /*    FilterRegistration.Dynamic mdcFilter = servletContext.addFilter("MDCInsertingServletFilter", ch.qos.logback.classic.helpers.MDCInsertingServletFilter.class);
        mdcFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
   */
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter", org.springframework.web.filter.CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
       try {
            InitalizeLogbackHandler.getInstance().initalize(System.getProperty("catalina.base") + "/conf/dataset_explorer_logback_v1.xml", true);
        } catch (LoggerInitalizationException ex) {
            LOGGER.error("Logging initializaton failed.", ex);
        }
        LOGGER.info("Entering application.");
    }
     
}
