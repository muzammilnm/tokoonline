package com.tokoonline.demo.product;

import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tokoonline.demo.product.model.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepostitory productRepostitory;

    @Test
    void fetchAll_shouldReturnAllProduct_whenProductIsNotEmpty(){
        Product productOne = Product.builder().name("Soklin").desctription("sabun pencuci pakaian").price(BigDecimal.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Product productTwo = Product.builder().name("white koffie").desctription("kopi asli luwak").price(BigDecimal.valueOf(13400)).stock(BigInteger.valueOf(29)).build();
        Product productThree = Product.builder().name("cimory").desctription("susu manis praktis").price(BigDecimal.valueOf(5400)).stock(BigInteger.valueOf(13)).build();
        List<Product> products = List.of(productOne, productTwo, productThree);
        List<Product> expectedResult = List.of(productOne, productTwo, productThree);
        Mockito.when(productRepostitory.findAll()).thenReturn(products);

        List<Product> actualResult = productService.fetchAll();

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
        Product product = Product.builder().id(id).name("Soklin").desctription("sabun pencuci pakaian").price(BigDecimal.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
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
        Product product = Product.builder().name("Soklin").desctription("sabun pencuci pakaian").price(BigDecimal.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Mockito.when(productRepostitory.save(product)).thenReturn(product);

        Product actualResult = productService.add(product);

        Assertions.assertEquals(product, actualResult);
    }

    @Test
    void update_shouldReturnProductUpdate_whenUpdateProductIsSuccess(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").desctription("sabun pakaian").price(BigDecimal.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
        Product expectedResult = Product.builder().id(id).name("Soklin").desctription("sabun pencuci pakaian").price(BigDecimal.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productRepostitory.save(product)).thenReturn(expectedResult);

        Product actualResult = productService.update(product);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test 
    void update_shouldThrowProductNotFoundException_whenProductIsNotFound() throws Exception{
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").desctription("sabun pakaian").price(BigDecimal.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
        Mockito.when(productRepostitory.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.update(product));
    }

    @Test
    void delete_shouldReturnTrue_whenDeleteProductIsSuccess(){
        UUID id = UUID.randomUUID();
        Product product = Product.builder().id(id).name("Daia").desctription("sabun pakaian").price(BigDecimal.valueOf(8500)).stock(BigInteger.valueOf(10)).build();
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
