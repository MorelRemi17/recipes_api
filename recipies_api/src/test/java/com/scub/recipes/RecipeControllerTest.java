package com.scub.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scub.recipies.Main;
import com.scub.recipies.controller.RecipeController;
import com.scub.recipies.model.Recipes;
import com.scub.recipies.repository.RecipesRepository;
import com.scub.recipies.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RecipesRepository recipesRepository;
    @InjectMocks
    private RecipeService recipeService;

    private Recipes recipe;

    private RecipeController recipeController;

    @Test
    public void testGetAllRecipes() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRecipeById() throws Exception {
        Long recipeId = 2L;
        mockMvc.perform(get("/api/recipes/{id}", recipeId))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveRecipeWithUniqueTitle() throws Exception {
        Recipes newRecipe = new Recipes();
        newRecipe.setTitle("Recette Unique");
        newRecipe.setDescription("Description de la recette unique");
        newRecipe.setIngredients("Ingrédients de la recette unique");
        newRecipe.setImageUrl("https://example.com/image.jpg");

        when(recipesRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(recipesRepository.save(any(Recipes.class))).thenReturn(newRecipe);

        Recipes savedRecipe = recipeService.saveRecipe(newRecipe);

        assertNotNull(savedRecipe);
        assertEquals("Recette Unique", savedRecipe.getTitle());
        verify(recipesRepository, times(1)).save(any(Recipes.class));
    }

    @Test
    public void testGetRecipeByTitle() {
        Recipes existingRecipe = new Recipes();
        existingRecipe.setId(1L);
        existingRecipe.setTitle("Recette Existante");
        existingRecipe.setDescription("Description de la recette existante");
        existingRecipe.setIngredients("Ingrédients de la recette existante");
        existingRecipe.setImageUrl("https://example.com/existing-image.jpg");

        when(recipesRepository.findByTitle("Recette Existante")).thenReturn(Optional.of(existingRecipe));

        Optional<Recipes> foundRecipe = recipeService.getRecipesByTitle("Recette Existante");

        assertTrue(foundRecipe.isPresent());
        assertEquals("Recette Existante", foundRecipe.get().getTitle());
        verify(recipesRepository, times(1)).findByTitle("Recette Existante");
    }

    @BeforeEach
    public void setUp() {
        recipe = new Recipes();
        recipe.setTitle("Tarte aux pommes");
        recipe.setDescription("Une délicieuse tarte aux pommes classique.");
        recipe.setIngredients("Pommes, pâte brisée, sucre, cannelle");
        recipe.setImageUrl("https://example.com/tarte-aux-pommes.jpg");
        recipe.setCookTime(60);
        recipe.setPrepTime(40);
        recipe.setAuthor(null);
    }

    @Test
    public void whenSavingRecipeWithTitleThatExists_thenThrowException() {
        when(recipesRepository.findByTitle(recipe.getTitle())).thenReturn(Optional.of(recipe));

        Exception exception = assertThrows(Exception.class, () -> {
            recipeService.saveRecipe(recipe);
        });

        String expectedMessage = "Une recette avec le même titre existe déjà.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(recipesRepository, times(1)).findByTitle(recipe.getTitle());
    }

    @Test
    public void whenCreateRecipeWithNegativePrepTime_thenThrowException() {
        Recipes newRecipe = new Recipes();
        newRecipe.setPrepTime(-10);
        assertThrows(Exception.class, () -> recipeController.createRecipe(newRecipe));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        Recipes newRecipe = new Recipes();
        newRecipe.setTitle("Nouvelle Recette 5");
        newRecipe.setDescription("au top !");
        newRecipe.setIngredients("pas plus !");
        newRecipe.setImageUrl("https://test.fr");
        newRecipe.setCookTime(20);
        newRecipe.setPrepTime(40);
        newRecipe.setAuthor(null);

        when(recipesRepository.save(any(Recipes.class))).thenReturn(newRecipe);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newRecipe)))
                .andExpect(status().isOk());
    }


}
