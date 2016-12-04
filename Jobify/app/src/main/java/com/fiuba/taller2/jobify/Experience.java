package com.fiuba.taller2.jobify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Experience implements Serializable {

    @Expose @SerializedName("id")           int id;
    @Expose @SerializedName("where")        String place;
    @Expose @SerializedName("job_position") String jobPosition;

    public String getPlace() {
        return place;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setJobPosition(String pos) {
        jobPosition = pos;
    }
}
