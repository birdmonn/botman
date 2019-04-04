package com.sapo.botman.serviceimpl;

import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.repository.QuestPokemonGoRepository;
import com.sapo.botman.service.QuestPokemonGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.getOne(id);
    }

    @Override
    public Member create(Member member) {
        return memberRepository.saveAndFlush(member);
    }

    @Override
    public Member update(Long id, Member member) {
        member.setId(id);
        return memberRepository.saveAndFlush(member);
    }

    @Override
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }


}
