package com.codeoftheweb.salvo.Repositories;

import com.codeoftheweb.salvo.Models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Long> {
}
