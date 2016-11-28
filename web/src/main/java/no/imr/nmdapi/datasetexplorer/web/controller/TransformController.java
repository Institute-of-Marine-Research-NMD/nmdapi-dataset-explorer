package no.imr.nmdapi.datasetexplorer.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import no.imr.nmdapi.datasetexplorer.service.TimeSeriesService;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Terry Hannant <a5119>
 */
@Controller
public class TransformController {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TransformController.class);

    final int BUFFER_SIZE = 1024 * 4;
    
    @Autowired
    private TimeSeriesService timeSeriesService;

    @RequestMapping(value = "/SimpleFetch", method = RequestMethod.GET)
    public void simpleFetch(@RequestParam(value = "src") String sourceURL,
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpURLConnection proxyRequest = createProxyRequest(sourceURL);

        if (proxyRequest != null) {
            try {
                String fileName = mapFilename(sourceURL);
                response.setStatus(proxyRequest.getResponseCode());
                InputStream input = proxyRequest.getInputStream();
                response.setContentType("text/xml");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                IOUtils.copy(input, response.getOutputStream());
                input.close();
            } catch (IOException ex) {
                LOG.error("Error proxying " + sourceURL,ex);
                
                
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    
    @RequestMapping(value = "/PartialFetch", method = RequestMethod.GET)
    public void shortFetch(@RequestParam(value = "src") String sourceURL,
            @RequestParam(value = "length") int requestLength,
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpURLConnection proxyRequest = createProxyRequest(sourceURL);

        if (proxyRequest != null) {
            try {
                String fileName = mapFilename(sourceURL);
                response.setStatus(proxyRequest.getResponseCode());

                InputStream input = proxyRequest.getInputStream();
                response.setContentType("text/xml");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                final byte[] buffer = new byte[BUFFER_SIZE];

                int n = 0;
                int count = requestLength;
                n = input.read(buffer);
                while (n != -1 && count - n >= 0) {
                    response.getOutputStream().write(buffer, 0, n);
                    count -= n;
                    n = input.read(buffer);
                }
                if (n != -1) {
                    response.getOutputStream().write(buffer, 0, count);
                }
                input.close();
            } catch (IOException ex) {
                LOG.error("Error proxying " + sourceURL);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /*
     *Maps dataset url to filename.
     *TODO Push functionality into service layer?
     */
    private String mapFilename(String url) {
        String[] parts = url.split("/");
        StringBuilder result = new StringBuilder(parts[5]);
        result.append("_");

        if ("stox".equals(parts[5])) {
            String stoxPath = timeSeriesService.getStoxPath(parts[parts.length - 1]);
            if (stoxPath != null) {
                result.append(stoxPath.replace("/", "_").replace(" ", "+"));
            } else {
                result.append("undefined");
            }

        } else {
            result.append("cruiseNumber_");
            result.append(parts[parts.length - 1]);
            result.append("_");
            result.append(parts[parts.length - 2].split("-")[0].replace("%20", "+"));
        }
        result.append(".xml");
        return result.toString();
    }

    private HttpURLConnection createProxyRequest(String requestURL) {
        HttpURLConnection result = null;
        try {
            URL url = new URL(requestURL.replace(" ", "%20"));
            result = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException ex) {
            LOG.error("Incorrect url syntax", ex);
        } catch (IOException ex) {
            LOG.error("Unable to connect", ex);
        }
        return result;
    }

}
