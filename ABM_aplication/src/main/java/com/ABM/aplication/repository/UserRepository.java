package com.ABM.aplication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ABM.aplication.entity.User;
@Repository
public interface UserRepository extends CrudRepository<User,Long>{

	
	
}
