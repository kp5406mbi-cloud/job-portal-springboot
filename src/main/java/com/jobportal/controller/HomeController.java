package com.jobportal.controller;

import com.jobportal.dto.UserDTO;
import com.jobportal.entity.Users;

import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "Job Portal Running 🚀";
    }

    @GetMapping("/add")
    public String addUser() {

        Users user = new Users();
        user.setEmail("test2@gmail.com");
        user.setPassword("1234");

        user.setRole("USER");

        userService.saveUser(user);

        return "User saved!";
    }
}

 /*   @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getAllUsers()
                .stream()
                .map(user -> new UserDTO(user.getUserId(), user.getEmail()))
                .toList();
    }  */

 /*   @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to Dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "User Dashboard";
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard() {
        return "Recruiter Dashboard";
    }


}  */


/*package com.jobportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "App is running";
    }
} */