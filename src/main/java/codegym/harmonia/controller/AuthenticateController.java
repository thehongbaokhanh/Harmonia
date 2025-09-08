package codegym.harmonia.controller;

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
        return new ResponseEntity<>(authenticateService.save(user), HttpStatus.CREATED);
    }
}
