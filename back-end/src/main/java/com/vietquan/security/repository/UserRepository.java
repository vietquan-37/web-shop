package com.vietquan.security.repository;

import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, PagingAndSortingRepository<User,Integer> {


    Optional<User> findByEmail(String email);
    Page<User> findAllByRole(Role role, Pageable pageable);

}
