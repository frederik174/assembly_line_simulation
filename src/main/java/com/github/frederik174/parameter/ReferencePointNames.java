package com.github.frederik174.parameter;

public enum ReferencePointNames {
    // names of RTLS reference Points etc.
    REFERENCE_POINT_NAME("12H2A70");

    String idString;

    ReferencePointNames(String idString){
        this.idString = idString;
    }

    public String getIdString() {
        return idString;
    }
}
