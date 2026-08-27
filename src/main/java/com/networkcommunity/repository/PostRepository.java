package com.networkcommunity.repository;

import com.networkcommunity.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT p
        FROM Post p
        WHERE p.user.id = :userId
           OR p.user.id IN (
               SELECT CASE
                   WHEN fr.sender.id = :userId THEN fr.receiver.id
                   ELSE fr.sender.id
               END
               FROM FriendRequest fr
               WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId)
                 AND fr.status = com.networkcommunity.entity.FriendRequestStatus.ACCEPTED
           )
        ORDER BY p.createdAt DESC
    """)
    List<Post> findFeedPosts(@Param("userId") Long userId);
}