package com.javatpoint.repository;
import com.javatpoint.model.Member;
import org.springframework.data.repository.CrudRepository;
public interface MemberRepository extends CrudRepository<Member, String>
{
}
