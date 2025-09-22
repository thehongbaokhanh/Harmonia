package codegym.harmonia.controller.admin;
import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/songs")
public class AdminSongController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ISongService songService;

    @GetMapping("")
    public String listSongs(Model model) {
        List<SongDTO> songs = songService.getAllSongs();
        model.addAttribute("songs", songs);
        return "admin/song-list";
    }

    @GetMapping("/add")
    public String addSongForm(Model model) {
        model.addAttribute("song", new SongRequest());
        return "admin/song-form";
    }

    @GetMapping("/edit/{id}")
    public String editSongForm(@PathVariable Long id, Model model) {
        Song song = songService.findById(id);
        if (song == null) {
            return "redirect:/admin/songs";
        }
        SongRequest req = new SongRequest();
        req.setSongId(song.getSongId());
        req.setTitle(song.getTitle());
        req.setFile(song.getFile());
        req.setCover(song.getCover());
        req.setPlayCount(song.getPlayCount());
        req.setArtistId(song.getArtist().getUserId());
        model.addAttribute("song", req);
        return "admin/song-form";
    }

    @PostMapping("/save")
    public String saveSong(@ModelAttribute SongRequest songRequest,
                           @RequestParam(value = "audioFile", required = false) MultipartFile audioFile) {
        try {
            if (audioFile != null && !audioFile.isEmpty()) {
                byte[] bytes = audioFile.getBytes();
                Path path = Paths.get(UPLOAD_DIR + audioFile.getOriginalFilename());
                Files.createDirectories(path.getParent());
                Files.write(path, bytes);
                songRequest.setFile("/" + path.toString());
            }
            songService.save(songRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/songs";
    }

    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        songService.delete(id);
        return "redirect:/admin/songs";
    }
}
