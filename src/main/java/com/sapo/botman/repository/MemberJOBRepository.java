package com.sapo.botman.repository;

import com.sapo.botman.model.MemberJOB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJOBRepository extends JpaRepository<MemberJOB, Long> {

//    @Query("SELECT qp FROM QuestPokemonGo qp WHERE fr.path = ?1 ")
//    FileReport findByPath(String path);
}
