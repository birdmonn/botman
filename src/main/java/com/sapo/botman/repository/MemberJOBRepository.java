package com.sapo.botman.repository;

import com.sapo.botman.model.MemberJOB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJOBRepository extends JpaRepository<MemberJOB, Long> {

    @Query("SELECT mj FROM MemberJOB mj WHERE mj.userId = ?1 ")
    MemberJOB findByUserId(String userId);
}
