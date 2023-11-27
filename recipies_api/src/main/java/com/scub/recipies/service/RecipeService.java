package com.scub.recipies.service;

import com.scub.recipies.model.Recipes;
import com.scub.recipies.repository.RecipesRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URL;
import java.util.Optional;

@Data
@Service
public class RecipeService {
    @Autowired
    private RecipesRepository recipesRepository;

    public Iterable<Recipes> getRecipes() {
        return recipesRepository.findAll();
    }

    public Optional<Recipes> getRecipesByTitle(String title) {
        return recipesRepository.findByTitle(title);
    }

    public Optional<Recipes> getRecipesById(Long id) {
        return recipesRepository.findById(id);
    }
    @Transactional
    public Recipes saveRecipe(Recipes recipe) throws Exception {
        if (recipe.getId() != null) {
            Optional<Recipes> existingRecipe = recipesRepository.findByTitle(recipe.getTitle());
            if (existingRecipe.isPresent() && !existingRecipe.get().getId().equals(recipe.getId())) {
                throw new Exception("Une recette avec le même titre existe déjà.");
            }
        } else {
            if (recipesRepository.findByTitle(recipe.getTitle()).isPresent()) {
                throw new Exception("Une recette avec le même titre existe déjà.");
            }
        }
        if (!isValidUrl(recipe.getImageUrl())) {
            throw new Exception("L'URL de l'image n'est pas valide.");
        }
        return recipesRepository.save(recipe);
    }
    // TODO: mettre en place si j'ai le temps, une verification est déjà en place côté front en attendant
    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteRecipesById(final Long id) {
        recipesRepository.deleteById(id);
    }

    public Iterable<Recipes> searchRecipesByTitle(String title) {
        return recipesRepository.findRecipesByTitleContainingIgnoreCase(title);
    }
}
