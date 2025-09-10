package codegym.harmonia.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SongRequest {
    private Long songId;
    private String title;
    private String file;
    private String cover;
    private Integer playCount;
    private Long artistId;
}
