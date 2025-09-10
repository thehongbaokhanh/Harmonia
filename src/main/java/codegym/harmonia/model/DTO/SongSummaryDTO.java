package codegym.harmonia.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongSummaryDTO {
    private Long songId;
    private String title;
}