package com.github.frederik174.utilities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class ObjectEvent {
    private String eventTime;
    private String eventTimeZoneOffset;
    private ArrayList<epc> epcList;
    private String action;
    private ArrayList<vin> ILMD;
    private String bizStep;
    private String disposition;
    private ArrayList<id> readPoint;
    private ArrayList<id> bizLocation;

    // Default constructor
    public ObjectEvent(){}

    // Custom constructor
    public ObjectEvent(String eventTime, String eventTimeZoneOffset, ArrayList epcList, String action, ArrayList<vin> ILMD,
                       String bizStep, String disposition, ArrayList readPoint, ArrayList bizLocation){
        this.eventTime = eventTime;
        this.eventTimeZoneOffset = eventTimeZoneOffset;
        this.epcList = epcList;
        this.action = action;
        this.ILMD = ILMD;
        this.bizStep = bizStep;
        this.disposition = disposition;
        this.readPoint = readPoint;
        this.bizLocation = bizLocation;
    }
    @XmlElement
    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @XmlElement
    public String getEventTimeZoneOffset() {
        return eventTimeZoneOffset;
    }

    public void setEventTimeZoneOffset(String eventTimeZoneOffset) {
        this.eventTimeZoneOffset = eventTimeZoneOffset;
    }

    @XmlElement
    public ArrayList<epc> getEpcList() {
        return epcList;
    }

    public void setEpcList(ArrayList<epc> epcList) {
        this.epcList = epcList;
    }

    @XmlElement
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @XmlElement
    public ArrayList<vin> getILMD() {
        return ILMD;
    }

    public void setILMD(ArrayList<vin> ILMD) {
        this.ILMD = ILMD;
    }

    @XmlElement
    public String getBizStep() {
        return bizStep;
    }

    public void setBizStep(String bizStep) {
        this.bizStep = bizStep;
    }

    @XmlElement
    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    @XmlElement
    public ArrayList<id> getReadPoint() {
        return readPoint;
    }

    public void setReadPoint(ArrayList<id> readPoint) {
        this.readPoint = readPoint;
    }

    @XmlElement
    public ArrayList<id> getBizLocation() {
        return bizLocation;
    }

    public void setBizLocation(ArrayList<id> bizLocation) {
        this.bizLocation = bizLocation;
    }
}