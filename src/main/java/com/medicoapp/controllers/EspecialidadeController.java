package com.medicoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.medicoapp.models.EspecialidadeModel;
import com.medicoapp.models.MedicoModel;
import com.medicoapp.repositorys.EspecialidadeRepository;
import com.medicoapp.repositorys.MedicoRepository;

@RestController
@RequestMapping(value="/especialidade")
public class EspecialidadeController {
	
	@Autowired
	private MedicoRepository medRepository;
	
	@Autowired
	private EspecialidadeRepository espRepository;
	
	@RequestMapping(value="/obterTodos", method=RequestMethod.GET)
	public Iterable<EspecialidadeModel> especialidades(){
		
		Iterable<EspecialidadeModel> listaEspecialidades = espRepository.findAll();
		
		for (EspecialidadeModel especialidade : listaEspecialidades) {
			for (MedicoModel medico : especialidade.getMedicos()) {
				medico.setEspecialidades(null);
			}
		}
				
		return listaEspecialidades;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public EspecialidadeModel especialidadeId(@PathVariable("id") long id) throws Exception {
		
		EspecialidadeModel especialidade = obterEspecialidade(id);
		
		for (MedicoModel medico : especialidade.getMedicos()) {
			medico.setEspecialidades(null);
		}
		
		return especialidade;
	}
	
	@RequestMapping(value="/cadastrar", method=RequestMethod.POST)
	public EspecialidadeModel cadastrar(@RequestBody EspecialidadeModel especialidade) throws Exception {
		
		validaFilto(especialidade);
		
		return espRepository.save(especialidade);
	}
	
	@RequestMapping(value="/editar", method=RequestMethod.PUT)
	public EspecialidadeModel editar(@RequestBody EspecialidadeModel especialidadeRetorno) throws Exception {
		
		EspecialidadeModel especialidade = obterEspecialidade(especialidadeRetorno.getId());
		validaFilto(especialidadeRetorno);
		especialidade = especialidadeRetorno;
		
		return espRepository.save(especialidade);
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") long id) throws Exception {
		
		EspecialidadeModel especialidadeModel = obterEspecialidade(id);
		
		for (MedicoModel medico : especialidadeModel.getMedicos()) {
		     medico.getEspecialidades().remove(especialidadeModel);
		     medRepository.save(medico);
		}
		
		espRepository.delete(especialidadeModel);
		
		return "médico deletado com sucesso!";
	}
	
	@RequestMapping(value="/alterarSituacao/{id}", method=RequestMethod.PUT)
	public EspecialidadeModel alterarSituacao(@PathVariable("id") long id) throws Exception {
		
		EspecialidadeModel especialidade = obterEspecialidade(id);
		especialidade.setAtivo(!especialidade.isAtivo());
		
		return espRepository.save(especialidade);
	}
	
	@RequestMapping(value="/obterMedicos/{id}", method=RequestMethod.PUT)
	public Iterable<MedicoModel> obterMedicos(@PathVariable("id") long id) throws Exception {
		
		EspecialidadeModel especialidade = obterEspecialidade(id);
		return especialidade.getMedicos();
	}	
	
	private boolean validaFilto(EspecialidadeModel especialidade) throws Exception {
		if(especialidade.getNome() == null || especialidade.getNome() == "") {
			throw new Exception("Campo nome é obrigatório.");
		}
		
		if(especialidade.getDescricao() == null || especialidade.getDescricao() == "") {
			throw new Exception("Campo descricao é obrigatório.");
		}
		
		if(!especialidade.isAtivo() && especialidade.isAtivo() != false) {
			throw new Exception("Campo ativo é obrigatório.");
		}
		
		return true;
	}
	
	private EspecialidadeModel obterEspecialidade(long id) throws Exception {
		
		EspecialidadeModel especialidade = espRepository.findById(id);
		
		if(especialidade == null) {
			throw new Exception("Não foi possivel encontrar uma especialidade com esse id.");
		}
		
		return especialidade;
	}
	
	
	
}