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
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

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

    public void deleteJob(Long id) {

        jobRepository.deleteById(id);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
}
