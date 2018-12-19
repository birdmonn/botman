package com.sapo.botman.serviceimpl;

import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.repository.QuestPokemonGoRepository;
import com.sapo.botman.service.QuestPokemonGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestPokemonGoServiceImpl implements QuestPokemonGoService {
    private QuestPokemonGoRepository questPokemonGoRepository;

    @Autowired
    public QuestPokemonGoServiceImpl(QuestPokemonGoRepository questPokemonGoRepository) {
        this.questPokemonGoRepository = questPokemonGoRepository;
    }

    @Override
    public List<QuestPokemonGo> findAll() {
        return questPokemonGoRepository.findAll();
    }

    @Override
    public QuestPokemonGo findById(Long id) {
        return questPokemonGoRepository.getOne(id);
    }

    @Override
    public QuestPokemonGo create(QuestPokemonGo fileReport) {
        return questPokemonGoRepository.saveAndFlush(fileReport);
    }

    @Override
    public QuestPokemonGo update(Long id, QuestPokemonGo fileReport) {
        fileReport.setId(id);
        return questPokemonGoRepository.saveAndFlush(fileReport);
    }

    @Override
    public void deleteById(Long id) {
        questPokemonGoRepository.deleteById(id);
    }


}
