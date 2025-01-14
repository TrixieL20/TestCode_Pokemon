package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder()
                .name("pickachu")
                .type("electric")
                .build();

        pokemonDto = PokemonDto.builder()
                .name("pickachu")
                .type("electric")
                .build();

        review = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        reviewDto = ReviewDto.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();
    }

    @Test
    public void ReviewService_CreateReview_ReturnReviewDto() {
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(pokemon.getId(), reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }

    @Test
    public void ReviewService_FindByPokemonId_ReturnReviewDto() {
        when(reviewRepository.findByPokemonId(pokemon.getId())).thenReturn(Arrays.asList(review));


        List<ReviewDto> pokemonReturn = reviewService.getReviewsByPokemonId(pokemon.getId());

        Assertions.assertThat(pokemonReturn).isNotNull();
    }

    @Test
    public void ReviewService_FindById_ReturnReviewDto() {
        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        ReviewDto reviewReturn = reviewService.getReviewById(review.getId(), pokemon.getId());

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_UpdateReview_ReturnReviewDto() {
        review.setPokemon(pokemon);
        pokemon.setReviews(Arrays.asList(review));

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto reviewReturn = reviewService.updateReview(pokemon.getId(), review.getId(), reviewDto);

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_DeleteReview_ReturnVoid() {
        review.setPokemon(pokemon);
        pokemon.setReviews(Arrays.asList(review));

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        assertAll(() -> reviewService.deleteReview(review.getId(), pokemon.getId()));
    }
}