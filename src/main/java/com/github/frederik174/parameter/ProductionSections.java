package com.github.frederik174.parameter;

public enum ProductionSections {
    // names of production sections
    ASSEMBLY_SEC_1("12B2AS1");

    String idString;

    ProductionSections(String idString){
        this.idString = idString;
    }

    public String getIdString() {
        return idString;
    }
}
