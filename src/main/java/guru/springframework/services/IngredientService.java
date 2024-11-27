package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientService(RecipeRepository recipeRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository,
                             IngredientCommandToIngredient ingredientCommandToIngredient,
                             IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    public IngredientCommand get(String recipeId, String id) {
        Ingredient ingredient = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"))
            .getIngredients().stream().filter(i -> i.getId().equals(id)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
        command.setRecipeId(recipeId);
        return command;
    }

    public IngredientCommand save(IngredientCommand command) {
        Recipe recipe = recipeRepository.findById(command.getRecipeId())
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        Ingredient ingredient;
        UnitOfMeasure unitOfMeasure = null;
        if (command.getUnitOfMeasure() != null) {
            unitOfMeasure = unitOfMeasureRepository.findById(command.getUnitOfMeasureId())
                .orElseThrow(() -> new IllegalArgumentException("Unit of measure not found"));
        }

        if (command.getId() == null) {
            ingredient = ingredientCommandToIngredient.convert(command);
            ingredient.setUnitOfMeasure(unitOfMeasure);
            recipe.getIngredients().add(ingredient);
        } else {
            ingredient = recipe.getIngredients().stream().filter(i -> i.getId().equals(command.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
            ingredient.setAmount(command.getAmount());
            ingredient.setDescription(command.getDescription());
            ingredient.setUnitOfMeasure(unitOfMeasure);
        }

        recipeRepository.save(recipe);
        IngredientCommand result = ingredientToIngredientCommand.convert(ingredient);
        result.setRecipeId(recipe.getId());
        return result;
    }

    public void delete(String recipeId, String id) {
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        Ingredient ingredient = recipe.getIngredients().stream().filter(i -> i.getId().equals(id)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        recipe.getIngredients().remove(ingredient);
        recipeRepository.save(recipe);
    }
}
