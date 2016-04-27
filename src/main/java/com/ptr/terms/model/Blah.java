package com.ptr.terms.model;


import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.ANY)
public class Blah implements Serializable {


    private int totalTermRowCount = 0;

    private List<TermRow> termRowList  = new ArrayList<TermRow>(10);

    public boolean addTermRow(TermRow termRow){

        return  termRowList.add(termRow);
    }

    public boolean removeTermRow(TermRow termRow){

        return  termRowList.remove(termRow);
    }

    public  void clearAllTermRows(){
        termRowList.clear();
    }


    public List<TermRow> getTermRowList() {
        return termRowList;
    }

    public void setTermRowList(List<TermRow> termRowList) {
        this.termRowList = termRowList;
    }

    public int getTotalTermRowCount() {
        return totalTermRowCount;
    }

    public void setTotalTermRowCount(int totalTermRowCount) {
        this.totalTermRowCount = totalTermRowCount;
    }
}
