package com.pokemonreview.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;

    PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemonDto = PokemonDto.builder()
                .name("pickachu")
                .type("electric")
                .build();
    }

    @Test
    public void PokemonController_CreatePokemon_ReturnCreated() throws Exception {
        given(pokemonService.createPokemon(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.name"), CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(("$.type"), CoreMatchers.is(pokemonDto.getType())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void PokemonController_GetAllPokemon_ReturnResponseDto() throws Exception {
        PokemonResponse responseDto = PokemonResponse.builder()
                .pageSize(10)
                .last(true)
                .pageNo(1)
                .content(Arrays.asList(pokemonDto))
                .build();

        when(pokemonService.getAllPokemon(1, 10)).thenReturn(responseDto);


        ResultActions response = mockMvc.perform(get("/api/pokemon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.content.size()"), CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    public void PokemonController_GetPokemonDetail_ReturnResponseDto() throws Exception {
        when(pokemonService.getPokemonById(pokemonDto.getId())).thenReturn(pokemonDto);


        ResultActions response = mockMvc.perform(get("/api/pokemon/" + pokemonDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.name"), CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(("$.type"), CoreMatchers.is(pokemonDto.getType())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void PokemonController_UpdatePokemon_ReturnPokemonDto() throws Exception {
        when(pokemonService.updatePokemon(pokemonDto, pokemonDto.getId())).thenReturn(pokemonDto);


        ResultActions response = mockMvc.perform(put("/api/pokemon/" + pokemonDto.getId() + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.name"), CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(("$.type"), CoreMatchers.is(pokemonDto.getType())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void PokemonController_DeletePokemon_ReturnString() throws Exception {
        doNothing().when(pokemonService).deletePokemonId(pokemonDto.getId());

        ResultActions response = mockMvc.perform(delete("/api/pokemon/" + pokemonDto.getId() + "/delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}