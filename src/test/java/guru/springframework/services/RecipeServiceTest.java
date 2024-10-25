package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RecipeServiceTest {
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    public void getRecipes() {
        when(recipeRepository.findAll()).thenReturn(Set.of(new Recipe()));

        Set<Recipe> recipes = recipeService.getRecipes();

        assertThat(recipes.size()).isEqualTo(1);
        verify(recipeRepository, times(1)).findAll();
    }
}