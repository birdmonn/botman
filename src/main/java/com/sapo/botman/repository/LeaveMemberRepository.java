package com.sapo.botman.repository;

import com.sapo.botman.model.LeaveMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveMemberRepository extends JpaRepository<LeaveMember, Long> {

//    @Query("SELECT mj FROM MemberJOB mj WHERE mj.userId = ?1 ")
//    MemberJOB findByUserId(String userId);
}
