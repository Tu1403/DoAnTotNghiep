package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.model.Jobboard;
import website.code.coffeeShop.model.UsersJob;
import website.code.coffeeShop.service.JobboardService;
import website.code.coffeeShop.service.UsersJobService;

import java.security.Principal;

@Controller
public class JobboardController {

    @Autowired
    private JobboardService jobboardService;

    @Autowired
    private UsersJobService usersJobService;

    @GetMapping("/management/jobboard")
    public String viewJobboard(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            Model model, Principal principal) {

        // Fetch user information if logged in
        if (principal != null) {
            String username = principal.getName();
            UsersJob user = usersJobService.findByUsername(username);
            model.addAttribute("user", user);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Jobboard> jobboardPage;
        boolean noNames = false;

        // Perform search or return empty page if search is null or empty
        if (search == null || search.isEmpty()) {
            jobboardPage = jobboardService.getAllJobboards(pageable);
            noNames = false; // Show full list, so no "No searching info" message needed
        } else {
            jobboardPage = jobboardService.searchJobboards(search, pageable);
            noNames = jobboardPage.isEmpty(); // Check if search results are empty
        }


        // Add attributes to the model
        model.addAttribute("jobboards", jobboardPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobboardPage.getTotalPages());
        model.addAttribute("totalItems", jobboardPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("noNames", noNames); // This will be used to conditionally show the message

        return "jobBoard-management";
    }

    @PostMapping("/management/jobboard/updatePresents")
    public String updatePresents(@RequestParam("id") int jobboardId) {
        jobboardService.incrementPresents(jobboardId);
        return "redirect:/management/jobboard"; // Redirect to the job board page
    }

    @PostMapping("/management/jobboard/updateAbsents")
    public String updateAbsents(@RequestParam("id") int jobboardId) {
        jobboardService.incrementAbsents(jobboardId);
        return "redirect:/management/jobboard"; // Redirect to the job board page
    }

    @PostMapping("/management/jobboard/updateShift")
    public String updateShift(@RequestParam("id") int jobboardId,
                              @RequestParam("shift") int shift,
                              RedirectAttributes redirectAttributes) {
        jobboardService.updateShift(jobboardId, shift);
        redirectAttributes.addFlashAttribute("message", "Cập nhật ca làm thành công!");
        return "redirect:/management/jobboard"; // Redirect to the job board page
    }
}