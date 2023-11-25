package com.vietquan.security.repository;

import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);

}
