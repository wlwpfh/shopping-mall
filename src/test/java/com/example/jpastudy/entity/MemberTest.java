package com.example.jpastudy.entity;

import com.example.jpastudy.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username="jiyoung", roles="USER")
    void auditingTest(){
        Member newMember=new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member=memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time: "+ member.getRegTime());
        System.out.println("update time: "+member.getUpdateTime());
        System.out.println("create member: "+member.getCreatedBy());
        System.out.println("update member: "+ member.getModifiedBy());
    }
}
