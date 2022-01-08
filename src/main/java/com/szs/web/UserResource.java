package com.szs.web;

import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import com.szs.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@Transactional
public class UserResource {
    private Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User: {}", user);

        if (user.getId() != null) {
            throw new RuntimeException("A new user cannot already have an ID");
        }

        User result = userRepository.save(user);

        return ResponseEntity.created(new URI("/api/users" + result.getId())).body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<UserDTO> getUser() throws URISyntaxException {
        log.debug("REST request to get User");

        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUserId).map(UserDTO::new)
                .map((response) -> ResponseEntity.ok(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}