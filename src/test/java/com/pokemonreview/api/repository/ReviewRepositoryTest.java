package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReviewRepositoryTest {
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTest(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnSavedReview() {
        // Arrage
        Review review = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        // Act
        Review savedReview = reviewRepository.save(review);


        // Assert
        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepository_GetAll_ReturnMoreThanOneReview() {
        // Arrage
        Review review1 = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        Review review2 = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        // Act
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        List<Review> reviewReturn = reviewRepository.findAll();


        // Assert
        Assertions.assertThat(reviewReturn).isNotNull();
        Assertions.assertThat(reviewReturn.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_ReturnSavedReview() {
        // Arrage
        Review review = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        // Act
        Review savedReview = reviewRepository.save(review);
        Review reviewReturn = reviewRepository.findById(savedReview.getId()).get();


        // Assert
        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnReview() {
        // Arrage
        Review review = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        // Act
        Review savedReview = reviewRepository.save(review);

        savedReview.setTitle("updated");
        savedReview.setContent("updated");

        Review updatedReview = reviewRepository.save(savedReview);


        // Assert
        Assertions.assertThat(updatedReview.getTitle()).isNotNull();
        Assertions.assertThat(updatedReview.getContent()).isNotNull();
    }

    @Test
    public void ReviewRepository_DeleteById_ReturnReviewIsEmpty () {
        // Arrage
        Review review = Review.builder()
                .title("title")
                .content("contents")
                .stars(5)
                .build();

        // Act
        reviewRepository.save(review);

        reviewRepository.delete(review);
        Optional<Review> reviewReturn = reviewRepository.findById(review.getId());

        // Assert
        Assertions.assertThat(reviewReturn).isEmpty();
    }
}