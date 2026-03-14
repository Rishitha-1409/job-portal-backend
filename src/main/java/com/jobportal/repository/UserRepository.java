package com.jobportal.repository;

import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRole(User.Role role, Pageable pageable);

    Page<User> findByAccountStatus(User.AccountStatus status, Pageable pageable);

    @Query("""
        SELECT u FROM User u
        WHERE (:role IS NULL OR u.role = :role)
        AND (:status IS NULL OR u.accountStatus = :status)
        AND (:keyword IS NULL
             OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(u.email)    LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<User> searchUsers(
        @Param("keyword") String keyword,
        @Param("role")    User.Role role,
        @Param("status")  User.AccountStatus status,
        Pageable pageable
    );

    long countByRole(User.Role role);

    long countByAccountStatus(User.AccountStatus status);
}
