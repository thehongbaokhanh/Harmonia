package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.model.User;
import codegym.harmonia.service.authenticate.IAuthenticateService;
import codegym.harmonia.service.song.ISongService;
import codegym.harmonia.service.song.SongService;
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
import java.net.MalformedURLException;
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
    private ISongService iSongService;

    @Autowired
    private SongService songService;

    @Autowired
    private IAuthenticateService authenticateService;

    @GetMapping
    public ResponseEntity<List<SongDTO>> findAll() {
        return ResponseEntity.ok(iSongService.getAllSongs());
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveSong(
            @RequestParam("title") String title,
            @RequestParam("artistId") Long artistId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("cover") MultipartFile cover
    ) {
        try {
            SongRequest songRequest = new SongRequest();
            songRequest.setTitle(title);
            songRequest.setFile(file);
            songRequest.setCover(cover);
            songRequest.setArtistId(artistId);
            songRequest.setPlayCount(0);
            return ResponseEntity.ok(iSongService.save(songRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Song> updateSong(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("artistId") Long artistId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("cover") MultipartFile cover
    ) throws IOException {
        Optional<Song> optionalSong = iSongService.findById(id);
        if (optionalSong.isPresent()) {
            SongRequest updatedSong = new SongRequest();
            updatedSong.setPlayCount(optionalSong.get().getPlayCount());
            System.out.println(updatedSong.getPlayCount());
            updatedSong.setSongId(id);
            updatedSong.setTitle(title);
            updatedSong.setFile(file);
            updatedSong.setCover(cover);
            updatedSong.setArtistId(artistId);
            return ResponseEntity.ok(iSongService.save(updatedSong));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<Resource> stream(@PathVariable Long id) {
        Song song = iSongService.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        Resource resource;
        try {
            resource = songService.loadAsResource(song.getFile(), false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 3️⃣ Chuẩn bị headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getFile() + "\"");
        headers.set(HttpHeaders.CONTENT_TYPE, song.getContentType() != null ? song.getContentType() : "audio/mpeg");
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(song.getSize()));

        // 4️⃣ Trả về ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
