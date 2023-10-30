package com.vietquan.security.repository;

import com.vietquan.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer>{
    @Query(
            """
                    select t from Token t inner join User u on t.user.Id=u.Id
                    where u.Id=:userId and (t.expired= false or t.revoked = false)
                    """
    )
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
