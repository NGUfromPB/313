package nur.Controller;

import nur.models.User;
import nur.service.RoleServ;
import nur.service.UserServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServ userServ;
    private final RoleServ roleServ;

    @Autowired()
    public AdminController(UserServ userServ, RoleServ roleServ) {
        this.roleServ=roleServ;
        this.userServ = userServ;
    }

    @GetMapping("/")
    public String users(Principal principal, Model model) {
        model.addAttribute("users", userServ.getAllUsers());
        User user = userServ.findByUsername(principal.getName());
        model.addAttribute("current",user);
        model.addAttribute("roles",roleServ.getAllRoles());
        return "adminPage";
    }

    @GetMapping("/new")
    public String addUser(Principal principal, Model model, @ModelAttribute("user") User user) {
        User user1 = userServ.findByUsername(principal.getName());
        model.addAttribute("current",user1);
        model.addAttribute("roles",roleServ.getAllRoles());
        return "newUser";
    }

    @PostMapping("/new")
    public String add(@RequestParam("role") ArrayList<Long> roles,
                      @ModelAttribute("user") User user) {
        user.setRoles(roleServ.getRolesById(roles));
        userServ.addUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userServ.removeUser(id);
        return "redirect:/admin/";
    }

    @PatchMapping("/edit/{id}")
    public String update(@RequestParam("role")ArrayList<Long> roles,
                         User user,
                         @PathVariable("id") long id, Model model) {
        model.addAttribute("roles",roleServ.getAllRoles());
        user.setRoles(roleServ.getRolesById(roles));
        user.setUserId(id);
        userServ.addUser(user);
        return "redirect:/admin/";
    }
}
