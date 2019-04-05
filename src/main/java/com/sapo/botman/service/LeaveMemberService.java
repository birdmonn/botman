package com.sapo.botman.service;


import com.sapo.botman.model.LeaveMember;
import com.sapo.botman.model.MemberJOB;

import java.util.List;

public interface LeaveMemberService {

    List<LeaveMember> findAll();

    LeaveMember findById(Long id);

//    List<LeaveMember> findByMemberJOB(MemberJOB memberJOB);

    LeaveMember create(LeaveMember leaveMember);

    LeaveMember update(Long id, LeaveMember leaveMember);

    void deleteById(Long id);

}
