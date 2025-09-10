package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/homeWorkSpace")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping
    public ResponseEntity<List<SongDTO>> findAll() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("File rỗng, vui lòng chọn file mp3.");
//        }
//
//        try {
//            // Trả file trực tiếp về client (không lưu vào server)
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() + "\"")
//                    .body(file.getBytes());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Lỗi khi đọc file: " + e.getMessage());
//        }
//    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSong(@RequestBody SongRequest songRequest) {
        System.out.println(songRequest);
        try {
            Song savedSong = songService.save(songRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSong);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
