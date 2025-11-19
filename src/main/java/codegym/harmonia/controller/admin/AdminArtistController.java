package codegym.harmonia.controller.admin;

import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/artists")
public class AdminArtistController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String listArtists(Model model) {
        List<User> artists = userService.findByRole(UserRole.ARTIST);
        model.addAttribute("artists", artists);
        return "user/artist-list";
    }

    // 👉 Thêm mới nghệ sĩ
    @GetMapping("/add")
    public String addArtistForm(Model model) {
        model.addAttribute("artist", new User());
        return "user/artist-form";
    }

    @GetMapping("/edit/{id}")
    public String editArtistForm(@PathVariable Long id, Model model) {
        User artist = userService.findById(id);
        if (artist == null || artist.getRole() != UserRole.ARTIST) {
            return "redirect:/admin/artists";
        }
        model.addAttribute("artist", artist);
        return "user/artist-form";
    }

    @PostMapping("/save")
    public String saveArtist(@ModelAttribute User artist) {
        artist.setRole(UserRole.ARTIST); // đảm bảo luôn là ARTIST
        userService.save(artist);
        return "redirect:/admin/artists";
    }

    @GetMapping("/delete/{id}")
    public String deleteArtist(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/artists";
    }
}
