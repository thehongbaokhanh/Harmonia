package codegym.harmonia.controller;

import codegym.harmonia.model.Song;
import codegym.harmonia.service.song.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homeWorkSpace")
//@CrossOrigin(Origin = "http://localhost:3000")
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping("/")
    public List<Song> findAll() {
        return songService.findAll();
    }
    
}
