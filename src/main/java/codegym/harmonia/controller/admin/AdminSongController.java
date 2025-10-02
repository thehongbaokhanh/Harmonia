package codegym.harmonia.controller.admin;
import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller

@RequestMapping("/admin/songs")
public class AdminSongController {
    @Value("${file.upload-dir}")
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";
    private static final String MUSIC_DIR = "music";
    private static final String COVER_DIR = "cover";

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
        Optional<Song> songOptional = songService.findById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            SongRequest req = new SongRequest();

            // Gán dữ liệu từ Song entity vào SongRequest
            req.setSongId(song.getSongId());
            req.setTitle(song.getTitle());
            req.setFile(song.getFile()); // Đảm bảo gán đường dẫn file cũ
            req.setCover(song.getCover()); // Đảm bảo gán đường dẫn ảnh cũ
            req.setPlayCount(song.getPlayCount());
            req.setArtistId(song.getArtist().getUserId());

            model.addAttribute("song", req);
            return "admin/song-form";
        } else {
            return "redirect:/admin/songs";
        }
    }

    @PostMapping("/save")
    public String saveSong(@ModelAttribute SongRequest songRequest) {
        try {
            // Lưu file nhạc
            MultipartFile audioFile = songRequest.getFileData();
            if (audioFile != null && !audioFile.isEmpty()) {
                String originalFileName = audioFile.getOriginalFilename();
                String safeFileName = originalFileName.replace(" ", "_");

                Path filePath = Paths.get(UPLOAD_DIR, MUSIC_DIR, safeFileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, audioFile.getBytes());

                songRequest.setFile("/uploads/" + MUSIC_DIR + "/" + safeFileName);
            }

            // Lưu file cover
            MultipartFile coverFile = songRequest.getCoverData();
            if (coverFile != null && !coverFile.isEmpty()) {
                String originalCoverName = coverFile.getOriginalFilename();
                String safeCoverName = System.currentTimeMillis() + "_" + originalCoverName.replace(" ", "_");

                Path coverPath = Paths.get(UPLOAD_DIR, COVER_DIR, safeCoverName);
                Files.createDirectories(coverPath.getParent());
                Files.write(coverPath, coverFile.getBytes());

                // 👉 Lưu tên file thôi, không kèm /uploads/cover/
                songRequest.setCover(safeCoverName);
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
