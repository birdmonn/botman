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
    public String regiMember(MemberJOB memberJOB) {
        MemberJOB findMember = memberJOBRepository.findByUserId(memberJOB.getUserId());
        if(findMember != null){
            return "คุณได้ลงทะเบียนแล้ว";
        }
        MemberJOB memberRegi = create(memberJOB);
        if (memberRegi != null){
            return "ลงทะเบียนเรียบร้อย";
        } else {
            return "เกิดข้อผิดพลากรุณาติดต่อผู้ดูแล";
        }
    }

    @Override
    public String getMemberList() {
        List<MemberJOB> memList= this.findAll();
        StringBuilder replay = new StringBuilder("รายชื่อทั้งหมด\n");
        for (int i =0; i<memList.size();i++) {
            replay.append(i + 1).append(". ").append(memList.get(i).getName()).append("\n");
        }
        return replay.toString();
    }

    @Override
    public void deleteById(Long id) {
        memberJOBRepository.deleteById(id);
    }


}
