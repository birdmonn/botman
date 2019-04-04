package com.sapo.botman.service;

import com.sapo.botman.model.QuestPokemonGo;

import java.util.List;

public interface MemberService {

    List<Member> findAll();

    Member findById(Long id);

    Member create(Member member);

    Member update(Long id, Member member);

    void deleteById(Long id);

}
