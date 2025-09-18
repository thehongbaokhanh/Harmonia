package codegym.harmonia.service.song;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.IGeneralService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface ISongService extends IGeneralService<Song> {
    Song save(SongRequest songRequest) throws IOException;
    List<SongDTO> getAllSongs();
}
