package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
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

    public Set<RecipeCommand> getAll() {
        Set<RecipeCommand> recipes = new HashSet<>();
        recipeRepository.findAll().forEach(r -> recipes.add(recipeToRecipeCommand.convert(r)));
        return recipes;
    }

    public RecipeCommand get(String id) {
        return recipeRepository.findById(id).map(recipeToRecipeCommand::convert)
            .orElseThrow(() -> new NotFoundException(String.format("Recipe ID %s not found", id)));
    }

    public RecipeCommand save(RecipeCommand command) {
        return recipeToRecipeCommand.convert(recipeRepository.save(recipeCommandToRecipe.convert(command)));
    }

    public void delete(String id) {
        recipeRepository.deleteById(id);
    }
}
