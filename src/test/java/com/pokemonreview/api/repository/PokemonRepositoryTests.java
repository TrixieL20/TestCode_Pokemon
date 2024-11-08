package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {
        // Arrage
        Pokemon pokemon = Pokemon.builder()
                .name("Pickachuu")
                .type("electric")
                .build();

        // Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        // Assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnMoreThanOnePokemon() {
        // Arrange
        Pokemon pokemon1 = Pokemon.builder()
                .name("Pickachuu")
                .type("electric")
                .build();

        Pokemon pokemon2 = Pokemon.builder()
                .name("Pickachuu")
                .type("electric")
                .build();

        // Act
        pokemonRepository.save(pokemon1);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemons = pokemonRepository.findAll();

        // Assert
        Assertions.assertThat(pokemons).isNotNull();
        Assertions.assertThat(pokemons.size()).isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnMoreThanOnePokemon() {
        // Arrage
        Pokemon pokemon = Pokemon.builder()
                .name("pickachu")
                .type("electric")
                .build();

        // Act
        pokemonRepository.save(pokemon);
        Pokemon pokemonReturn = pokemonRepository.findById(pokemon.getId()).get();
        System.out.println(pokemonReturn);

        // Assert
        Assertions.assertThat(pokemonReturn).isNotNull();



    }

    @Test
    public void PokemonRepository_FindByType_ReturnNotNullPokemon() {
        // Arrage
        Pokemon pokemon = Pokemon.builder()
                .name("pickachu")
                .type("electric")
                .build();

        // Act
        pokemonRepository.save(pokemon);
        Pokemon pokemonReturn = pokemonRepository.findByType(pokemon.getType()).get();

        // Assert
        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnNotNullPokemon() {
        // Arrage
        Pokemon pokemon = Pokemon.builder()
                .name("pickachu")
                .type("electric")
                .build();

        // Act
        Pokemon pokemonSave = pokemonRepository.save(pokemon);

        pokemonSave.setType("Electric");
        pokemonSave.setName("Raichu");

        Pokemon updatedPokemon = pokemonRepository.save(pokemonSave);

        // Assert
        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
        Assertions.assertThat(updatedPokemon.getType()).isNotNull();
    }

    @Test
    public void PokemonRepository_DeleteById_ReturnPokemonIsEmpty () {
        // Arrage
        Pokemon pokemon = Pokemon.builder()
                .name("pickachu")
                .type("electric")
                .build();

        // Act
        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());
        Optional<Pokemon> pokemonReturn = pokemonRepository.findById(pokemon.getId());

        // Assert
        Assertions.assertThat(pokemonReturn).isEmpty();
    }
}
