package com.hmo.repository;
import com.hmo.model.Member;
import org.springframework.data.repository.CrudRepository;
public interface MemberRepository extends CrudRepository<Member, String>
{
}
