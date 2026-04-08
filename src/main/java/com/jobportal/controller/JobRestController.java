package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.service.JobService;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobRestController {

    @Autowired
    private JobService jobService;

    // ✅ GET all jobs with pagination + sorting + filtering
    @GetMapping
    public Page<Job> getJobs(
            Principal principal,
            @RequestParam(defaultValue = "") String company,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);


        if (principal == null) {
            return jobService.getAllJobs(pageable, page);
        }

        return jobService.searchJobsByRecruiter(
                principal.getName(),
                company,
                pageable
        );
    }

    // ✅ GET job by ID
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    // ✅ CREATE job
    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.saveJob(job);
    }

    // ✅ UPDATE job
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        return jobService.updateJob(id, updatedJob);
    }

    // ✅ DELETE job
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "Job deleted successfully";
    }
}
