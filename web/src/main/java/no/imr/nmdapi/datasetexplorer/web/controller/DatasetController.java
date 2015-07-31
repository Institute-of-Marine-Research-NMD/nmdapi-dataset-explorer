package no.imr.nmdapi.datasetexplorer.web.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import no.imr.nmdapi.datasetexplorer.service.DatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author a5119
 */
@RestController
public class DatasetController {
  
     private static final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);
    
    @Autowired private DatasetService nmdRestService;
       
    
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
     * *General exception handler
     * 
     * @param request
     * @param exception 
     */
      @ExceptionHandler(Exception.class)
      @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)    
      public void handleError(HttpServletRequest request, Exception exception) {
     LOGGER.error("Request: " + request.getRequestURL() + " raised " + exception);
   }
   
      
      @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void  root(HttpServletResponse httpServletResponse) {
          httpServletResponse.setHeader("Location", "html/main.html");
    }
      
     /**
     * List all dataset  for a delivery,platform,year and mission type
     * 
     * @return
     */
    @RequestMapping(value = "/list/{missiontype}/{year}/{platform}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listDeliveries(@PathVariable(value = "missiontype") String missionType,
                @PathVariable(value = "year") String year,@PathVariable(value = "platform") String platform) {
        return nmdRestService.listDeliveries(missionType, year, platform);
    }
    
    
    /**
     * List details of all dataset  for a delivery,platform,year and mission type
     * 
     * @return
     */
    @RequestMapping(value = "/list/{missiontype}/{year}/{platform}/{delivery}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listDatasetDetail(@PathVariable(value = "missiontype") String missionType,
                @PathVariable(value = "year") String year,
                @PathVariable(value = "platform") String platform,
                @PathVariable(value = "delivery") String delivery) {
        return nmdRestService.listExistingDatasetsDetail(missionType, year, platform, delivery);
    }
    
   /**
     * List all  platforms for a year and mission type
     * 
     * @return
     */
    @RequestMapping(value = "/list/{missiontype}/{year}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listPlatforms(@PathVariable(value = "missiontype") String missionType,
                @PathVariable(value = "year") String year) {
        return nmdRestService.listPlatforms(missionType, year);
    }

    /**
     * List all years for  mission type
     * 
     * @return
     */
    @RequestMapping(value = "/list/{missiontype}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listYears(@PathVariable(value = "missiontype") String missionType) {
        return nmdRestService.listYears(missionType);
    }

    /**
     * List all mission types
     * 
     * @return
     */
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listMissionTypes() {
        return nmdRestService.listMissionTypes();
    }

    
    /**
     * List all delivery platforms for a platform,year and mission type and their load counts.
     *
     * @return
     */
    @RequestMapping(value = "/count/{missiontype}/{year}/{platform}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object summarizeDelivery(@PathVariable(value = "missiontype") String missionType,
                @PathVariable(value = "year") String year,@PathVariable(value = "platform") String platform) {
        return nmdRestService.summarizePlatformDataSets(missionType, year, platform);
    }
   
    /**
     * List all   platforms for a year and mission type and their load counts.
     *
     * @return
     */
    @RequestMapping(value = "/count/{missiontype}/{year}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object summarizePlatform(@PathVariable(value = "missiontype") String missionType,
                @PathVariable(value = "year") String year) {
        return nmdRestService.summarizeYearDataSets(missionType, year);
    }
   
    /**
     * List all  years for a mission type and their load counts.
     *
     * @return
     */
    @RequestMapping(value = "/count/{missiontype}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object summarizeMissionType(@PathVariable(value = "missiontype") String missionType) {
        return nmdRestService.summarizeMissionTypeDataSets(missionType);
    }
    
   /**
     * List all  mission types  and their load counts.
     *
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object summarizeAll() {
        return nmdRestService.summarizeAllDataSets();
    }
     
     
    /**
     * Count all  existing and  loaded datasets.
     *
     * @return
     */
    @RequestMapping(value = "/countAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object countAll() {
        return nmdRestService.countAllDataSets();
    }


    
    
}
 