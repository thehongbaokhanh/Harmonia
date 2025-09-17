package codegym.harmonia.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeWorkSpace")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001","http://localhost:3002"})
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping
    public ResponseEntity<List<SongDTO>> findAll() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> findById(@PathVariable Long id) {
        SongDTO song = songService.findSongById(id);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(song);
    }


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
