package codegym.harmonia.controller;

import codegym.harmonia.model.Song;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/homeWorkSpace")
//@CrossOrigin(Origin = "http://localhost:3000")
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> findAll() {
        List<Song> songs = songService.findAll();
        if (songs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Song> save(Song song) {
        return new ResponseEntity<>(songService.save(song), HttpStatus.CREATED);
    }

}
