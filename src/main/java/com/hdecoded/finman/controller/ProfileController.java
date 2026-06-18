package com.hdecoded.finman.controller;

import com.hdecoded.finman.dto.ProfileDTO;
import com.hdecoded.finman.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam("token") String activationToken) {
        boolean isActivated = profileService.activateProfile(activationToken);
        if (isActivated) {
            return ResponseEntity.ok("Profile Activated Successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile Activation Failed");
        }
    }
}
