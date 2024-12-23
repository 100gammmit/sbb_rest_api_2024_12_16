package dev.spring.sbbpart2and3.controller.admin;

import dev.spring.sbbpart2and3.domain.Role;
import dev.spring.sbbpart2and3.service.RoleService;
import dev.spring.sbbpart2and3.service.UserSecurityService;
import dev.spring.sbbpart2and3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.relation.RoleNotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public AdminController(UserSecurityService userSecurityService, UserService userService, RoleService roleService) {
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam(value = "role") String roleName,
                             @RequestParam(value = "userId") String username, RedirectAttributes redirectAttributes) {
        try {
            Role role = roleService.getRoleByRoleName(roleName);
            userService.assignRoleToUser(username, role);
            redirectAttributes.addFlashAttribute("successMessage", "권한이 성공적으로 부여되었습니다.");
        } catch (RoleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 권한을 찾을 수 없습니다: " + roleName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한 부여 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/manage/role";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/removeRole")
    public String removeRole(@RequestParam(value = "role") String roleName,
                             @RequestParam(value = "userId") String username, RedirectAttributes redirectAttributes) throws RoleNotFoundException {
        try {
            Role role = roleService.getRoleByRoleName(roleName);
            userService.removeRoleFromUser(username, role);
            redirectAttributes.addFlashAttribute("successMessage", "권한이 성공적으로 삭제되었습니다.");
        } catch (RoleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 권한을 찾을 수 없습니다: " + roleName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한 삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/admin/manage/role";
    }

}
