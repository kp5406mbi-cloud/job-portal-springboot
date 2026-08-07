package com.jobportal.controller;

import com.jobportal.entity.Application;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private S3Service s3Service;




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
    public ResponseEntity<byte[]> downloadResume(
            @PathVariable Long id) throws Exception {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        byte[] fileBytes =
                s3Service.downloadFile(app.getResumePath());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                app.getResumePath() + "\""
                )
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        "application/pdf"
                )
                .body(fileBytes);
    }


    @PostMapping("/recruiter/application/{id}/accept")
    public String acceptApplication(@PathVariable Long id) {

        applicationService.updateStatus(id, "ACCEPTED");

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return "redirect:/recruiter/applicants/" + app.getJob().getId();
    }

    @PostMapping("/recruiter/application/{id}/reject")
    public String rejectApplication(@PathVariable Long id) {

        applicationService.updateStatus(id, "REJECTED");

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        return "redirect:/recruiter/applicants/" + app.getJob().getId();
    }

    @GetMapping("/user/applications")
    public String myApplications(Model model,
                                 Principal principal) {

        String email = principal.getName();

        List<Application> applications =
                applicationService.getApplicationsByUser(email);

        model.addAttribute("applications", applications);

        return "my-applications";
    }


}

/*package com.jobportal.controller;

import com.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // ================= APPLY JOB =================
    @PostMapping("/apply/{id}")
    public String applyJob(@PathVariable Long id,
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {

        try {
            boolean applied = applicationService.apply(id, auth.getName(),null);

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

    // ================= DOWNLOAD RESUME =================
    @GetMapping("/resume/{id}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {

        try {
            // Dummy file (no DB dependency)
            Path path = Paths.get("uploads/dummy.pdf");

            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"resume.pdf\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }

    // ================= ACCEPT APPLICATION =================
    @PostMapping("/recruiter/application/{id}/accept")
    public String acceptApplication(@PathVariable Long id) {

        System.out.println("Accepted application: " + id);

        return "redirect:/recruiter/applicants";
    }

    // ================= REJECT APPLICATION =================
    @PostMapping("/recruiter/application/{id}/reject")
    public String rejectApplication(@PathVariable Long id) {

        System.out.println("Rejected application: " + id);

        return "redirect:/recruiter/applicants";
    }
}  */
