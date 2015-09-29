package no.imr.nmdapi.datasetexplorer.web.controller;

import javax.servlet.http.HttpServletRequest;
import no.imr.nmdapi.datasetexplorer.service.CruiseService;
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
public class CruiseController {

     private static final Logger LOGGER = LoggerFactory.getLogger(CruiseController.class);
    
    @Autowired private CruiseService cruiseService;
       
    
    
  
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
     * List path for cruise nr
     * 
     * @return
     */
    @RequestMapping(value = "/Cruise/mapByNR/{cruiseNR}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object listCruiseSeries( @PathVariable(value = "cruiseNR") String cruiseNR) {
        return cruiseService.getCruisePath(cruiseNR);
    }


    
}
