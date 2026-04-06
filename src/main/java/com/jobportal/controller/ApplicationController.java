package com.jobportal.controller;

import com.jobportal.entity.Application;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ApplicationController {




    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;





    @PostMapping("/apply/{id}")
    public String applyJob(@PathVariable Long id,
                           Authentication auth,
                           @RequestParam("file") MultipartFile file,
                           RedirectAttributes redirectAttributes) {

        try{
        boolean applied = applicationService.apply(id, auth.getName(), file);

        if (!applied) {
            redirectAttributes.addFlashAttribute("error", "Already applied!");
        } else {
            redirectAttributes.addFlashAttribute("success", "Application submitted!");
        }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Upload failed!");
        }

        return "redirect:/user/jobs";
    }

    @GetMapping("/resume/{id}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) throws IOException {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));


        System.out.println("APP ID: " + app.getId());
        System.out.println("RESUME PATH FROM DB: " + app.getResumePath());

        if (app.getResumePath() == null || app.getResumePath().isEmpty()) {
            throw new RuntimeException("Resume not found");
        }

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path path = Paths.get(uploadDir).resolve(app.getResumePath());

        if (!path.toFile().exists()) {
            throw new RuntimeException("File not found at: " + path);
        }

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + path.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(resource);
    }

    @PostMapping("/recruiter/application/{id}/accept")
    public String acceptApplication(@PathVariable Long id) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("ACCEPTED");
        applicationRepository.save(app);

        return "redirect:/recruiter/applicants/" + app.getJob().getId();
    }

    @PostMapping("/recruiter/application/{id}/reject")
    public String rejectApplication(@PathVariable Long id) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("REJECTED");
        applicationRepository.save(app);

        return "redirect:/recruiter/applicants/" + app.getJob().getId();
    }


}
