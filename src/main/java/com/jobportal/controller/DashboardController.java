package com.jobportal.controller;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class DashboardController<auth> {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobService jobService;

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("jobs", jobService.getAllJobs());
        return "user-dashboard";
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard(Model model, Principal principal) {

        String email = principal.getName();

        List<Job> jobs = jobService.getJobsByRecruiter(email);
        model.addAttribute("jobs", jobs);



        Map<Long, List<Application>> applicationsMap = new HashMap<>();

        for (Job job : jobs) {
            List<Application> apps = applicationRepository.findByJob(job);
            applicationsMap.put(job.getId(), apps);
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("applicationsMap", applicationsMap);

        return "redirect:/recruiter/jobs";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"))) {
            return "redirect:/recruiter/dashboard";
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/clear-jobs")
    public String clearJobs() {
        jobRepository.deleteAll();
        return "redirect:/recruiter/dashboard";
    }
}
