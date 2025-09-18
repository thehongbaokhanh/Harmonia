package codegym.harmonia.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class SongRequest {
    private Long songId;
    private String title;
    private MultipartFile file;
    private MultipartFile cover;
    private Integer playCount;
    private Long artistId;
}
