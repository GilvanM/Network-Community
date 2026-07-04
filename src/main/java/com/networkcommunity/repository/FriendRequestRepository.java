package com.networkcommunity.repository;

import com.networkcommunity.entity.FriendRequest;
import com.networkcommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // único método correto para os dois lados
    @Query("""
        SELECT fr
        FROM FriendRequest fr
        WHERE (fr.sender = :user1 AND fr.receiver = :user2)
           OR (fr.sender = :user2 AND fr.receiver = :user1)
    """)
    Optional<FriendRequest> findBetweenUsers(
            @Param("user1") User user1,
            @Param("user2") User user2
    );
}