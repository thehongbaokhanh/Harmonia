package codegym.harmonia.service.song;

import codegym.harmonia.model.DTO.SongDTO;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.SongRequest;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.repository.IAuthenticateRepository;
import codegym.harmonia.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SongService implements ISongService {

    private final Path musicFolder;
    private final Path coverFolder;

    @Autowired
    private ISongRepository songRepository;

    @Autowired
    private IAuthenticateRepository authenticateRepository;

    public SongService(@Value("${file.upload-dir}") String storageLocation, ISongRepository repo) {
        Path rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
        this.musicFolder = rootLocation.resolve("music");
        this.coverFolder = rootLocation.resolve("cover");
        this.songRepository = repo;

        try {
            Files.createDirectories(musicFolder);
            Files.createDirectories(coverFolder);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directories", e);
        }
    }

    @Override
    public List<SongDTO> getAllSongs() {
        return songRepository.findAll()
                .stream()
                .map(song -> new SongDTO(
                        song.getSongId(),
                        song.getTitle(),
                        song.getFile(),
                        "/uploads/cover/" + song.getCover(), // Thêm prefix đúng
                        song.getPlayCount(),
                        song.getCreatedAt(),
                        song.getArtist().getUsername(),
                        song.getDuration()
                ))

                .toList();
    }
    @Override
    public SongDTO findSongById(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            return new SongDTO(
                    song.getSongId(),
                    song.getTitle(),
                    song.getFile(),
                    "/uploads/cover/" + song.getCover(), // Thêm prefix
                    song.getPlayCount(),
                    song.getCreatedAt(),
                    song.getArtist().getUsername(),
                    song.getDuration()
            );
        } else {
            return null; // hoặc throw exception
        }
    }

    public Resource loadAsResource(String filename, boolean isCover) {
        try {
            Path folder = isCover ? coverFolder : musicFolder;
            Path filePath = folder.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) return resource;
            throw new RuntimeException("File not found: " + filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL for file: " + filename, e);
        }
    }


    @Override
    public List<Song> findAll() {
        return null;
    }

    @Override
    public Optional<Song>  findById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Song save(Song song) {
        return null;
    }

    @Override
    public void delete(Long id) {
        songRepository.deleteById(id);
    }

    @Override
    public Song save(SongRequest songRequest) throws IOException {
        // Lấy artist
        User artist = authenticateRepository.findById(songRequest.getArtistId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (artist.getRole() != UserRole.ARTIST) {
            throw new RuntimeException("User is not an ARTIST");
        }

        MultipartFile file = songRequest.getFileData();
        MultipartFile cover = songRequest.getCoverData();

        String filename = null;
        String coverFilename = null;

        if (file != null && !file.isEmpty()) {
            filename = System.currentTimeMillis() + "_" + Paths.get(file.getOriginalFilename()).getFileName().toString();
            Path target = musicFolder.resolve(filename); // Lưu vào music folder
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        if (cover != null && !cover.isEmpty()) {
            coverFilename = System.currentTimeMillis() + "_" + Paths.get(cover.getOriginalFilename()).getFileName().toString();
            Path targetCover = coverFolder.resolve(coverFilename); // Lưu vào cover folder
            try (InputStream in = cover.getInputStream()) {
                Files.copy(in, targetCover, StandardCopyOption.REPLACE_EXISTING);
            }
        }


        Song song;
        if (songRequest.getSongId() != null) {
            // Update
            song = songRepository.findById(songRequest.getSongId())
                    .orElseThrow(() -> new RuntimeException("Song not found"));
            song.setTitle(songRequest.getTitle() != null ? songRequest.getTitle() : song.getTitle());
            if (filename != null) {
                song.setFile(filename);
                song.setContentType(file.getContentType());
                song.setSize(file.getSize());
            }
            if (coverFilename != null) {
                song.setCover(coverFilename);
            }
            song.setPlayCount(songRequest.getPlayCount());
            song.setArtist(artist);
        } else {
            // Tạo mới
            song = Song.builder()
                    .title(songRequest.getTitle() != null ? songRequest.getTitle() : (file != null ? file.getOriginalFilename() : "Unknown"))
                    .artist(artist)
                    .file(filename)
                    .contentType(file != null ? file.getContentType() : null)
                    .size(file != null ? file.getSize() : 0)
                    .cover(coverFilename)
                    .playCount(songRequest.getPlayCount())
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        return songRepository.save(song);
    }

}
