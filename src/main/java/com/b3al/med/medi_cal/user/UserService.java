package com.b3al.med.medi_cal.user;

import com.b3al.med.medi_cal.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    Page<UserDTO> findAll(String filter, Pageable pageable);

    UserDTO get(Long id);

    Long create(UserDTO userDTO);

    void update(Long id, UserDTO userDTO);

    void delete(Long id);

    boolean usernameExists(String username);

    ReferencedWarning getReferencedWarning(Long id);

}
