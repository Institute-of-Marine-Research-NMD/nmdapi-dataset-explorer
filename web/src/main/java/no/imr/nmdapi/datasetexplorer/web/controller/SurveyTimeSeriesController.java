package no.imr.nmdapi.datasetexplorer.web.controller;

import java.util.List;
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
public class SurveyTimeSeriesController {

     private static final Logger LOGGER = LoggerFactory.getLogger(SurveyTimeSeriesController.class);
    
    @Autowired private TimeSeriesService timeSeriesService;
    
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
     * List all time series
     * 
     * @return
     */
    @RequestMapping(value = "/SurveyTimeSeries/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listTimeSeries() {
        return timeSeriesService.listSurveyTimeSeries();
    }
    
   /**
     * List sample times for a Survey timeseries
     * 
     * @return
     */
    @RequestMapping(value = "/SurveyTimeSeries/listSamples/{surveyTimeSeriesName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listTimeSeries( @PathVariable(value = "surveyTimeSeriesName") String surveyTimeSeriesName) {
        return timeSeriesService.listSurveyTimeSeriesSample(surveyTimeSeriesName);
    }

    
   /**
     * List cruises for a Survey timeseries and time
     * 
     * @return
     */
    @RequestMapping(value = "/SurveyTimeSeries/listCruise/{surveyTimeSeriesName}/{period}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listTimeSeries( @PathVariable(value = "surveyTimeSeriesName") String surveyTimeSeriesName,
             @PathVariable(value = "period") String period) {
        return timeSeriesService.listSurveyTimeSeriesCruise(surveyTimeSeriesName,period);
    }

  /**
     * List cruises for a Survey timeseries and time
     * 
     * @return
     */
    @RequestMapping(value = "/SurveyTimeSeries/summary/{surveyTimeSeriesName}/{period}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List  summarizeTimeSeries( @PathVariable(value = "surveyTimeSeriesName") String surveyTimeSeriesName,
             @PathVariable(value = "period") String period) {
        return timeSeriesService.summarizeDatasetsStatus(surveyTimeSeriesName, period);
    }

    
}
