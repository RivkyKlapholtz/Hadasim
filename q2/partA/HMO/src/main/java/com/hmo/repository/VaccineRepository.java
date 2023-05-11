package com.hmo.repository;
import com.hmo.model.Vaccine;
import org.springframework.data.repository.CrudRepository;
public interface VaccineRepository extends CrudRepository<Vaccine, String>
{
}
