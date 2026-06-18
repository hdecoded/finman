package com.hdecoded.finman.repository;

import com.hdecoded.finman.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // Select * from tbl_profiles where email = ?
    Optional<ProfileEntity> findByEmail(String email);

    // Select * from tbl_profile where activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);

}
