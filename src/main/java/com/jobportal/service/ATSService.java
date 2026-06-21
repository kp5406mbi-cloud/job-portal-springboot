package com.jobportal.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ATSService {

    public int calculateScore(
            String resumeText,
            String requiredSkills){

        if(requiredSkills == null ||
                requiredSkills.isBlank()){

            return 0;
        }

        String[] skills =
                requiredSkills.split(",");

        int matched = 0;

        for(String skill : skills){

            if(resumeText.toLowerCase()
                    .contains(skill.trim().toLowerCase())){

                matched++;
            }
        }

        int score =
                30 + (matched * 70) / skills.length;

        return Math.min(score,100);
    }

    public String getMissingSkills(
            String resumeText,
            String requiredSkills){

        List<String> missing =
                new ArrayList<>();

        for(String skill :
                requiredSkills.split(",")){

            if(!resumeText.toLowerCase()
                    .contains(skill.trim().toLowerCase())){

                missing.add(skill.trim());
            }
        }

        return String.join(", ", missing);
    }
}