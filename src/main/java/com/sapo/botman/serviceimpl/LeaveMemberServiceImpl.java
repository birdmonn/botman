package com.sapo.botman.serviceimpl;

import com.sapo.botman.model.LeaveMember;
import com.sapo.botman.model.MemberJOB;
import com.sapo.botman.repository.LeaveMemberRepository;
import com.sapo.botman.repository.MemberJOBRepository;
import com.sapo.botman.service.LeaveMemberService;
import com.sapo.botman.service.MemberJOBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class LeaveMemberServiceImpl implements LeaveMemberService {
    private LeaveMemberRepository leaveMemberRepository;
    private MemberJOBService memberJOBService;

    @Autowired
    public LeaveMemberServiceImpl(LeaveMemberRepository leaveMemberRepository,
                                  MemberJOBService memberJOBService) {
        this.leaveMemberRepository = leaveMemberRepository;
        this.memberJOBService = memberJOBService;
    }

    @Override
    public List<LeaveMember> findAll() {
        return leaveMemberRepository.findAll();
    }

    @Override
    public LeaveMember findById(Long id) {
        return leaveMemberRepository.getOne(id);
    }

    @Override
    public String getLeaveList(Long memberJOBId) {
        List<LeaveMember> leaveMemberList = leaveMemberRepository.findByMemberJOBId(memberJOBId);
        StringBuilder replay = new StringBuilder("list leave " + leaveMemberList.size()+" day \n");
        for (int i =0; i<leaveMemberList.size();i++) {
            replay.append(i + 1).append(". ").append(leaveMemberList.get(i).getDateLeave()).append(" ").append("\n");
        }
        return replay.toString();
    }



    @Override
    public LeaveMember create(LeaveMember leaveMember) {
        return leaveMemberRepository.saveAndFlush(leaveMember);
    }

    @Override
    public LeaveMember update(Long id, LeaveMember leaveMember) {
        leaveMember.setId(id);
        return leaveMemberRepository.saveAndFlush(leaveMember);
    }


    @Override
    public void deleteById(Long id) {
        leaveMemberRepository.deleteById(id);
    }

    @Override
    public String createLeaveDate(String userId, List<Date> dateLeave) {
        MemberJOB userLeave = memberJOBService.findByUserId(userId);
        if(userLeave != null) {
            for (Date itemDateLeave : dateLeave) {
                this.create(new LeaveMember(userLeave, itemDateLeave));
            }
            return  "save leave date ";
        } else {
            return "user not regi";
        }
    }
}
