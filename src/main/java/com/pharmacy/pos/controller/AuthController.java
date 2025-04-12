package com.pharmacy.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling authentication-related requests
 */
@Controller
public class AuthController {

    /**
     * Display login page
     * @param error Error message if authentication fails
     * @param logout Logout message if user has logged out
     * @param model Spring Model object for passing attributes to the view
     * @return The login view template name
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "login";
    }

    /**
     * Redirect to dashboard after successful login
     * @return Redirect to dashboard
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}