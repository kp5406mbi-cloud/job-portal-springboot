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
    private JobService jobService;

    // ✅ View recruiter jobs
    @GetMapping("/jobs")
    public String viewRecruiterJobs(Model model, Principal principal) {

        String username = principal.getName();
        model.addAttribute("username", username);

        List<Job> jobs = jobService.getJobsByRecruiter(username);
        model.addAttribute("jobs", jobs);

        return "recruiter-jobs";
    }

    // ✅ Show form
    @GetMapping("/post")
    public String showForm(Model model) {
        model.addAttribute("job", new Job());
        return "post-job";
    }

    // ✅ Save job
    @PostMapping("/post")
    public String saveJob(@ModelAttribute Job job, Principal principal) {

        // FIXED ORDER ✅
        job.setRecruiterEmail(principal.getName());
        jobService.saveJob(job);

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
        job.setRecruiterEmail(principal.getName()); // keep ownership

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