package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.UserDTO;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.service.user.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user/signIn")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001","http://localhost:3003"})
public class UserController {
    @Autowired
    private IUserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/view/login")
    public String showLoginRegisterPage() {
        return "login/signIn"; // file auth.html trong templates
    }
    @GetMapping("/view/register")
    public String showRegisterPage() {
        return "/login/register";
    }
    @GetMapping("/clause")
    public String showClausePage() {
        return "login/clause";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {

        // Kiểm tra trùng email
        boolean emailExists = userService.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "login/signIn";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp!");
            return "login/signIn";
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(fullName); // dùng fullName làm username
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);
        user.setCreateAt(LocalDateTime.now());
        user.setAvatar("/images/default-avatar.png");

        userService.save(user);

        model.addAttribute("message", "Đăng ký thành công! Bây giờ bạn có thể đăng nhập.");
        return "login/signIn";
    }

    @PostMapping("/login")
    public Object loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        // Tìm user theo email
        List<User> users = userService.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .toList();

        if (users.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy người dùng!");
            return "login/signIn";
        }

        User user = users.get(0);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Mật khẩu không hợp lệ!");
            return "login/signIn";
        }

        // Lưu session
        session.setAttribute("currentUser", user);

        // ==============================
        // ✅ Điều kiện phân trang theo role
        // ==============================
        if ("ADMIN".equalsIgnoreCase(String.valueOf(user.getRole()))) {
            // chuyển hướng sang trang admin
            return new RedirectView("/admin/songs");
            // Hoặc React admin: http://localhost:3003/admin
        }

        // Nếu là user thường → về trang chủ React
        String frontendHomeUrl = "http://localhost:3003/";
        return new RedirectView(frontendHomeUrl);
    }


}
