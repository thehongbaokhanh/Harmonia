package codegym.harmonia.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class SongRequest {
    private Long songId;
    private String title;
    private MultipartFile fileData;
    private MultipartFile coverData;
    private String file;
    private String cover;
    private Integer playCount;
    private Long artistId;
}
