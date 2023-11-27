package com.scub.recipies.controller;

import com.scub.recipies.model.Recipes;
import com.scub.recipies.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class RecipeController {


    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Read - Get one recipe
     *
     * @param id
     * @return A Recipes object full filled
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipes> getRecipe(@PathVariable("id") final Long id) {
        Optional<Recipes> recipe = recipeService.getRecipesById(id);
        return recipe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Read - Get all recipes
     *
     * @return array of Recipes
     */
    @GetMapping("/recipes")
    public ResponseEntity<Iterable<Recipes>> getAllRecipes() {
        Iterable<Recipes> recipes = recipeService.getRecipes();
        return ResponseEntity.ok(recipes);
    }

    /**
     * Create - Create a recipe
     *
     * @param recipe
     * @throws Exception
     */
    @PostMapping("/recipes")
    public Recipes createRecipe(@RequestBody Recipes recipe) throws Exception {
        if (recipe.getPrepTime() < 1 || recipe.getCookTime() < 1) {
            throw new Exception("Les durées de préparation et de cuisson doivent être positives.");
        }
        return recipeService.saveRecipe(recipe);
    }

    /**
     * Delete - Delete an recipe
     *
     * @param id - The recipes_id of the recipe to delete
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") final Long id) {
        Optional<Recipes> recipe = recipeService.getRecipesById(id);
        if (recipe.isPresent()) {
            recipeService.deleteRecipesById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update - Update an recipe
     *
     * @param recipeId - The recipe_id of the recipe to update
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable(value = "id") Long recipeId,
                                          @RequestBody Recipes recipeDetails) {
        try {
            return recipeService.getRecipesById(recipeId).map(existingRecipe -> {
                existingRecipe.setTitle(recipeDetails.getTitle());
                existingRecipe.setDescription(recipeDetails.getDescription());
                existingRecipe.setIngredients(recipeDetails.getIngredients());
                existingRecipe.setImageUrl(recipeDetails.getImageUrl());
                Recipes updatedRecipe = null;
                try {
                    updatedRecipe = recipeService.saveRecipe(existingRecipe);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
            }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Read - Searching recipes by title
     *
     * @param title
     */
    @GetMapping("/recipes/search")
    public ResponseEntity<Iterable<Recipes>> searchRecipes(@RequestParam String title) {
        Iterable<Recipes> recipes = recipeService.searchRecipesByTitle(title);
        return ResponseEntity.ok(recipes);
    }
}
