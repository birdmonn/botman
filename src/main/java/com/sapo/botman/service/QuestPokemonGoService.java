package com.sapo.botman.service;

import com.sapo.botman.model.QuestPokemonGo;

import java.util.List;


public interface QuestPokemonGoService {

    List<QuestPokemonGo> findAll();

    QuestPokemonGo findById(Long id);

    QuestPokemonGo create(QuestPokemonGo fileReport);

    QuestPokemonGo update(Long id, QuestPokemonGo fileReport);

    void deleteById(Long id);

}
