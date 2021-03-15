package com.medicoapp.repositorys;

import org.springframework.data.repository.CrudRepository;

import com.medicoapp.models.EspecialidadeModel;

public interface EspecialidadeRepository extends CrudRepository<EspecialidadeModel, String>{

	EspecialidadeModel findById(long id);

	Iterable<EspecialidadeModel> findByAtivo(boolean ativo);
	
}
