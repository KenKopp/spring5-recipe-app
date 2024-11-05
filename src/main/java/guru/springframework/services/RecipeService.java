package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeService(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe,
                         RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    public Set<Recipe> getAll() {
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    public Recipe get(Long id) {
        return recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
    }

    public RecipeCommand save(RecipeCommand command) {
        return recipeToRecipeCommand.convert(recipeRepository.save(recipeCommandToRecipe.convert(command)));
    }
}
