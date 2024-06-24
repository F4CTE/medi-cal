package com.b3al.med.medi_cal.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findAllById(Long id, Pageable pageable);

    boolean existsByUsernameIgnoreCase(String username);

}
