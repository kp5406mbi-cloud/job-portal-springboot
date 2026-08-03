package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.EmailService;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private EmailService emailService;

    // ✅ View recruiter jobs
    @GetMapping("/jobs")
    public String viewRecruiterJobs(Model model,
                                    Principal principal,
                                    @RequestParam(defaultValue = "") String company,
                                    @RequestParam(defaultValue = "") String location,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "id") String sortBy,
                                    @RequestParam(defaultValue = "asc") String direction) {

        String username;

        if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken token) {
            username = token.getPrincipal().getAttribute("email");
        } else {
            username = principal.getName();
        }
        model.addAttribute("username", username);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String keyword = (company == null ? "" : company ) + " " +
                (location == null ? "" : location);

        Page<Job> jobPage = jobService.searchJobsByRecruiter(username, keyword, pageable);

        model.addAttribute("jobs", jobPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());


        model.addAttribute("company", company);
        model.addAttribute("location", location);

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "recruiter-jobs";
    }

    // ✅ Show form
    @GetMapping("/post")
    public String showForm(Model model) {
        model.addAttribute("job", new Job());
        return "post-job";
    }

    @PostMapping("/post")
    public String saveJob(@ModelAttribute Job job, Principal principal) {

        String email;

        if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken token) {
            email = token.getPrincipal().getAttribute("email");
        } else {
            email = principal.getName();
        }

        job.setRecruiterEmail(email);
        jobService.saveJob(job);

        System.out.println("Saved job for: " + email);

        try {

            System.out.println("About to send recruiter email...");

            emailService.sendEmail(
                    email,
                    "Job Posted Successfully",
                    "Your job '" + job.getTitle() +
                            "' has been posted successfully on Job Portal."
            );

            System.out.println("Recruiter email sent.");

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("FAILED TO SEND RECRUITER EMAIL");
        }

        return "redirect:/recruiter/jobs";
    }

    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "redirect:/recruiter/jobs";
    }
    @GetMapping("/edit/{id}")
    public String editJobForm(@PathVariable Long id, Model model) {

        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);

        return "edit-job";
    }

    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable Long id,
                            @ModelAttribute Job job,
                            Principal principal) {

        job.setId(id); // VERY IMPORTANT
        String username;

        if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken token) {
            username = token.getPrincipal().getAttribute("email");
        } else {
            username = principal.getName();
        }

        jobService.updateJob(id, job);

        return "redirect:/recruiter/jobs";
    }

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/applicants/{id}")
    public String viewApplicants(@PathVariable Long id, Model model) {

        var applications = applicationService.getApplicationsByJobId(id);

        model.addAttribute("applications", applications);

        return "applicants"; // create this HTML
    }

    @PostMapping("/application/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status) {

        applicationService.updateStatus(id, status);

        return "redirect:/recruiter/jobs";
    }

}