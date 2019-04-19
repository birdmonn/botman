package com.sapo.botman.service;


import com.sapo.botman.model.LeaveMember;

import java.util.Date;
import java.util.List;

public interface LeaveMemberService {

    List<LeaveMember> findAll();

    LeaveMember findById(Long id);

    String getLeaveList(Long memberJOBId);

    String createLeaveDate(String userId, List<Date> dateLeave);

    LeaveMember create(LeaveMember leaveMember);

    LeaveMember update(Long id, LeaveMember leaveMember);

    void deleteById(Long id);

}
