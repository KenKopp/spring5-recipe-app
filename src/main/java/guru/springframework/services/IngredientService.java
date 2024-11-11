package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientService(IngredientRepository ingredientRepository,
                             RecipeRepository recipeRepository,
                             UnitOfMeasureRepository unitOfMeasureRepository,
                             IngredientCommandToIngredient ingredientCommandToIngredient,
                             IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    public IngredientCommand get(Long id) {
        return ingredientRepository.findById(id).map(ingredientToIngredientCommand::convert)
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
    }

    public IngredientCommand save(IngredientCommand command) {
        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
        ingredient.setRecipe(recipeRepository.findById(command.getRecipeId())
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found")));
        if (command.getUnitOfMeasureId() != null) {
            ingredient.setUnitOfMeasure(unitOfMeasureRepository.findById(command.getUnitOfMeasureId())
                .orElseThrow(() -> new IllegalArgumentException("Unit of measure not found")));
        } else {
            ingredient.setUnitOfMeasure(null);
        }
        return ingredientToIngredientCommand.convert(ingredientRepository.save(ingredient));
    }

    public void delete(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ingredient not found"));
        ingredient.getRecipe().getIngredients().remove(ingredient);
        ingredientRepository.delete(ingredient);
    }
}
