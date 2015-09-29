package no.imr.nmdapi.datasetexplorer.web.controller;

import javax.servlet.http.HttpServletRequest;
import no.imr.nmdapi.datasetexplorer.service.CruiseSeriesService;
import no.imr.nmdapi.datasetexplorer.service.TimeSeriesService;
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
public class TimeSeriesController {

     private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeriesController.class);
    
    @Autowired private TimeSeriesService timeSeriesService;
  
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
     * List all time series
     * 
     * @return
     */
    @RequestMapping(value = "/TimeSeries/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listTimeSeries() {
        return timeSeriesService.listTimeSeries();
    }
    
   /**
     * List years for a cruise series
     * 
     * @return
     */
    @RequestMapping(value = "/TimeSeries/list/{timeSeriesName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listTimeSeries( @PathVariable(value = "timeSeriesName") String timeSeriesName) {
        return timeSeriesService.listTimeSeriesYears(timeSeriesName);
    }

 
   /**
     * List datasets  nr for a cruise series and year
     * 
     * @return
     */
    @RequestMapping(value = "/TimeSeries/list/{timeSeriesName}/{year}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listCruiseSeries( @PathVariable(value = "timeSeriesName") String timeSeriesName,
           @PathVariable(value = "year") String year ) {
        return timeSeriesService.listCruises(timeSeriesName,year);
    }

    
}
