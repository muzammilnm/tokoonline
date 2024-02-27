package com.tokoonline.demo.product;

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

}
