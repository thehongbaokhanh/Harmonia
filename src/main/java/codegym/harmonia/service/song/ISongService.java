package codegym.harmonia.service.song;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.service.IGeneralService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ISongService extends IGeneralService<Song> {
    Song save(SongRequest songRequest) throws IOException;
    List<SongDTO> getAllSongs();
    SongDTO findSongById(Long id);
    Optional<Song> findById(Long id);
    void delete(Long id);
}
