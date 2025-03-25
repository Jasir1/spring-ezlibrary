package com.mhdjasir.ezLibrary.domain.security.repos;

import com.mhdjasir.ezLibrary.domain.security.entity.User;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndStatusAndDelete(String email,boolean status, boolean delete);

    List<User> findAllByUserRole(UserRole userRole);

    Optional<User> findByMobile(String mobile);
}
