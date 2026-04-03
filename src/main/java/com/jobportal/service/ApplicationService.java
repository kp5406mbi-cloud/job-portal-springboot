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
import java.util.List;

@Service
public class ApplicationService {


    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    public boolean apply(Long jobId, String userEmail, MultipartFile file) throws IOException {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByJobAndUserEmail(job, userEmail)) {
            return false;
        }

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        System.out.println("Original filename: " + file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();;
        System.out.println("Final filename: " + fileName);
        System.out.println("Saving to DB: " + fileName);

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();


        String filePath = uploadDir + fileName;

        File dest = new File(filePath);
        file.transferTo(dest);

        // ✅ VERY IMPORTANT PART
        Application application = new Application();
        application.setJob(job);
        application.setUserEmail(userEmail);
        application.setResumePath(fileName);  // ✅ SAVE FILE NAME

        applicationRepository.save(application);  // ✅ SAVE TO DB

        return true;
    }

    public List<Long> getAppliedJobIds(String userEmail) {


        return applicationRepository.findByUserEmail(userEmail)
                .stream()
                .map(app -> app.getJob().getId())
                .toList();
    }
}

