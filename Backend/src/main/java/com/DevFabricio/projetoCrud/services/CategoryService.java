package com.DevFabricio.projetoCrud.services;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevFabricio.projetoCrud.dto.CategoryDTO;
import com.DevFabricio.projetoCrud.entities.Category;
import com.DevFabricio.projetoCrud.repositories.CategoryRepository;
import com.DevFabricio.projetoCrud.services.esceptions.DatabaseException;
import com.DevFabricio.projetoCrud.services.esceptions.ResourceNotFoundException;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		 Page<Category> list =repository.findAll(pageRequest);
			return list.map(x -> new CategoryDTO(x));
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj =repository.findById(id);
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	
		@Transactional
		public CategoryDTO update(Long id,CategoryDTO dto) {
			try {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
			}
			catch(EntityNotFoundException e) {
				throw new ResourceNotFoundException("Id not found " + id);
			}
		}
		
		public void delete(Long id) {
			try {
			repository.deleteById(id);
			}
			catch(EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException("Id not found " + id);
			}
			catch (DataIntegrityViolationException e) {
				throw new DatabaseException("Integrity violation");
			}
			
		}

		

		}
