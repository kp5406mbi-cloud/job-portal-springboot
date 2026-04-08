package com.jobportal.repository;

import com.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterEmail(String email);

    Page<Job> findByRecruiterEmail(String email, Pageable pageable);

    Page<Job> findByCompanyContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String company,
            String location,
            Pageable pageable
    );



    Page<Job> findByTitleContainingAndLocationContaining(
            String keyword,
            String location,
            Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.recruiterEmail = :email AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchJobsByRecruiter(@Param("email") String email,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);



}
