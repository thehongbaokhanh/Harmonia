package codegym.harmonia.service.song;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.IGeneralService;

import java.util.List;

public interface ISongService extends IGeneralService<Song> {
    Song save(SongRequest songRequest);
    List<SongDTO> getAllSongs();
}
