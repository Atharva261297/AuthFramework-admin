package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.client.EmailFeignClient;
import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.model.AdminRights;
import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.model.ui.LoginModel;
import com.atharva.auth.adminservice.model.ui.ProjectAdminsModel;
import com.atharva.auth.adminservice.model.ui.ProjectDetailModel;
import com.atharva.auth.adminservice.model.ui.RegisterModel;
import com.atharva.auth.adminservice.service.AdminService;
import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.service.ProjectService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Controller
public class ViewController {

    @Autowired
    private AuthService auth;

    @Autowired
    private EmailFeignClient emailClient;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AdminService adminService;

    Logger log = LoggerFactory.getLogger(ViewController.class);

    @RequestMapping("/login")
    public String login(@CookieValue(name = "auth.userId", required = false) String cookieId,
                        @CookieValue(name = "auth.key", required = false) String cookieValue,
                        Model model){
        if (auth.verify(cookieId, cookieValue) == ErrorCodes.SUCCESS) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
            return "login";
        }
    }

    @PostMapping("/login-result")
    public String loginResult(HttpServletResponse response, Model model, @ModelAttribute LoginModel loginModel){

        if (loginModel != null && loginModel.getId() != null && loginModel.getPass() != null) {
            String id = Base64.getEncoder().encodeToString(loginModel.getId().getBytes());
            String pass = Base64.getEncoder().encodeToString(loginModel.getPass().getBytes());

            ErrorCodes code = auth.login(id + ":" + pass);

            if (code == ErrorCodes.SUCCESS) {
                Cookie cookieId = new Cookie("auth.userId", id);
                Cookie cookiePass = new Cookie("auth.key", auth.generateKey(id));
                response.addCookie(cookieId);
                response.addCookie(cookiePass);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", code.name());
                model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
                return "login";
            }
        } else {
            model.addAttribute("error", "NULL_ERROR");
            model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("registerModel", new RegisterModel());
        return "register";
    }

    @PostMapping("/register-result")
    public String registerResult(Model model, @ModelAttribute RegisterModel registerModel){
        RegisterModel register = (RegisterModel) model.getAttribute("registerModel");
        if (register != null) {
            String id = Base64.getEncoder().encodeToString(register.getId().getBytes());
            String pass = Base64.getEncoder().encodeToString(register.getPass().getBytes());

            ErrorCodes code = auth.register(id + ":" + pass, new AdminModel(register.getId(), register.getEmail(), false, register.getName()));

            if (code == ErrorCodes.SUCCESS) {
                model.addAttribute("loginModel", new LoginModel());
                model.addAttribute("success", "REGISTERED");
                return "login";
            } else {
                model.addAttribute("error", code.name());
                return "register";
            }
        } else {
            model.addAttribute("error", "NULL_ERROR");
            return "register";
        }
    }

    @RequestMapping("/reset")
    public String resetPassword(Model model){
        model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
        return "reset";
    }

    @PostMapping("/reset-result")
    public String resetPasswordResult(Model model, @ModelAttribute LoginModel loginModel) {

        if (loginModel != null && loginModel.getId() != null) {
            ErrorCodes code = auth.sendResetPasswordMail(loginModel.getId());
            if (code == ErrorCodes.SUCCESS) {
                model.addAttribute("success", "RESET_LINK_SENT");
                model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
                return "login";
            } else {
                model.addAttribute("error", code.name());
                model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
                return "reset";
            }
        } else {
            model.addAttribute("error", "NULL_ERROR");
            model.addAttribute("loginModel", new LoginModel(StringUtils.EMPTY, StringUtils.EMPTY));
            return "reset";
        }
    }

    @PostMapping("/create-project-result")
    public RedirectView createProjectResult(@CookieValue(name = "auth.userId", required = false) String cookieId,
                                            @CookieValue(name = "auth.key", required = false) String cookieValue,
                                            @ModelAttribute ProjectModel newProject, RedirectAttributes attributes) {

//        if (auth.verify(id, key) == ErrorCodes.SUCCESS) {
            if (newProject != null && newProject.getId() != null && newProject.getName()!= null) {
                newProject.setOwnerId(new String(Base64.getDecoder().decode(cookieId.getBytes())));
                newProject.setUserSize(0);
                ErrorCodes code = projectService.registerProject(newProject);
                attributes.addFlashAttribute("alert", code.name());
            } else {
                attributes.addFlashAttribute("alert", "NULL_ERROR");
            }
            return new RedirectView("projects");
//         } else {
//            return "redirect:/login";
//        }
    }

    @RequestMapping("/project-details/{projectId}")
    public String getProjectDetails(@CookieValue(name = "auth.userId", required = false) String cookieId,
                                    @CookieValue(name = "auth.key", required = false) String cookieValue,
                                    @PathVariable(name = "projectId") String projectId, Model model) {
//        if (auth.verify(id, key) == ErrorCodes.SUCCESS) {
            ProjectDetailModel projectDetails = projectService.getProjectDetails(projectId, new String(Base64.getDecoder().decode(cookieId.getBytes())));
            model.addAttribute("prj", projectDetails);
            model.addAttribute("updatedPrj", projectDetails.getProjectModel());
            model.addAttribute("newAdmin", new ProjectAdminsModel());
            return "project-details";
//        } else {
//            return "redirect:/login";
//        }
    }

    @PostMapping("/update-project-result")
    public String updateProjectResult(@CookieValue(name = "auth.userId", required = false) String cookieId,
                                            @CookieValue(name = "auth.key", required = false) String cookieValue,
                                            @ModelAttribute ProjectModel updatedPrj) {

//        if (auth.verify(id, key) == ErrorCodes.SUCCESS) {
//        if (!updatedPrj.getName().isEmpty() && !updatedPrj.getOwnerId().isEmpty()) {
            projectService.updateProject(updatedPrj);
//        } else {
//            attributes.addFlashAttribute("alert", "NULL_ERROR");
//        }
        return "redirect:/project-details/" + updatedPrj.getId();
//         } else {
//            return "redirect:/login";
//        }
    }

    @PostMapping("/add-admin-result/{projectId}")
    public String addAdminResult(@CookieValue(name = "auth.userId", required = false) String cookieId,
                                 @CookieValue(name = "auth.key", required = false) String cookieValue,
                                 @PathVariable(name = "projectId") String projectId,
                                 @ModelAttribute ProjectAdminsModel newAdmin) {

//        if (auth.verify(id, key) == ErrorCodes.SUCCESS) {
//        if (!updatedPrj.getName().isEmpty() && !updatedPrj.getOwnerId().isEmpty()) {
        projectService.addAdmin(newAdmin, projectId);
//        } else {
//            attributes.addFlashAttribute("alert", "NULL_ERROR");
//        }
        return "redirect:/project-details/" + projectId;
//         } else {
//            return "redirect:/login";
//        }
    }

    @RequestMapping("/change-rights/READ/{adminId}/{projectId}")
    public String changeRightsToRead(@CookieValue(name = "auth.userId", required = false) String cookieId,
                               @CookieValue(name = "auth.key", required = false) String cookieValue,
                               @PathVariable(name = "projectId") String projectId,
                               @PathVariable(name = "adminId") String adminId) {

        projectService.updateAdmin(adminId, projectId, AdminRights.READ);
        return "redirect:/project-details/" + projectId;
    }

    @RequestMapping("/change-rights/WRITE/{adminId}/{projectId}")
    public String changeRightsToWrite(@CookieValue(name = "auth.userId", required = false) String cookieId,
                               @CookieValue(name = "auth.key", required = false) String cookieValue,
                               @PathVariable(name = "projectId") String projectId,
                               @PathVariable(name = "adminId") String adminId) {

        projectService.updateAdmin(adminId, projectId, AdminRights.WRITE);
        return "redirect:/project-details/" + projectId;
    }

    @RequestMapping("/delete-admin/{adminId}/{projectId}")
    public String deleteAdmin(@CookieValue(name = "auth.userId", required = false) String cookieId,
                               @CookieValue(name = "auth.key", required = false) String cookieValue,
                               @PathVariable(name = "projectId") String projectId,
                               @PathVariable(name = "adminId") String adminId) {

        projectService.deleteAdmin(adminId, projectId);
        return "redirect:/project-details/" + projectId;
    }

    @RequestMapping("/account-details/{adminId}")
    public String accountDetails(@CookieValue(name = "auth.userId", required = false) String cookieId,
                              @CookieValue(name = "auth.key", required = false) String cookieValue,
                              @PathVariable(name = "adminId") String adminId, Model model) {

        AdminModel details = adminService.getDetails(adminId);
        model.addAttribute("admin", details);
        return "account-details";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String id = "";
        String key = "";
        for (Cookie cookie : cookies) {
            if ("auth.userId".equals(cookie.getName())) {
                id = cookie.getValue();
            }
            if ("auth.key".equals(cookie.getName())) {
                key = cookie.getValue();
            }
        }
        Cookie cookieId = new Cookie("auth.userId", id);
        cookieId.setMaxAge(0);
        Cookie cookiePass = new Cookie("auth.key", key);
        cookiePass.setMaxAge(0);
        response.addCookie(cookieId);
        response.addCookie(cookiePass);
        return "redirect:/login";
    }
}