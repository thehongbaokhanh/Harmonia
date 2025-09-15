package codegym.harmonia.service.song;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.repository.IAuthenticateRepository;
import codegym.harmonia.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private ISongRepository songRepository;

    @Autowired
    private IAuthenticateRepository authenticateRepository;

    @Override
    public List<SongDTO> getAllSongs() {
        return songRepository.findAll()
                .stream()
                .map(song -> new SongDTO(
                        song.getSongId(),
                        song.getTitle(),
                        song.getFile(),
                        song.getCover(),
                        song.getPlayCount(),
                        song.getCreatedAt(),
                        song.getArtist().getUsername()
                ))
                .toList();
    }

    @Override
    public List<Song> findAll() {
        return null;
    }

    @Override
    public Song findById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    @Override
    public Song save(Song song) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Song save(SongRequest songRequest) {
        System.out.println(songRequest.getArtistId());
        User artist = authenticateRepository.findById(songRequest.getArtistId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(artist.getUserId());

        if (artist.getRole() != UserRole.ARTIST) {
            throw new RuntimeException("User is not an ARTIST");
        }

        Song song;
        if (songRequest.getSongId() != null) {
            song = songRepository.findById(songRequest.getSongId())
                    .orElseThrow(() -> new RuntimeException("Song not found"));
            song.setTitle(songRequest.getTitle());
            song.setFile(songRequest.getFile());
            song.setCover(songRequest.getCover());
            song.setPlayCount(songRequest.getPlayCount());
            song.setArtist(artist);
        } else {    
            song = Song.builder()
                    .title(songRequest.getTitle())
                    .file(songRequest.getFile())
                    .cover(songRequest.getCover())
                    .playCount(songRequest.getPlayCount())
                    .artist(artist)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        return songRepository.save(song);
    }
}
