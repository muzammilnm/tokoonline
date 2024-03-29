package com.tokoonline.demo.product;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokoonline.demo.applicationuser.ApplicationUserRepository;
import com.tokoonline.demo.applicationuser.model.ApplicationUser;
import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.ProductRequestDto;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepostitory productRepostitory;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    void setup(){
        ApplicationUser applicationUser = ApplicationUser.builder()
            .firstName("john")
            .lastName("doe")
            .username("johndoe")
            .email("johndoe@gmail.com")
            .build();
        applicationUserRepository.save(applicationUser);
    }

    @AfterEach
    public void cleanUp(){
        applicationUserRepository.deleteAll();
        productRepostitory.deleteAll();

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchAll_shouldReturnAllProduct_whenGivenProductsIsNotEmpty() throws Exception{
        Product productFirst = Product.builder().name("Soklin").desctription("sabun pencuci pakaian").price(BigDecimal.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        Product productSecond = Product.builder().name("white koffie").desctription("kopi asli luwak").price(BigDecimal.valueOf(13400)).stock(BigInteger.valueOf(29)).build();
        List<Product> products = List.of(productFirst, productSecond);
        productRepostitory.saveAll(products);
        List<Product> findAllProduct = productRepostitory.findAll();
        List<ProductResponseDto> expectedResult = findAllProduct.stream().map(Product::convertToDto).toList();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        List<ProductResponseDto> actualResult = objectMapper.readerForListOf(ProductResponseDto.class).readValue(responseString);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchAll_shouldReturnEmptyProduct_whenGivenProductsIsEmpty() throws Exception{
        List<Product> findAllProduct = productRepostitory.findAll();
        List<ProductResponseDto> expectedResult = findAllProduct.stream().map(Product::convertToDto).toList();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        List<ProductResponseDto> actualResult = objectMapper.readerForListOf(ProductResponseDto.class).readValue(responseString);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchById_shouldReturnCustomer_whenGivenCustomerIsNotEmpty() throws Exception{
        Product product = Product.builder().name("Soklin").desctription("sabun pencuci pakaian").price(new BigDecimal("9500.00")).stock(BigInteger.valueOf(20)).build();
        Product productSaved = productRepostitory.save(product);
        ProductResponseDto expectedResult = ProductResponseDto.builder().id(productSaved.getId()).name("Soklin").desctription("sabun pencuci pakaian").price(new BigDecimal("9500.00")).stock(BigInteger.valueOf(20)).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productSaved.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ProductResponseDto actualResult = objectMapper.readValue(responseString, ProductResponseDto.class);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchById_shouldTrhowProductNotFoundException_whenGivenCustomerIsEmpty() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void add_shouldAddNewProduct_whenGivenAddProduct()throws Exception{
        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Soklin")
            .description("sabun pencuci pakaian")
            .price(new BigDecimal("9500.00"))
            .stock(BigInteger.valueOf(20))
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result  = mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ProductResponseDto actualResult = objectMapper.readValue(responseString, ProductResponseDto.class);
        Product product = productRepostitory.findById(actualResult.getId()).get();
        ProductResponseDto expectedResult = product.convertToDto();
        
        Assertions.assertEquals(expectedResult, actualResult);
    }

    // @Test
    // @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    // void add_shouldErrorResponse_whenGivenNameIsNull()throws Exception{
    //     ProductRequestDto requestDto = ProductRequestDto.builder().price(new BigDecimal("9500.00")).stock(BigInteger.valueOf(20)).build();
    //     String requestDtoString = objectMapper.writeValueAsString(requestDto);
    //     mockMvc.perform(MockMvcRequestBuilders.post("/products")
    //         .content(requestDtoString)
    //         .contentType(MediaType.APPLICATION_JSON)
    //         ).andExpect(MockMvcResultMatchers.status().isBadRequest())
    //         .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name can not be null")))
    //         .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    // }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void update_shouldReturnUpdateProduct_whenGivenUpdateProductIsSuccess() throws Exception{
        UUID id = UUID.randomUUID();
        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Soklin")
            .description("Sabun pencuci pakaian")
            .price(new BigDecimal("9000.00"))
            .stock(BigInteger.valueOf(20))
            .build();
        Product product = Product.builder()
            .id(id)
            .name("Deterjen")
            .desctription("sabun pakaian")
            .price(new BigDecimal("9500.00"))
            .stock(BigInteger.valueOf(20)).build();
        Product productSaved = productRepostitory.save(product);
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        RequestBuilder builder = MockMvcRequestBuilders.put("/products/{id}", productSaved.getId())
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ProductResponseDto actualResult = objectMapper.readValue(responseString, ProductResponseDto.class);
        Product foundProduct = productRepostitory.findById(actualResult.getId()).get();
        ProductResponseDto expectedResult = foundProduct.convertToDto();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void update_shouldThrowProductNotFoundException_whenGivenUpdateProductIsNotFound() throws Exception{
        UUID id = UUID.randomUUID();
        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Soklin")
            .description("Sabun pencuci pakaian")
            .price(new BigDecimal("9000.00"))
            .stock(BigInteger.valueOf(20))
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        RequestBuilder builder = MockMvcRequestBuilders.put("/products/{id}", id)
            .content(requestDtoString)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void deleteById_shouldReturnTrue_whenDeleteProductIsSuccess() throws Exception{
        Product product = Product.builder()
            .name("Deterjen")
            .desctription("sabun pakaian")
            .price(new BigDecimal("9500.00"))
            .stock(BigInteger.valueOf(20)).build();
        Product productSaved = productRepostitory.save(product);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", productSaved.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void deleteById_shouldErrorResponse_whenDeleteProductIsNotFound() throws Exception{
        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
