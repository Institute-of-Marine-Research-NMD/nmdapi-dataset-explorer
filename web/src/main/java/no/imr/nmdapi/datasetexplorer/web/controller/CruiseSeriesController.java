package no.imr.nmdapi.datasetexplorer.web.controller;

import javax.servlet.http.HttpServletRequest;
import no.imr.nmdapi.datasetexplorer.service.CruiseSeriesService;
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
 * @author Terry Hannant <a5119>
 */
@RestController
public class CruiseSeriesController {

     private static final Logger LOGGER = LoggerFactory.getLogger(CruiseSeriesController.class);
    
    @Autowired private CruiseSeriesService cruiseSeriesService;
       
    
    
  
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
   
    
    /**
     * List all cruise series
     * 
     * @return
     */
    @RequestMapping(value = "/CruiseSeries/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listCruiseSeries() {
        return cruiseSeriesService.listCruiseSeries();
    }
    
   /**
     * List years for a cruise series
     * 
     * @return
     */
    @RequestMapping(value = "/CruiseSeries/list/{cruiseSeriesName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listCruiseSeries( @PathVariable(value = "cruiseSeriesName") String cruiseSeriesName) {
        return cruiseSeriesService.listCruiseSeriesYears(cruiseSeriesName);
    }

 
   /**
     * List details for a cruise series and year
     * 
     * @return
     */
    @RequestMapping(value = "/CruiseSeries/list/{cruiseSeriesName}/{year}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listCruiseSeries( @PathVariable(value = "cruiseSeriesName") String cruiseSeriesName,
           @PathVariable(value = "year") String year ) {
        return cruiseSeriesService.listCruises(cruiseSeriesName,year);
    }

    /**
     * Summarize status of cruises for a cruise series and year
     * 
     * @return
     */
    @RequestMapping(value = "/CruiseSeries/summary/{cruiseSeriesName}/{year}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object summarizeCruiseSeries( @PathVariable(value = "cruiseSeriesName") String cruiseSeriesName,
           @PathVariable(value = "year") String year ) {
        return cruiseSeriesService.summarizeDatasetsStatus(cruiseSeriesName,year);
    }
    
    
}
