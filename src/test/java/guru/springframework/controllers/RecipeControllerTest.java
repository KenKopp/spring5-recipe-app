package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RecipeControllerTest {
    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
            .standaloneSetup(new RecipeController(recipeService))
            .setControllerAdvice(new ControllerExceptionHandler())
            .build();
    }

    @Test
    public void getRecipe() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);
        when(recipeService.get(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipes/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("recipe"))
            .andExpect(model().attributeExists("recipe"));

        verify(recipeService).get(1L);
    }

    @Test
    public void getRecipeNotFoundException() throws Exception {
        when(recipeService.get(anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/recipes/1"))
            .andExpect(status().isNotFound())
            .andExpect(view().name("404error"));
    }

    @Test
    public void getRecipeNumberFormatException() throws Exception {
        when(recipeService.get(anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/recipes/x"))
            .andExpect(status().isBadRequest())
            .andExpect(view().name("400error"));
    }
}