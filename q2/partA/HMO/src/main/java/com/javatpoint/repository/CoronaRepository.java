package com.javatpoint.repository;
import com.javatpoint.model.Corona;
import org.springframework.data.repository.CrudRepository;
public interface CoronaRepository extends CrudRepository<Corona, String>
{
}
