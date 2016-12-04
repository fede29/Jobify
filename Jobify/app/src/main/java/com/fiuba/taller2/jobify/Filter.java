package com.fiuba.taller2.jobify;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Filter implements Serializable {

    Integer range;
    JobPosition jobPosition;
    LinkedList<Skill> skills;
    public String query;


    public Filter() {
        query = "";
        skills = new LinkedList<>();
    }

    public Boolean hasValidQuery() {
        return query != null && !query.isEmpty();
    }

    public void removeRange() {
        range = null;
    }

    public void setRange(int i) {
        range = i;
    }

    public Integer getRange() {
        return range.intValue();
    }

    public Boolean hasRange() {
        return range != null;
    }

    public void addSkill(Skill s) {
        if (! skills.contains(s)) skills.add(s);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setJobPosition(JobPosition jp) {
        jobPosition = jp;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public Boolean hasJobPosition() {
        return jobPosition != null;
    }

    public void remove(Skill s) {
        skills.remove(s);
    }

    public String toQuery(User user) {
        String q = String.format("user=%s&skills=%s", htmlize(query), htmlize(skills));
        if (hasRange() && user.hasLastLocation())
            q += String.format("&range=%s&latitude=%s&longitude=%s",
                    range, user.getPosition().getLat(), user.getPosition().getLng());
        if (jobPosition != null)
            q += String.format("&job_position=%s", htmlize(jobPosition.toString()));
        return q;
    }


    private String htmlize(String string) {
        return string.replace(" ", "%20");
    }

    private String htmlize(Collection<?> c) {
        String s = "";
        for (Object obj : c) {
            if (!s.isEmpty()) s += ",";
            s += obj.toString();
        }
        return htmlize(s);
    }
}
