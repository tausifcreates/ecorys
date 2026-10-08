package com.tausifk.ecorys.application;

import com.tausifk.ecorys.common.ConflictException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping({"/", "/applications/new"})
    public String form(Model model) {
        model.addAttribute("form", new ApplicationForm());
        return "application-form";
    }

    @PostMapping("/applications")
    public String submit(@Valid @ModelAttribute("form") ApplicationForm form, BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "application-form";
        }
        Application application;
        try {
            application = service.submit(form);
        } catch (ConflictException e) {
            result.reject("conflict", e.getMessage());
            return "application-form";
        }
        redirect.addFlashAttribute("submittedId", application.getId());
        return "redirect:/applications/new";
    }

    @GetMapping("/applications")
    public String list(Model model) {
        model.addAttribute("applications", service.findAll());
        return "applications";
    }

    @GetMapping("/reports/approved-applications")
    public String approvedReport(@RequestParam(defaultValue = "desc") String sort, Model model) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sort).orElse(Sort.Direction.DESC);
        model.addAttribute("rows", service.approvedReport(direction));
        model.addAttribute("asc", direction.isAscending());
        return "approved-report";
    }

    @PostMapping("/applications/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirect) {
        service.approve(id);
        redirect.addFlashAttribute("message", "Application #" + id + " approved.");
        return "redirect:/applications";
    }

    @PostMapping("/applications/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes redirect) {
        service.reject(id);
        redirect.addFlashAttribute("message", "Application #" + id + " rejected.");
        return "redirect:/applications";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(ConflictException e, RedirectAttributes redirect) {
        redirect.addFlashAttribute("error", e.getMessage());
        return "redirect:/applications";
    }
}
