package no.imr.nmdapi.datasetexplorer.web.controller;

import no.imr.nmdapi.datasetexplorer.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Terry Hannant <a5119>
 */
@RestController
public class SystemController {
  
     private static final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);
    
    @Autowired private CacheService cacheService;
       
    
    /**
     * Simple ping to test if system is up
     * 
     * @return 
     */
    @RequestMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String ping() {
        return ("ping ");
       }
  
    /**
     * Simple mem stat
     * 
     * @return 
     */
    @RequestMapping("/mem")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String memStat() {
    Runtime rt = Runtime.getRuntime();
    long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
    return String.valueOf(used);
}  
    
    /**
     * Simple mem stat after gc
     * 
     * @return 
     */
    @RequestMapping("/gcmem")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String gcMemStat() {
        System.gc();
    Runtime rt = Runtime.getRuntime();
    long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
    return String.valueOf(used);
}
    
    @RequestMapping("/cacheReload")
    @ResponseStatus(HttpStatus.OK)
    public String cacheReload() {
        cacheService.refreshAll();
    return "OK";
    }
    
    
}
