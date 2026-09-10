package uy.edu.utec.apifarmayopin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.utec.apifarmayopin.dtos.requests.CategoryRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.CategoryResponseDTO;
import uy.edu.utec.apifarmayopin.models.Category;
import uy.edu.utec.apifarmayopin.repositories.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(this::mapCategoryToCategoryDTO).toList();
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe una categoria registrada con el nombre: " + dto.getName()
            );
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
        return mapCategoryToCategoryDTO(category);
    }

    private CategoryResponseDTO mapCategoryToCategoryDTO(Category category) {
        CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }
}
