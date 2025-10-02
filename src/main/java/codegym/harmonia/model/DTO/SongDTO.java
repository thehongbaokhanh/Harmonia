package codegym.harmonia.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long songId;
    private String title;
    private String file;
    private String cover;
    private Integer playCount;
    private LocalDateTime createdAt;
    private String artistName;
    private Integer duration;
}
