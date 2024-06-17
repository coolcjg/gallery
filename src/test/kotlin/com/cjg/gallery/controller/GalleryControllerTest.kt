package com.cjg.gallery.controller

import com.cjg.gallery.dto.GalleryDto
import com.cjg.gallery.dto.SearchParamDto
import com.cjg.gallery.service.GalleryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap

@WebMvcTest(controllers = [GalleryController::class])
@AutoConfigureMockMvc
class GalleryControllerTest() {

    @Autowired
    private lateinit var mockMvc : MockMvc;

    @Autowired
    private lateinit var objectMapper: ObjectMapper;

    @MockBean
    private lateinit var galleryService : GalleryService;

    @MockBean(name = "mongoMappingContext")
    private lateinit var mongoMappingContext :  MongoMappingContext;

    @Test
    @DisplayName("list")
    fun list(){

        var searchParamDto = SearchParamDto();

        var result = LinkedHashMap<String, Any>();
        val boardDtoList = mutableListOf<GalleryDto>();
        result.put("data", boardDtoList);

        given(galleryService.list(searchParamDto)).willReturn(result);

        var mvm = LinkedMultiValueMap<String, String>();
        mvm.add("pageNumber", "1");
        mvm.add("pageSize", "10");
        mvm.add("type", "all");
        mvm.add("day", "all");
        mvm.add("dateRange", "");

        mockMvc.perform(get("/gallery/list").params(mvm))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").isArray)
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("delete")
    fun deleteGallery(){

        val galleryDto = GalleryDto();
        galleryDto.galleryId = 1L;

        val result = HashMap<String, Any>();
        result["message"] = "success"

        given(galleryService.delete(galleryDto)).willReturn(result);

        mockMvc.perform(delete("/gallery")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(galleryDto)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("success"))
            .andDo(MockMvcResultHandlers.print());
    }
}