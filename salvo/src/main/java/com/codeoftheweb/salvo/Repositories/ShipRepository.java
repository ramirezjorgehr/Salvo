package com.codeoftheweb.salvo.Repositories;
import com.codeoftheweb.salvo.Models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource

public interface ShipRepository extends JpaRepository<Ship,Long>{



}
