//package com.minizin.travel.tour.service;
//
//import com.minizin.travel.tour.domain.dto.TourAPIDto;
//import com.minizin.travel.tour.domain.entity.TourAPI;
//import com.minizin.travel.tour.domain.repository.TourAPIRepository;
//import com.nimbusds.jose.shaded.gson.Gson;
//import okhttp3.OkHttpClient;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * Class: TourServiceTest Project: travel Package: com.minizin.travel.tour.service
// * <p>
// * Description:
// *
// * @author dong-hoshin
// * @date 6/27/24 18:34 Copyright (c) 2024
// * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
// */
//class TourServiceTest {
//
//    @Mock
//    private TourAPIRepository tourAPIRepository;
//
//    @InjectMocks
//    private TourService tourService;
//
//    private MockWebServer mockWebServer;
//
//    private OkHttpClient client;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        MockitoAnnotations.openMocks(this);
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//        client = new OkHttpClient();
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    /*@Test
//    void testGetTourAPIFromSiteDetailCommon() throws Exception {
//        // given
//        TourAPIDto.TourRequest requestParam = new TourAPIDto.TourRequest();
//        requestParam.setPageNo("0");
//        requestParam.setNumOfRows("1");
//
//        TourAPI tourAPI = new TourAPI();
//        tourAPI.setContentId("129459");
//        tourAPI.setContentTypeId("12");
//        Page<TourAPI> tourAPIPage = new PageImpl<>(Collections.singletonList(tourAPI));
//        when(tourAPIRepository.findAll(any(PageRequest.class))).thenReturn(tourAPIPage);
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody("{\"response\": {\"body\": {\"items\": {\"item\": [{\"contentId\": \"129459\", \"contentTypeId\": \"12\"}]}}}}")
//            .addHeader("Content-Type", "application/json"));
//
//        // when
//        CompletableFuture<String> future = tourService.getTourAPIFromSiteDetailCommon(requestParam);
//        String result = future.join();
//
//        // then
//        assertEquals("getTourAPIFromSiteDetailCommon executed successfully", result);
//        verify(tourAPIRepository, times(1)).findAll(any(PageRequest.class));
//    }*/
//
//    @Test
//    void testGetTourAPIFromSiteSearchKeyword() throws Exception {
//        // given
//        TourAPIDto.TourRequest requestParam = new TourAPIDto.TourRequest();
//        requestParam.setKeyword("test");
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody("{\"response\": {\"body\": {\"items\": {\"item\": [{\"contentId\": \"1\", \"contentTypeId\": \"2\"}]}}}}")
//            .addHeader("Content-Type", "application/json"));
//
//        // when
//        CompletableFuture<List<TourAPI>> future = tourService.getTourAPIFromSiteSearchKeyword(requestParam);
//        List<TourAPI> result = future.join();
//
//        // then
//        assertEquals(1, result.size());
//        assertEquals("1", result.get(0).getContentId());
//        assertEquals("2", result.get(0).getContentTypeId());
//    }
//
//    @Test
//    void testGetTourAPIFromSiteAreaBasedList() throws Exception {
//        // given
//        TourAPIDto.TourRequest requestParam = new TourAPIDto.TourRequest();
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody("{\"response\": {\"body\": {\"items\": {\"item\": [{\"contentId\": \"1\", \"contentTypeId\": \"2\"}]}}}}")
//            .addHeader("Content-Type", "application/json"));
//
//        // when
//        CompletableFuture<List<TourAPI>> future = tourService.getTourAPIFromSiteAreaBasedList(requestParam);
//        List<TourAPI> result = future.join();
//
//        // then
//        assertEquals(1, result.size());
//        assertEquals("1", result.get(0).getContentId());
//        assertEquals("2", result.get(0).getContentTypeId());
//    }
//
//    @Test
//    void testGetTourAPIFromSiteAreaBasedListSingle() throws Exception {
//        // given
//        TourAPIDto.TourRequest requestParam = new TourAPIDto.TourRequest();
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody("{\"response\": {\"body\": {\"items\": {\"item\": [{\"contentId\": \"1\", \"contentTypeId\": \"2\"}]}}}}")
//            .addHeader("Content-Type", "application/json"));
//
//        // when
//        CompletableFuture<List<TourAPI>> future = tourService.getTourAPIFromSiteAreaBasedListSingle(requestParam);
//        List<TourAPI> result = future.join();
//
//        // then
//        assertEquals(1, result.size());
//        assertEquals("1", result.get(0).getContentId());
//        assertEquals("2", result.get(0).getContentTypeId());
//    }
//
//    @Test
//    void testGetTourAPIFromSiteAreaCode() throws Exception {
//        // given
//        TourAPIDto.TourRequest requestParam = new TourAPIDto.TourRequest();
//
//        mockWebServer.enqueue(new MockResponse()
//            .setBody("{\"response\": {\"body\": {\"items\": {\"item\": [{\"contentId\": \"1\", \"contentTypeId\": \"2\"}]}}}}")
//            .addHeader("Content-Type", "application/json"));
//
//        // when
//        CompletableFuture<List<TourAPI>> future = tourService.getTourAPIFromSiteAreaCode(requestParam);
//        List<TourAPI> result = future.join();
//
//        // then
//        assertEquals(1, result.size());
//        assertEquals("1", result.get(0).getContentId());
//        assertEquals("2", result.get(0).getContentTypeId());
//    }
//}