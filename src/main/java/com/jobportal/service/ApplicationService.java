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

            System.out.println("BEFORE SENDING EMAIL");

            emailService.sendEmail(
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

            System.out.println("AFTER SENDING EMAIL");

        } catch (Exception e) {

            System.out.println("========= EMAIL ERROR =========");
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("===============================");
        }

        try {

            System.out.println("========== RECRUITER EMAIL ==========");

            String recruiterEmail = job.getRecruiterEmail();

            String recruiterSubject =
                    "New Application Received - " + job.getTitle();

            String recruiterBody =
                    "Dear Recruiter,\n\n" +

                            "A new candidate has applied for one of your job postings.\n\n" +

                            "Application Details\n" +
                            "--------------------------------------------------\n" +

                            "Job Title        : " + job.getTitle() + "\n" +
                            "Company          : " + job.getCompany() + "\n" +
                            "Applicant Email  : " + userEmail + "\n" +
                            "Application Date : " + application.getAppliedDate() + "\n" +
                            "Application Status : " + application.getStatus() + "\n" +
                            "ATS Score        : " + atsScore + "%\n";

            if (!missingSkills.isBlank()) {

                recruiterBody +=
                        "Missing Skills   : " +
                                missingSkills +
                                "\n";
            }

            recruiterBody +=
                    "Resume File      : " +
                            application.getResumePath() +
                            "\n";

            recruiterBody +=
                    "--------------------------------------------------\n\n";

            recruiterBody +=
                    "Please log in to Job Portal to review the application and download the candidate's resume.\n\n";

            recruiterBody +=
                    "Best Regards,\n";
            recruiterBody +=
                    "Job Portal Team";

            emailService.sendEmail(
                    recruiterEmail,
                    recruiterSubject,
                    recruiterBody
            );

            System.out.println("Recruiter notification sent.");

        } catch (Exception e) {

            System.out.println("========= RECRUITER EMAIL ERROR =========");

            e.printStackTrace();

            System.out.println("=========================================");
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

        System.out.println("========== UPDATE STATUS ==========");
        System.out.println("Application ID: " + id);
        System.out.println("Status: " + status);

        Application app = applicationRepository.findById(id)
                .orElseThrow();

        System.out.println("Applicant Email: " + app.getUserEmail());

        app.setStatus(status);
        applicationRepository.save(app);

        String jobTitle = app.getJob().getTitle();

        if ("ACCEPTED".equalsIgnoreCase(status)) {

            System.out.println("Inside ACCEPTED block");

            try {

                System.out.println("Sending acceptance email...");

                emailService.sendEmail(
                        app.getUserEmail(),
                        "Application Status Updated - Accepted",
                        "Dear Candidate,\n\n" +

                                "Congratulations! We are pleased to inform you that your application has been accepted.\n\n" +

                                "Application Details:\n" +
                                "----------------------------------------\n" +
                                "Job Title : " + jobTitle + "\n" +
                                "Status    : ACCEPTED\n" +
                                "----------------------------------------\n\n" +

                                "Our recruitment team will contact you soon with the next steps in the hiring process.\n\n" +

                                "You can also log in to your Job Portal account to view your application status.\n\n" +

                                "Thank you for using Job Portal.\n\n" +

                                "Best Regards,\n" +
                                "Job Portal Team"
                );

                System.out.println("Acceptance email sent.");

            } catch (Exception e) {

                System.out.println("========= ACCEPT EMAIL ERROR =========");
                e.printStackTrace();
            }
        }

        if ("REJECTED".equalsIgnoreCase(status)) {

            System.out.println("Inside REJECTED block");

            try {

                System.out.println("Sending rejection email...");

                emailService.sendEmail(
                        app.getUserEmail(),
                        "Application Status Updated",
                        "Dear Candidate,\n\n" +

                                "Thank you for your interest in the position and for taking the time to apply.\n\n" +

                                "Application Details:\n" +
                                "----------------------------------------\n" +
                                "Job Title : " + jobTitle + "\n" +
                                "Status    : REJECTED\n" +
                                "----------------------------------------\n\n" +

                                "After careful review, we have decided to move forward with other candidates whose qualifications more closely match the current requirements.\n\n" +

                                "We encourage you to continue exploring other opportunities on Job Portal and wish you every success in your future career.\n\n" +

                                "Thank you for using Job Portal.\n\n" +

                                "Best Regards,\n" +
                                "Job Portal Team"
                );
                System.out.println("Rejection email sent.");

            } catch (Exception e) {

                System.out.println("========= REJECT EMAIL ERROR =========");
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

