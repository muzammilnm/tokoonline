package com.tokoonline.demo.product;

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
import org.springframework.http.HttpStatus;
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
import com.tokoonline.demo.category.CategoryRepository;
import com.tokoonline.demo.category.model.Category;
import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.ProductRequestDto;
import com.tokoonline.demo.product.model.dto.ProductResponseDto;
import com.tokoonline.demo.product.model.dto.SearchProductResponseDto;
import com.tokoonline.demo.response.PageResponse;
import com.tokoonline.demo.response.ResponseSuccess;

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
    private CategoryRepository categoryRepository;

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
        categoryRepository.deleteAll();

    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchAll_shouldReturnAllProduct_whenGivenProductContainsWithName() throws Exception{
        Category category = Category.builder().name("buku").build();
        categoryRepository.save(category);
        Product productFirst = Product.builder().name("clean code").description("clean code").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).categories(List.of(category)).build();
        Product productSecond = Product.builder().name("clean architechture").description("clean architechture").price(Double.valueOf(13400)).stock(BigInteger.valueOf(29)).categories(List.of(category)).build();
        List<Product> products = List.of(productFirst, productSecond);
        productRepostitory.saveAll(products);
        List<SearchProductResponseDto> data = List.of(productFirst.convertToSearchDto());
        PageResponse pages = PageResponse.builder()
            .size(10)
            .total(Long.valueOf(2))
            .totalPage(1)
            .current(1)
            .build();
        ResponseSuccess expectedResult = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(data)
            .page(pages)
            .build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products").param("name", "clean code"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ResponseSuccess actualResponse = objectMapper.readValue(responseString, ResponseSuccess.class);
        String responseDataString = objectMapper.writeValueAsString(actualResponse.getData());
        List<SearchProductResponseDto> actualResult = objectMapper.readerForListOf(SearchProductResponseDto.class).readValue(responseDataString);
        
        Assertions.assertEquals(expectedResult.getData(), actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchAll_shouldReturnAllProduct_whenGivenPriceIs9500() throws Exception{
        Category category = Category.builder().name("buku").build();
        categoryRepository.save(category);
        Product productFirst = Product.builder().name("clean code").description("clean code").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).categories(List.of(category)).build();
        Product productSecond = Product.builder().name("clean architechture").description("clean architechture").price(Double.valueOf(13400)).stock(BigInteger.valueOf(29)).categories(List.of(category)).build();
        List<Product> products = List.of(productFirst, productSecond);
        productRepostitory.saveAll(products);
        List<SearchProductResponseDto> data = List.of(productFirst.convertToSearchDto());
        PageResponse pages = PageResponse.builder()
            .size(10)
            .total(Long.valueOf(2))
            .totalPage(1)
            .current(1)
            .build();
        ResponseSuccess expectedResult = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(data)
            .page(pages)
            .build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products").param("price", "9500"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ResponseSuccess actualResponse = objectMapper.readValue(responseString, ResponseSuccess.class);
        String responseDataString = objectMapper.writeValueAsString(actualResponse.getData());
        List<SearchProductResponseDto> actualResult = objectMapper.readerForListOf(SearchProductResponseDto.class).readValue(responseDataString);
        
        Assertions.assertEquals(expectedResult.getData(), actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchAll_shouldReturnAllProduct_whenGivenProductCategoryIsExists() throws Exception{
        Category category = Category.builder().name("buku").build();
        categoryRepository.save(category);
        Product productFirst = Product.builder().name("clean code").description("clean code").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).categories(List.of(category)).build();
        Product productSecond = Product.builder().name("clean architechture").description("clean architechture").price(Double.valueOf(13400)).stock(BigInteger.valueOf(29)).categories(List.of(category)).build();
        List<Product> products = List.of(productFirst, productSecond);
        productRepostitory.saveAll(products);
        List<SearchProductResponseDto> data = List.of(productFirst.convertToSearchDto(), productSecond.convertToSearchDto());
        PageResponse pages = PageResponse.builder()
            .size(10)
            .total(Long.valueOf(2))
            .totalPage(1)
            .current(1)
            .build();
        ResponseSuccess expectedResult = ResponseSuccess.builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(data)
            .page(pages)
            .build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products").param("categoryIds", category.getId().toString()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ResponseSuccess actualResponse = objectMapper.readValue(responseString, ResponseSuccess.class);
        String responseDataString = objectMapper.writeValueAsString(actualResponse.getData());
        List<SearchProductResponseDto> actualResult = objectMapper.readerForListOf(SearchProductResponseDto.class).readValue(responseDataString);
        
        Assertions.assertEquals(expectedResult.getData(), actualResult);
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

        ResponseSuccess actualResponse = objectMapper.readValue(responseString, ResponseSuccess.class);
        String responseDataString = objectMapper.writeValueAsString(actualResponse.getData());
        List<ProductResponseDto> actualResult = objectMapper.readerForListOf(ProductResponseDto.class).readValue(responseDataString);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void fetchById_shouldReturnCustomer_whenGivenCustomerIsNotEmpty() throws Exception{
        Category category = Category.builder().name("perawatan").build();
        categoryRepository.save(category);
        Product product = Product.builder().name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).categories(List.of(category)).build();
        Product productSaved = productRepostitory.save(product);
        ProductResponseDto expectedResult = ProductResponseDto.builder().id(productSaved.getId()).name("Soklin").description("sabun pencuci pakaian").price(Double.valueOf(9500)).stock(BigInteger.valueOf(20)).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productSaved.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ResponseSuccess actualResponse = objectMapper.readValue(responseString, ResponseSuccess.class);
        String responseDataString = objectMapper.writeValueAsString(actualResponse.getData());
        ProductResponseDto actualResult = objectMapper.readValue(responseDataString, ProductResponseDto.class);

        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(200, actualResponse.getCode());
        Assertions.assertEquals(HttpStatus.OK.getReasonPhrase(), actualResponse.getStatus());
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
        Category category = Category.builder().name("perawatan").build();
        categoryRepository.save(category);
        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Soklin")
            .description("sabun pencuci pakaian")
            .price(Double.valueOf(9500))
            .stock(BigInteger.valueOf(20))
            .categoryIds(List.of(category.getId()))
            .build();
        String requestDtoString = objectMapper.writeValueAsString(requestDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestDtoString)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult result  = mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
        String responseString = result.getResponse().getContentAsString();

        ResponseSuccess actualResult = objectMapper.readValue(responseString, ResponseSuccess.class);
        
        Assertions.assertEquals(HttpStatus.OK.value(), actualResult.getCode());
        Assertions.assertEquals(HttpStatus.OK.getReasonPhrase(), actualResult.getStatus());
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
            .price(Double.valueOf(9500))
            .stock(BigInteger.valueOf(20))
            .build();
        Product product = Product.builder()
            .id(id)
            .name("Deterjen")
            .description("sabun pakaian")
            .price(Double.valueOf(9500))
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

        ResponseSuccess actualResult = objectMapper.readValue(responseString, ResponseSuccess.class);
        
        Assertions.assertEquals(HttpStatus.OK.value(), actualResult.getCode());
        Assertions.assertEquals(HttpStatus.OK.getReasonPhrase(), actualResult.getStatus());
    }

    @Test
    @WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION, value = "johndoe@gmail.com")
    void update_shouldThrowProductNotFoundException_whenGivenUpdateProductIsNotFound() throws Exception{
        UUID id = UUID.randomUUID();
        ProductRequestDto requestDto = ProductRequestDto.builder()
            .name("Soklin")
            .description("Sabun pencuci pakaian")
            .price(Double.valueOf(9500))
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
            .description("sabun pakaian")
            .price(Double.valueOf(9500))
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
