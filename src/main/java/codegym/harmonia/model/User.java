package codegym.harmonia.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING) // lưu dạng chuỗi "ADMIN", "USER", "ARTIST"
    private UserRole role;

    private String avatar;
    private LocalDateTime create_at;

    // 1-N: 1 nghệ sĩ có thể sáng tác nhiều bài hát
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs;

    // 1-N: 1 user có nhiều favorite
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;
}