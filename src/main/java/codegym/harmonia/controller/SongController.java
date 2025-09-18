package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.model.User;
import codegym.harmonia.service.authenticate.IAuthenticateService;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/homeWorkSpace")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SongController {
    @Autowired
    private ISongService songService;

    @Autowired
    private IAuthenticateService authenticateService;

    @GetMapping
    public ResponseEntity<List<SongDTO>> findAll() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveSong(
            @RequestParam("title") String title,
            @RequestParam("artistId") Long artistId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("cover") MultipartFile cover
    ) {
        if (file == null || file.isEmpty() || cover == null || cover.isEmpty()) {
            return ResponseEntity.badRequest().body("File hoặc cover bị thiếu");
        }

        try {
            User artist = authenticateService.findById(artistId);

            // validate types
            if (file.getContentType() == null || !file.getContentType().startsWith("audio/"))
                return ResponseEntity.badRequest().body("File phải là audio");
            if (cover.getContentType() == null || !cover.getContentType().startsWith("image/"))
                return ResponseEntity.badRequest().body("Cover phải là ảnh");

            // sanitize + unique name
            String fileExt = Optional.ofNullable(file.getOriginalFilename())
                    .map(n -> n.contains(".") ? n.substring(n.lastIndexOf(".")) : "")
                    .orElse("");
            String coverExt = Optional.ofNullable(cover.getOriginalFilename())
                    .map(n -> n.contains(".") ? n.substring(n.lastIndexOf(".")) : "")
                    .orElse("");

            String newFileName = UUID.randomUUID().toString() + fileExt;
            String newCoverName = UUID.randomUUID().toString() + coverExt;

            Path songDir = Paths.get("static/uploads/file");
            Path coverDir = Paths.get("static/uploads/images");
            Files.createDirectories(songDir);
            Files.createDirectories(coverDir);

            Path savedSongPath = songDir.resolve(newFileName);
            Path savedCoverPath = coverDir.resolve(newCoverName);

            Files.copy(file.getInputStream(), savedSongPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(cover.getInputStream(), savedCoverPath, StandardCopyOption.REPLACE_EXISTING);

            // tạo SongRequest trước
            SongRequest songRequest = new SongRequest();
            songRequest.setTitle(title);
            songRequest.setFile("static/uploads/file/" + newFileName);
            songRequest.setCover("static/uploads/images/" + newCoverName);
            songRequest.setPlayCount(0);
            songRequest.setArtistId(artistId);

            // convert sang Song (ở service)
            Song saved = songService.save(songRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi upload file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<Resource> streamSong(@PathVariable String fileName) {
        try {
            // Đường dẫn tới folder chứa nhạc
            Path filePath = Paths.get("static/uploads/file").resolve(fileName).normalize();

            // Kiểm tra tồn tại
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Lấy file dưới dạng Resource
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg")) // có thể đổi sang "audio/wav"
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
