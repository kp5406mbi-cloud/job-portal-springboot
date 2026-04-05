package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
public class JobController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;

    @GetMapping("/jobs")
    public String showJobs(Model model, Authentication auth) {

        List<Job> jobs = jobService.getAllJobs();

        System.out.println("JOBS SIZE: " + jobs.size());



        List<Long> appliedJobIds = applicationService.getAppliedJobIds(auth.getName());

        System.out.println("Applied Job IDs: " + appliedJobIds);

        model.addAttribute("jobs", jobs);
        model.addAttribute("appliedJobIds", appliedJobIds);
        return "jobs";
    }

    @GetMapping("/post")
    public String showForm(Model model) {
        model.addAttribute("job", new Job());
        return "post-job";
    }

    @PostMapping("/post")
    public String saveJob(@ModelAttribute Job job, Principal principal) {
        jobService.saveJob(job);
        job.setRecruiterEmail(principal.getName());

        return "redirect:/recruiter/dashboard";
    }

    @GetMapping("/user/jobs")
    public List<String> getUserJobs(@RequestParam String email) {
        return applicationService.getJobsByUser(email);
    }
}
