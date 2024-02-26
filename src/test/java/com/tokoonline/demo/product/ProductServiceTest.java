package com.tokoonline.demo.product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

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

}
