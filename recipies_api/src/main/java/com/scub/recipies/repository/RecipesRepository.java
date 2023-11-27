package com.scub.recipies.repository;

import com.scub.recipies.model.Recipes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesRepository extends CrudRepository<Recipes, Long> {
    Optional<Recipes> findByTitle(String title);

    Iterable<Recipes> findRecipesByTitleContainingIgnoreCase(String title);
}
