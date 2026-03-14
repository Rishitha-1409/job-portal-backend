package com.jobportal.dto;

public class ApplicationRequest {

    private String coverLetter;
    private String resumeUrl;

    public ApplicationRequest(){}

    public String getCoverLetter(){ return coverLetter; }

    public void setCoverLetter(String coverLetter){ this.coverLetter = coverLetter; }

    public String getResumeUrl(){ return resumeUrl; }

    public void setResumeUrl(String resumeUrl){ this.resumeUrl = resumeUrl; }
}