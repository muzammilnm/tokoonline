package com.tokoonline.demo.product;

import static org.mockito.Mockito.times;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.tokoonline.demo.category.CategoryRepository;
import com.tokoonline.demo.category.CategoryService;
import com.tokoonline.demo.category.model.Category;
import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.SearchProductRequestDto;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepostitory productRepostitory;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void fetchAll_shouldReturnAllProduct_whenProductIsNotEmpty(){
        Product productOne = Product.builder().name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Product productTwo = Product.builder().name("white koffie").description("kopi asli luwak").price(Double.valueOf(13400)).stock(BigInteger.valueOf(29)).build();
        Product productThree = Product.builder().name("cimory").description("susu manis praktis").price(Double.valueOf(5400)).stock(BigInteger.valueOf(13)).build();
        List<Product> products = List.of(productOne, productTwo, productThree);
        List<Product> expectedResult = List.of(productOne, productTwo, productThree);
        Mockito.when(productRepostitory.findAll()).thenReturn(products);

        List<Product> actualResult = productService.fetchAll();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void search_shouldReturnListOfProductThatNameContainsOppo5s_whenGivenNameIsopo(){
        SearchProductRequestDto request = SearchProductRequestDto.builder()
            .page(1)
            .size(10)
            .name("opo")
            .build();
        Product firstProduct = Product.builder()
            .name("Oppo5s")
            .description("oppo 5s description example")
            .price(Double.valueOf(4000000))
            .build();
        Product secondProduct = Product.builder()
            .name("Oppo4s")
            .description("Oppo 4s description example")
            .price(Double.valueOf(3000000))
            .build();
        List<Product> products = List.of(firstProduct, secondProduct);
        Pageable paging = PageRequest.of(1, 10);
        Page<Product> expectedResult = new PageImpl<>(products, paging, 2);
        Mockito.when(this.productRepostitory.findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging))).thenReturn(expectedResult);
        
        Page<Product> actualResult = productService.search(request);

        Mockito.verify(productRepostitory, Mockito.times(1)).findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging));
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void search_shouldReturnListOfProductThatPrice_whenGivenPriceIs4000000(){
        SearchProductRequestDto request = SearchProductRequestDto.builder()
            .page(1)
            .size(10)
            .price(Double.valueOf(4000000))
            .build();
        Product firstProduct = Product.builder()
            .name("Oppo5s")
            .description("oppo 5s description example")
            .price(Double.valueOf(4000000))
            .build();
        List<Product> products = List.of(firstProduct);
        Pageable paging = PageRequest.of(1, 10);
        Page<Product> expectedResult = new PageImpl<>(products, paging, 2);
        Mockito.when(this.productRepostitory.findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging))).thenReturn(expectedResult);
        
        Page<Product> actualResult = productService.search(request);

        Mockito.verify(productRepostitory, Mockito.times(1)).findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging));
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void search_shouldReturnListOfProductThatcategory_whenGivenCategoryIdIsExists(){
        Category category = Category.builder().id(UUID.randomUUID()).name("buku").build();
        Product firstProduct = Product.builder()
            .name("Oppo5s")
            .description("oppo 5s description example")
            .categories(List.of(category))
            .build();
        SearchProductRequestDto request = SearchProductRequestDto.builder()
            .page(1)
            .size(10)
            .categoryIds(List.of(category.getId()))
            .build();
        List<Product> products = List.of(firstProduct);
        Pageable paging = PageRequest.of(1, 10);
        Page<Product> expectedResult = new PageImpl<>(products, paging, 2);
        Mockito.when(this.productRepostitory.findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging))).thenReturn(expectedResult);
        
        Page<Product> actualResult = productService.search(request);

        Mockito.verify(productRepostitory, Mockito.times(1)).findAll(ArgumentMatchers.<Specification<Product>>any(), Mockito.eq(paging));
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void fetchAll_shouldReturnEmptyProduct_whenProductIsEmpty(){
        List<Product> products = List.of();
        List<Product> expectedResult = List.of();
        Mockito.when(productRepostitory.findAll()).thenReturn(products);

        List<Product> actualResult = productService.fetchAll();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void fetchById_shouldReturnProduct_whenGivenProductAlreadyById(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.of(product));

        Product actualResult = productService.fetchById(id);

        Assertions.assertEquals(product, actualResult);
    }

    @Test
    void fetchById_shouldThrowErrorException_whenGivenProductIsNotFound(){
        UUID id = UUID.randomUUID();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.fetchById(id));
    }

    @Test
    void add_shouldReturnNewProduct_whenProductsIsAdded(){
        UUID id = UUID.randomUUID();
        Category category = Category.builder().id(id).name("perawatan").build();
        Product product = Product.builder().name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).categories(List.of(category)).build();
        Mockito.when(categoryService.fetchAllById(List.of(id))).thenReturn(List.of(category));
        Mockito.when(productRepostitory.save(product)).thenReturn(product);

        Product actualResult = productService.add(product, List.of(id));

        Assertions.assertEquals(product, actualResult);
    }

    @Test
    void update_shouldReturnProductUpdate_whenUpdateProductIsSuccess(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").description("sabun pakaian").price(Double.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
        Product expectedResult = Product.builder().id(id).name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productRepostitory.save(product)).thenReturn(expectedResult);

        Product actualResult = productService.update(product);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test 
    void update_shouldThrowProductNotFoundException_whenProductIsNotFound() throws Exception{
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").description("sabun pakaian").price(Double.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.update(product));
    }

    @Test
    void delete_shouldReturnTrue_whenDeleteProductIsSuccess(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").description("sabun pakaian").price(Double.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.of(product));
        
        Boolean actualResult = productService.deleteById(id);

        Mockito.verify(productRepostitory,times(1)).deleteById(id);
        Assertions.assertEquals(true, actualResult);
    }

    @Test
    void delete_shouldThrowProductNotFoundExeption_whenProductIsNotFound(){
        UUID id = UUID.randomUUID();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.empty());

        Mockito.verify(productRepostitory,times(0)).deleteById(id);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteById(id));
    }

}
