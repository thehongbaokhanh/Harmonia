package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.LoginRequest;
import codegym.harmonia.model.DTO.UserDTO;
import codegym.harmonia.model.User;
import codegym.harmonia.service.authenticate.IAuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authenticate")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AuthenticateController {
    @Autowired
    private IAuthenticateService authenticateService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = authenticateService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(authenticateService::getUserDTO)
                .toList();

        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Tìm user theo email
        User user = authenticateService.findByEmail(request.getEmail())
                .stream()
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("Email không tồn tại!");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Sai mật khẩu!");
        }

        // Trả về UserDTO (ẩn mật khẩu)
        UserDTO dto = authenticateService.getUserDTO(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        if (user.getUserId() != null) {
            return new ResponseEntity<>(authenticateService.save(user), HttpStatus.OK);
        }
        user.setCreateAt( java.time.LocalDateTime.now());
        return new ResponseEntity<>(authenticateService.save(user), HttpStatus.CREATED);
    }
}
