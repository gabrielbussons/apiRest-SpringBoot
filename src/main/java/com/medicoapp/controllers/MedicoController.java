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
@RequestMapping(value="/medico")
public class MedicoController {

	@Autowired
	private MedicoRepository medRepository;
	
	@Autowired
	private EspecialidadeRepository espRepository;
	
	@RequestMapping(value="/obterTodos", method=RequestMethod.GET)
	public Iterable<MedicoModel> medicos(){
		
		Iterable<MedicoModel> listaMedicos = medRepository.findAll();
		
		for (MedicoModel medico : listaMedicos) {
			for (EspecialidadeModel especialidade : medico.getEspecialidades()) {
				especialidade.setMedicos(null);
			}
		}
				
		return listaMedicos;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public MedicoModel medicoId(@PathVariable("id") long id) throws Exception {
		
		MedicoModel medico = obterMedico(id);
		
		for (EspecialidadeModel especialidade : medico.getEspecialidades()) {
			especialidade.setMedicos(null);
		}
		
		return medico;
	}
	
	@RequestMapping(value="/cadastrar", method=RequestMethod.POST)
	public MedicoModel cadastrarMedico(@RequestBody MedicoModel medico) throws Exception {
		
		validaFilto(medico);
		
		return medRepository.save(medico);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.PUT)
	public MedicoModel editar(@RequestBody MedicoModel medicoRetorno) throws Exception {
		
		MedicoModel medico = obterMedico(medicoRetorno.getId());
		validaFilto(medicoRetorno);
		medico = medicoRetorno;
		
		return medRepository.save(medico);
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") long id) throws Exception {
		
		MedicoModel medico = obterMedico(id);
		medRepository.delete(medico);
		
		return "médico deletado com sucesso!";
	}
	
	@RequestMapping(value="/alterarSituacao/{id}", method=RequestMethod.PUT)
	public MedicoModel alterarSituacaoMedico(@PathVariable("id") long id) throws Exception {
		
		MedicoModel medico = obterMedico(id);
		medico.setAtivo(!medico.isAtivo());
		
		return medRepository.save(medico);
	}
	
	@RequestMapping(value="/obterEspecialidades/{id}", method=RequestMethod.PUT)
	public Iterable<EspecialidadeModel> obterEspecialidades(@PathVariable("id") long id) throws Exception {
		
		MedicoModel medico = obterMedico(id);
		return medico.getEspecialidades();
	}
	
	@RequestMapping(value="/associarEspecialidade/{idMedico}/{idEspecialidade}", method=RequestMethod.POST)
	public String associarEspecialidade(@PathVariable("idMedico") long idMedico, @PathVariable("idEspecialidade") long idEspecialidade) throws Exception {
		
		MedicoModel medico = obterMedico(idMedico);
		EspecialidadeModel especialidade = obterEspecialidade(idEspecialidade);
		
		medico.getEspecialidades().add(especialidade);
		medRepository.save(medico);

		return "Especialidade associada com sucesso!";
	}
	
	@RequestMapping(value="/desassociarEspecialidade/{idMedico}/{idEspecialidade}", method=RequestMethod.POST)
	public String desassociarrEspecialidade(@PathVariable("idMedico") long idMedico, @PathVariable("idEspecialidade") long idEspecialidade) throws Exception {
		
		MedicoModel medico = obterMedico(idMedico);
		EspecialidadeModel especialidade = obterEspecialidade(idEspecialidade);
		
		medico.getEspecialidades().remove(especialidade);
		medRepository.save(medico);

		return "Especialidade desassociada com sucesso!";
	}
	
	
	private boolean validaFilto(MedicoModel medico) throws Exception {
		if(medico.getNome() == null || medico.getNome() == "") {
			throw new Exception("Campo nome é obrigatório.");
		}
		
		if(medico.getDataNascimento() == null) {
			throw new Exception("Campo dataNascimento é obrigatório.");
		}
		
		if(!medico.isAtivo() && medico.isAtivo() != false) {
			throw new Exception("Campo ativo é obrigatório.");
		}
		
		return true;
	}
	
	private MedicoModel obterMedico(long id) throws Exception {
		
		MedicoModel medico = medRepository.findById(id);
		
		if(medico == null) {
			throw new Exception("Não foi possivel encontrar um médico com esse id.");
		}
		
		return medico;
	}
	
	private EspecialidadeModel obterEspecialidade(long id) throws Exception {
		
		EspecialidadeModel especialidade = espRepository.findById(id);
		
		if(especialidade == null) {
			throw new Exception("Não foi possivel encontrar uma especialidade com esse id.");
		}
		
		return especialidade;
	}
}
