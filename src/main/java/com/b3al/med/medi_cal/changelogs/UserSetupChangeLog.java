package com.b3al.med.medi_cal.changelogs;

import com.b3al.med.medi_cal.user.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@AllArgsConstructor
public class UserSetupChangeLog {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void setupAdminUser() {
        if (userRepository.findByUsernameIgnoreCase("admin") == null) {

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("admin");
            userDTO.setPassword("admin");
            userDTO.setRole(UserRole.ADMIN);
            userDTO.setSpecialization("ADMIN SI");
            userDTO.setStatus(UserStatus.ACTIVE);
            userDTO.setFirstname("shems");
            userDTO.setLastname("chelgoui");

            final User user = new User();
            userMapper.updateUser(userDTO, user, passwordEncoder);
            userRepository.save(user);
        }
    }
}
