package com.codeoftheweb.salvo.Repositories;

import com.codeoftheweb.salvo.Models.Player;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player,Long> {

    Player findByUserName(@Param("name")String name);

}
