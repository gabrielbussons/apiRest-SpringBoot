package com.medicoapp.repositorys;

import org.springframework.data.repository.CrudRepository;

import com.medicoapp.models.MedicoModel;

public interface MedicoRepository extends CrudRepository<MedicoModel, String>{

	MedicoModel findById(long id);

	Iterable<MedicoModel> findByAtivo(boolean ativo);
}