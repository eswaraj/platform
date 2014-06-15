package com.eswaraj.core.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.CategoryConvertor;
import com.eswaraj.core.convertors.PartyConvertor;
import com.eswaraj.core.convertors.PoliticalBodyTypeConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@Component
@Transactional
public class AppServiceImpl implements AppService {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryConvertor categoryConvertor;
	@Autowired
	private PoliticalBodyTypeRepository politicalBodyTypeRepository;
	@Autowired
	private PoliticalBodyTypeConvertor politicalBodyTypeConvertor;
	@Autowired
	private PartyRepository partyRepository;
	@Autowired
	private PartyConvertor partyConvertor;
	
	@Override
	public CategoryDto saveCategory(CategoryDto categoryDto) throws ApplicationException {
		Category category = categoryConvertor.convert(categoryDto);
		category = categoryRepository.save(category);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public CategoryDto getCategoryById(long categoryId) throws ApplicationException {
		Category category = categoryRepository.findOne(categoryId);
		return categoryConvertor.convertBean(category);
	}

	@Override
	public List<CategoryDto> getAllRootCategories() throws ApplicationException {
		Collection<Category> rootCategories = categoryRepository.getAllRootCategories();
		return categoryConvertor.convertBeanList(rootCategories);
	}

	@Override
	public List<CategoryDto> getAllChildCategoryOfParentCategory(long parentCategoryId) throws ApplicationException {
		Category parentCategory = categoryRepository.findOne(parentCategoryId);
		if(parentCategory == null){
			throw new ApplicationException("No such location exists [id="+parentCategoryId+"]");
		}
		Collection<Category> rootCategories = categoryRepository.findAllChildCategoryOfParentCategory(parentCategory);
		return categoryConvertor.convertBeanList(rootCategories);
	}

	@Override
	public PoliticalBodyTypeDto savePoliticalBodyType(PoliticalBodyTypeDto politicalBodyTypeDto) throws ApplicationException {
		PoliticalBodyType politicalBodyType = politicalBodyTypeConvertor.convert(politicalBodyTypeDto);
		politicalBodyType = politicalBodyTypeRepository.save(politicalBodyType);
		return politicalBodyTypeConvertor.convertBean(politicalBodyType);
	}

	@Override
	public PoliticalBodyTypeDto getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException {
		PoliticalBodyType politicalBodyType = politicalBodyTypeRepository.findOne(politicalBodyTypeId);
		return politicalBodyTypeConvertor.convertBean(politicalBodyType);
	}

	@Override
	public List<PoliticalBodyTypeDto> getAllPoliticalBodyTypes() throws ApplicationException {
		EndResult<PoliticalBodyType> allPoliticalBodyTypesFromDb = politicalBodyTypeRepository.findAll();
		return politicalBodyTypeConvertor.convertBeanList(allPoliticalBodyTypesFromDb);
	}

	@Override
	public PartyDto saveParty(PartyDto partyDto) throws ApplicationException {
		Party party = partyConvertor.convert(partyDto);
		party = partyRepository.save(party);
		return partyConvertor.convertBean(party);
	}

	@Override
	public PartyDto getPartyById(Long partyId) throws ApplicationException {
		Party party = partyRepository.findOne(partyId);
		return partyConvertor.convertBean(party);
	}

	@Override
	public List<PartyDto> getAllPoliticalParties() throws ApplicationException {
		EndResult<Party> result = partyRepository.findAll();
		return partyConvertor.convertBeanList(result);
	}

}
