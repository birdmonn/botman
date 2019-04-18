package com.sapo.botman.repository;

import com.sapo.botman.model.LeaveMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveMemberRepository extends JpaRepository<LeaveMember, Long> {

    @Query("SELECT lm FROM LeaveMember lm WHERE lm.memberJOB.id = ?1 ")
    List<LeaveMember> findByMemberJOBId(Long memberJOBId);
}
