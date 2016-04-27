package com.ptr.terms.controller;


import com.ptr.terms.model.Blah;
import com.ptr.terms.model.TermRow;
import com.ptr.terms.model.TermsCard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


@Controller("termsController")
@Scope("request")
@SessionAttributes({"termsCard"})
public class TermsController {

    private static final Logger logger = Logger.getLogger(TermsController.class);
    private final String GOOGLE_URL  = "http://www.google.com/search?q=";
    private final String USER_AGENT =  "Mozilla/5.0";

    @Qualifier("termsCard")
    @Autowired
    TermsCard termsCard;

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchWelcome(@ModelAttribute("termsCard") TermsCard card, HttpServletRequest request,
                                      HttpServletResponse response) {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        return new ModelAndView("searchPage", modelMap);
    }


    /**
     * redirects to specified URL
     * @param redirectUrl passed as "q" input parameter from REST Controller caller
     *
     * */
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public String redirectToUrl(@RequestParam("q") String redirectUrl, HttpServletRequest request,
                                HttpServletResponse response){
        logger.info(" ==== redirect to URL : "+redirectUrl);
        return "redirect:"+redirectUrl;
    }


    /**
     * Search for the term in file
     * @param searchTerm - phrase to search
     *
     * */
    @RequestMapping(value = "/get/foundTerms.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Blah searchTerm(@RequestParam String searchTerm) {
        Blah blahResult = new Blah();
        logger.info("=== term is " + searchTerm);
        List<TermRow> termRowList = null;
        termsCard.findMatchedTerms(searchTerm);
        termRowList = termsCard.getFoundTermsInRange(0, 10);
        blahResult.setTotalTermRowCount(termsCard.getFoundTermsCount());
        blahResult.setTermRowList(termRowList);
        return blahResult;
    }


    /**
     * Search by terms in specified range
     * @param from  - range start
     * @param to - range end
     *
     * */
    @RequestMapping(value = "/get/termsInRange.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Blah getMatchesInRange(@RequestParam int from, @RequestParam int to) {
        logger.info("==== get search terms  from " + from + " to " + to + " ===");
        Blah blahResult = new Blah();
        List<TermRow> subList = termsCard.getFoundTermsInRange(from, to);
        blahResult.setTermRowList(subList);
        blahResult.setTotalTermRowCount(termsCard.getFoundTermsCount());
        return blahResult;
    }


    /**
     * Sends/redirects search to google.com search service
     * @param searchTerm - phrase to search for in google search field
     *
     * */
    @RequestMapping(value = "/search/g", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public
    @ResponseBody
    String searchGoogle(@RequestParam String searchTerm) {
        logger.info("=== google search  " + searchTerm);
        String result = "";
        try {
            result = sendGet(searchTerm);
            return result;
        } catch (IOException iex) {
            logger.error(" <<<<< IO exception thrown " + iex);
            result = iex.getMessage();
            return result;
        }
    }

    /**
     * test Blah.json
     * @return Blah object
     *
     * */
    @RequestMapping(value = "/blah.json", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Blah getBlahJson() {
        Blah blah = new Blah();
        return blah;
    }

    /**
     * Actual GET request using Http connection
     * @param searchTerm - phrase to search for in google search field
     * @return resulting response as string converted from char reader
     * */
    private String sendGet(String searchTerm) throws IOException {

        String result = "";
        String strUrl = GOOGLE_URL + searchTerm;
        BufferedReader in = null;

        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        logger.info("=== GET request URL "+ url +" Response Code : " + responseCode);
        if (responseCode >= 200 && responseCode < 400) {
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = "";
                StringBuffer responseString = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    responseString.append(inputLine);
                }
                result = responseString.toString();
                return result;
            } catch (IOException iex) {
                logger.error(" <<<<  IOException " + iex);
                return result;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }  else {
            return result;
        }
    }


}

