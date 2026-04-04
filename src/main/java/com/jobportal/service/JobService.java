package com.jobportal.service;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private ApplicationRepository applicationRepository;


    @Autowired
    private JobRepository jobRepository;

    public void saveJob(Job job) {
        jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public void applyJob(Long jobId, String userEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application();
        application.setJob(job);
        application.setUserEmail(userEmail);

        applicationRepository.save(application);
    }

    public List<Job> getJobsByRecruiter(String email) {

        return jobRepository.findByRecruiterEmail(email);

    }
}
