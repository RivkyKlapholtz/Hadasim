package com.javatpoint.repository;
import com.javatpoint.model.Vaccine;
import org.springframework.data.repository.CrudRepository;
public interface VaccineRepository extends CrudRepository<Vaccine, String>
{
}
