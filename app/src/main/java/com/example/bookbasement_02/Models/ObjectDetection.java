package com.example.bookbasement_02.Models;

public class ObjectDetection implements Comparable<ObjectDetection>{
    String label;
    Float confidence;

    public ObjectDetection (String label, Float confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    public String getLabel ( ) {
        return label;
    }

    public void setLabel (String label) {
        this.label = label;
    }

    public Float getConfidence ( ) {
        return confidence;
    }

    public void setConfidence (Float confidence) {
        this.confidence = confidence;
    }

    @Override
    public int compareTo(ObjectDetection u) {
        if(getConfidence()==u.getConfidence())
            return 0;
        else if(getConfidence()>u.getConfidence())
            return 1;
        else
            return -1;
    }
}
