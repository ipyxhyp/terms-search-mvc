package com.ptr.terms.model;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Scope("session")
@Component
public class TermsCard implements Serializable {


    private static final Logger logger = Logger.getLogger(TermsCard.class);

    private static final long serialVersionUID = 1L;
    private List<String> termsList = new ArrayList<String>(100);
    private List<TermRow> foundTermsList = new ArrayList<TermRow>(50);

    @Autowired
    private ResourceLoader resourceLoader;

    public boolean addTerm(String term) {
        boolean result = false;
        if (termsList != null) {
            result = termsList.add(term);
        }
        return result;
    }

    public boolean removeTerm(String term) {
        boolean result = false;
        if (termsList != null) {
            result = termsList.remove(term);
        }
        return result;
    }

    public void removeAllTerms() {

        if (termsList != null) {
            termsList.clear();
        }
    }

    public int getTermsCount() {

        int size = 0;
        if (termsList != null) {
            size = termsList.size();
        }
        return size;
    }

    @PostConstruct
    private void init() {
        logger.info("=== init post construct ====");
        Resource resourceFile = resourceLoader.getResource("classpath:" + "term.txt");
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = resourceFile.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                addTerm(line);
            }

        } catch (IOException iex) {
            logger.error(" IO Exception during the resource access thrown", iex);
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(" Exception while closing reader");
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(" Exception while closing input stream");
                }
            }
        }
    }

    public void findMatchedTerms(String term) {

        if (termsList != null && !termsList.isEmpty()) {
            foundTermsList.clear();
            for (String content : termsList) {
                if (content != null && term != null && content.contains(term)) {
                    String [] row = content.split(",");
                    TermRow termRow = new TermRow();
                    termRow.setColumn1(row[0]);
                    if(row.length > 1){
                        termRow.setColumn2(row[1]);
                        StringBuffer column3 = new StringBuffer();
                        for(int i = 2; i < row.length; i++){
                            column3.append(row[i]);
                        }
                        termRow.setColumn3(column3.toString());
                    }
                    foundTermsList.add(termRow);
                }
            }
        }
    }

    protected List<TermRow> getFoundTermsList() {
        return foundTermsList;
    }

    public int getFoundTermsCount() {
        return foundTermsList.size();
    }

    public List<TermRow> getFoundTermsInRange(int fromIndex, int toIndex) {

        List<TermRow> subList = Collections.EMPTY_LIST;
        if (foundTermsList != null && !foundTermsList.isEmpty()) {
            if(toIndex >= foundTermsList.size() ){
                toIndex = foundTermsList.size();
            }
            subList = Collections.unmodifiableList(foundTermsList.subList(fromIndex, toIndex));
        }
        return subList;
    }
}
