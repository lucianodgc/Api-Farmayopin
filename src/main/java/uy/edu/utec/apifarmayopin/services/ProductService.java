package uy.edu.utec.apifarmayopin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.utec.apifarmayopin.dtos.requests.ProductRequestDTO;
import uy.edu.utec.apifarmayopin.dtos.responses.ProductResponseDTO;
import uy.edu.utec.apifarmayopin.models.Category;
import uy.edu.utec.apifarmayopin.models.Product;
import uy.edu.utec.apifarmayopin.repositories.CategoryRepository;
import uy.edu.utec.apifarmayopin.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponseDTO> getAllProducts(String sortBy) {
        Sort sort;

        if ("category".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "category.name");
        } else if ("name".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "id");
        }

        return productRepository.findAll(sort)
                .stream()
                .map(this::mapToProductResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto con id: " + id + " no encontrado"));
        return mapToProductResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        if (productRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un producto registrado con el nombre: " + dto.getName()
            );
        }
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una categoría con el id: " + dto.getCategoryId()
                ));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setPhoto(dto.getPhoto());
        product.setStock(dto.getStock());
        product.setCategory(category);
        return mapToProductResponseDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto con id: " + id + " no encontrado"));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());
        existing.setPhoto(dto.getPhoto());
        existing.setStock(dto.getStock());
        existing.setCategory(existing.getCategory());

        return mapToProductResponseDTO(productRepository.save(existing));
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        ProductResponseDTO productDTO = new ProductResponseDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setPhoto(product.getPhoto());
        productDTO.setStock(product.getStock());
        productDTO.setCategoryId(product.getCategory().getId());
        return productDTO;
    }
}