package com.sapo.botman.serviceimpl;

import com.sapo.botman.model.MemberJOB;
import com.sapo.botman.repository.MemberJOBRepository;
import com.sapo.botman.service.MemberJOBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberJOBServiceImpl implements MemberJOBService {
    private MemberJOBRepository memberJOBRepository;

    @Autowired
    public MemberJOBServiceImpl(MemberJOBRepository memberJOBRepository) {
        this.memberJOBRepository = memberJOBRepository;
    }

    @Override
    public List<MemberJOB> findAll() {
        return memberJOBRepository.findAll();
    }

    @Override
    public MemberJOB findById(Long id) {
        return memberJOBRepository.getOne(id);
    }

    @Override
    public MemberJOB create(MemberJOB memberJOB) {
        return memberJOBRepository.saveAndFlush(memberJOB);
    }

    @Override
    public MemberJOB update(Long id, MemberJOB memberJOB) {
        memberJOB.setId(id);
        return memberJOBRepository.saveAndFlush(memberJOB);
    }

    @Override
    public void deleteById(Long id) {
        memberJOBRepository.deleteById(id);
    }


}
