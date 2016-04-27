package com.ptr.terms.model;


import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.ANY)
public class TermRow implements Serializable {

    private String column1;
    private String column2;
    private String column3;



    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermRow termRow = (TermRow) o;

        if (column1 != null ? !column1.equals(termRow.column1) : termRow.column1 != null) return false;
        if (column2 != null ? !column2.equals(termRow.column2) : termRow.column2 != null) return false;
        if (column3 != null ? !column3.equals(termRow.column3) : termRow.column3 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column1 != null ? column1.hashCode() : 0;
        result = 31 * result + (column2 != null ? column2.hashCode() : 0);
        result = 31 * result + (column3 != null ? column3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TermRow{" +
                "column1='" + column1 + '\'' +
                ", column2='" + column2 + '\'' +
                ", column3='" + column3 + '\'' +
                '}';
    }
}
