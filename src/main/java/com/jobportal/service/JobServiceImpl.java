package com.jobportal.service;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import com.jobportal.util.EmailTemplates;



import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;

    // CRUD

    @Override
    public Job saveJob(Job job) {

        // First save the job
        Job savedJob = jobRepository.save(job);

        try {

            System.out.println("========== JOB POSTED HTML EMAIL ==========");

            String recruiterEmail = savedJob.getRecruiterEmail();

            String jobPostedHtml =
                    EmailTemplates.newJobPosted(
                            savedJob.getTitle(),
                            savedJob.getCompany(),
                            savedJob.getLocation()
                    );

            emailService.sendHtmlEmail(
                    recruiterEmail,
                    "Job Posted Successfully",
                    jobPostedHtml
            );

            System.out.println("Job posted HTML notification sent.");

        } catch (Exception e) {

            System.out.println(
                    "========= JOB POSTED HTML EMAIL ERROR ========="
            );

            e.printStackTrace();

            System.out.println(
                    "==============================================="
            );
        }

        return savedJob;
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    @Override
    public Job updateJob(Long id, Job updatedJob) {
        Job job = getJobById(id);

        job.setTitle(updatedJob.getTitle());
        job.setCompany(updatedJob.getCompany());
        job.setSalary(updatedJob.getSalary());
        job.setLocation(updatedJob.getLocation());
        job.setDescription(updatedJob.getDescription());
        job.setRequiredSkills(
                updatedJob.getRequiredSkills());


        return jobRepository.save(job);
    }

    @Override
    public Page<Job> searchJobsByRecruiter(String email, String keyword, Pageable pageable) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return jobRepository.findByRecruiterEmail(email, pageable);
        }

        return jobRepository.searchJobsByRecruiter(email, keyword, pageable);
    }


    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    // Recruiter jobs

    @Override
    public List<Job> getJobsByRecruiter(String email) {
        return jobRepository.findByRecruiterEmail(email);
    }

    // SEARCH + PAGINATION




    // Apply job

    @Override
    public void applyJob(Long jobId, String userEmail) {
        Job job = getJobById(jobId);

        Application application = new Application();
        application.setJob(job);
        application.setUserEmail(userEmail);

        applicationRepository.save(application);
    }

    @Override
    public Page<Job> getAllJobs(Pageable pageable, int page) {

        System.out.println("===== NEW BUILD =====");

        Page<Job> pageResult = jobRepository.findAll(pageable);

        System.out.println("Jobs found = " + pageResult.getTotalElements());

        for (Job job : pageResult.getContent()) {
            System.out.println(
                    job.getId() + " | " +
                            job.getTitle() + " | " +
                            job.getCompany()
            );
        }

        return pageResult;
    }




    @Override
    public List<Job> getAllJobs() {
        return List.of();
    }

 /*   @Override
    public Job saveJob(Job job) {
        return job;
    }

    @Override
    public Job getJobById(Long id) {
        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setCompany("Demo Company");
        job.setLocation("Remote");
        return job;
    }

    @Override
    public Job updateJob(Long id, Job job) {
        return job;
    }

    @Override
    public void deleteJob(Long id) {

    }

    @Override
    public List<Job> getJobsByRecruiter(String email) {
        return List.of();
    }

    @Override
    public Page<Job> searchJobsByRecruiter(String email, String keyword, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public void applyJob(Long jobId, String userEmail) {

    }

    @Override
    public Page<Job> getAllJobs(Pageable pageable, int page) {
        List<Job> jobs = new ArrayList<>();

        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setCompany("Demo Company");
        job.setLocation("Remote");

        jobs.add(job);

        return new org.springframework.data.domain.PageImpl<>(jobs);
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();

        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setCompany("Demo Company");
        job.setLocation("Remote");

        jobs.add(job);

        return jobs;
    }  */


}




