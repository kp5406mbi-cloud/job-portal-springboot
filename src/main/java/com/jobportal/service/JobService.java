package com.jobportal.service;

import com.jobportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    // CRUD
    Job saveJob(Job job);
    Job getJobById(Long id);
    Job updateJob(Long id, Job job);
    void deleteJob(Long id);

    // Recruiter jobs
    List<Job> getJobsByRecruiter(String email);

    // Pagination + Sorting + Search




    Page<Job> searchJobsByRecruiter(String email, String keyword, Pageable pageable);

    // Apply job
    void applyJob(Long jobId, String userEmail);



    Page<Job> getAllJobs(Pageable pageable, int page);
}