package com.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name="resume_path")
    private String resumePath;


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
}