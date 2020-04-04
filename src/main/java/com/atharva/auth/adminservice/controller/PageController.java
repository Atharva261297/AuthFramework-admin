package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.service.AdminService;
import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;

@Controller
public class PageController {

    @Autowired
    private AuthService auth;

    @Autowired
    private AdminService admin;

    @Autowired
    private ProjectService project;

    @RequestMapping("/{page}")
    public String page(@CookieValue(name = "auth.userId", required = false) String id,
                       @CookieValue(name = "auth.key", required = false) String key,
                       @PathVariable(name = "page") String page, Model model) {

//        if (auth.verify(id, key) == ErrorCodes.SUCCESS) {
            String userId = new String(Base64.getDecoder().decode(id.getBytes()));
            switch (page) {
                case "dashboard":
                    model.addAttribute("projectSize", 12);
                    model.addAttribute("userSize", 52);
                    break;

                case "profile":
                    model.addAttribute("user", admin.getDetails(userId));
                    break;

                case "projects":
                    model.addAttribute("newProject", new ProjectModel());
                    model.addAttribute("myPrj", project.getProjectUiList(userId));
                    break;
            }
            return page;
//        } else {
//            return "redirect:/login";
//        }

    }
}
