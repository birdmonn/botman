package com.sapo.botman.service;


import com.sapo.botman.model.MemberJOB;

import java.util.List;

public interface MemberJOBService {

    List<MemberJOB> findAll();

    MemberJOB findById(Long id);

    MemberJOB create(MemberJOB memberJOB);

    MemberJOB update(Long id, MemberJOB memberJOB);

    void deleteById(Long id);

}
