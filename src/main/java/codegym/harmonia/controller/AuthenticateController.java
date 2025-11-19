package codegym.harmonia.controller;

import codegym.harmonia.model.DTO.LoginRequest;
import codegym.harmonia.model.DTO.UserDTO;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.service.authenticate.IAuthenticateService;
import codegym.harmonia.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Controller
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


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) {
        if (user.getUserId() != null) {
            return new ResponseEntity<>(authenticateService.save(user), HttpStatus.OK);
        }
        user.setCreateAt( java.time.LocalDateTime.now());
        return new ResponseEntity<>(authenticateService.save(user), HttpStatus.CREATED);
    }
}
