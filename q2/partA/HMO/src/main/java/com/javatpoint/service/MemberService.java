package com.javatpoint.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javatpoint.model.Member;
import com.javatpoint.repository.MemberRepository;

//defining the business logic
@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public List<Member> getAllMember() {
        List<Member> members = new ArrayList<>();
        memberRepository.findAll().forEach(member -> members.add(member));
        return members;
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id).get();
    }

    public void saveOrUpdate(Member member) {
        memberRepository.save(member);
    }

    public void delete(String id) {
        memberRepository.deleteById(id);
    }
}