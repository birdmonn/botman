package com.sapo.botman.repository;

import com.sapo.botman.model.QuestPokemonGo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestPokemonGoRepository extends JpaRepository<QuestPokemonGo, Long> {

//    @Query("SELECT qp FROM QuestPokemonGo qp WHERE fr.path = ?1 ")
//    FileReport findByPath(String path);
}
