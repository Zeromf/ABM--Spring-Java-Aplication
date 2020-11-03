package com.ABM.aplication.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ABM.aplication.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{

	public Role findByName(String name);


	
	
}
