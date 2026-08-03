package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
@RequestMapping("/user")
public class UserJobController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;

    @GetMapping("/jobs")
    public String userJobs(Model model, Authentication auth, Principal principal) {

        int page = 0;
        Pageable pageable = PageRequest.of(page, 50);

        Page<Job> jobPage = jobService.getAllJobs(pageable, page);
        List<Job> jobs = jobPage.getContent();

        System.out.println("Jobs displayed = " + jobs.size());

        for (Job job : jobs) {
            System.out.println(
                    job.getId() + " | " +
                            job.getTitle() + " | " +
                            job.getCompany()
            );
        }

        model.addAttribute("jobs", jobs);

        String username;

        if (auth.getPrincipal() instanceof OAuth2User oauthUser) {
            username = oauthUser.getAttribute("email");
        } else {
            username = principal.getName();
        }

        model.addAttribute("username", username);



        // Handle logged-in users safely
        if (auth != null && auth.isAuthenticated()) {
            List<Long> appliedJobIds = applicationService.getAppliedJobIds(username);
            if (appliedJobIds == null) {
                appliedJobIds = new ArrayList<>();
            }
            model.addAttribute("appliedJobIds", appliedJobIds);
        }

        return "jobs";
    }
}
