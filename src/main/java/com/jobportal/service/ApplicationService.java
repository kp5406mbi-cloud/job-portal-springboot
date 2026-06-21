package com.jobportal.service;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ATSService atsService;

    @Autowired
    private ResumeParserService resumeParserService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    public boolean apply(Long jobId, String userEmail, MultipartFile file) throws Exception {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobAndUserEmail(job, userEmail)) {
            return false;
        }

        String resumeText =
                resumeParserService.extractText(file);

        int atsScore =
                atsService.calculateScore(
                        resumeText,
                        job.getRequiredSkills());

        String missingSkills =
                atsService.getMissingSkills(
                        resumeText,
                        job.getRequiredSkills());

        Application application =
                new Application();

        String fileName =
                s3Service.uploadFile(file);

        application.setJob(job);
        application.setUserEmail(userEmail);
        application.setResumePath(fileName);
        application.setAppliedDate(LocalDateTime.now());

        application.setAtsScore(atsScore);
        application.setStatus("PENDING");
        application.setMissingSkills(
                missingSkills);

        applicationRepository.save(application);




        try {

            emailService.sendMail(
                    userEmail,
                    "Application Submitted Successfully",
                    "Your application for the position '" +
                            job.getTitle() +
                            "' at " +
                            job.getCompany() +
                            " has been submitted successfully.\n\n" +
                            "Status: PENDING\n\n" +
                            "Thank you for using Job Portal."
            );

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("MAIL FAILED BUT APPLICATION SAVED");
        }

        return true;

    }

    public List<Long> getAppliedJobIds(String userEmail) {


        return applicationRepository.findByUserEmail(userEmail)
                .stream()
                .map(app -> app.getJob().getId())
                .toList();
    }

    public List<String> getJobsByUser(String email) {
        return applicationRepository.findJobTitlesByUserEmail(email);
    }

    public List<Application> getApplicationsByJobId(Long jobId) {

        return applicationRepository
                .findByJobIdOrderByAtsScoreDesc(jobId);
    }

    public List<Application> getApplicationsByUser(String email) {

        return applicationRepository.findByUserEmail(email);
    }

    public void updateStatus(Long id, String status) {
        Application app = applicationRepository.findById(id).orElseThrow();
        app.setStatus(status);
        applicationRepository.save(app);

        String jobTitle =
                app.getJob().getTitle();

        if ("ACCEPTED".equalsIgnoreCase(status)) {

            try {

                emailService.sendMail(
                        app.getUserEmail(),
                        "Application Accepted",
                        "Congratulations!\n\n" +
                                "Your application for '" +
                                jobTitle +
                                "' has been accepted."
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ("REJECTED".equalsIgnoreCase(status)) {

            try {

                emailService.sendMail(
                        app.getUserEmail(),
                        "Application Rejected",
                        "Thank you for applying.\n\n" +
                                "Your application for '" +
                                jobTitle +
                                "' was not selected at this time."
                );

            } catch (Exception e) {
                e.printStackTrace();
            }

           
        }
    }
}

/*@Service
public class ApplicationService {

    public void applyJob(Long jobId, String userEmail) {
        System.out.println("Dummy apply: " + jobId + " by " + userEmail);
    }
}



package com.jobportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ApplicationService {

    public boolean apply(Long jobId, String userEmail, MultipartFile file) {
        System.out.println("Dummy apply: " + jobId + " by " + userEmail);
        return false;
    }

    public List<Object> getApplicationsByJobId(Long jobId) {
        return new ArrayList<>();
    }

    public void updateStatus(Long applicationId, String status) {
        System.out.println("Dummy update status: " + applicationId + " -> " + status);
    }

    public List<Long> getAppliedJobIds(String userEmail) {
        return new ArrayList<>();
    }
} */

