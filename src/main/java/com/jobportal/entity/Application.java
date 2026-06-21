package com.jobportal.entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Column
    private Integer atsScore;

    @Column(length = 3000)
    private String missingSkills;

    @Column(length = 5000)
    private String aiFeedback;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;


    private LocalDateTime appliedDate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name="resume_path")
    private String resumePath;
    private String status;


    // getters & setters

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Job getJob() {
        return job;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setJob(Job job) {
        this.job = job;
    }


    public void setResumePath(String filePath) {
        this.resumePath = filePath;
    }

    public String getResumePath() {
        return resumePath;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public String getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(String missingSkills) {
        this.missingSkills = missingSkills;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
}