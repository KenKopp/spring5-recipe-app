package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Notes;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@Slf4j
@Profile("default")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(RecipeRepository recipeRepository, CategoryRepository categoryRepository,
                      UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadRecipes();
    }

    private void loadRecipes() {
        log.debug("DataLoader.run");
        Category mexican = categoryRepository.findByName("Mexican")
            .orElseThrow(() -> new RuntimeException("Mexican not found"));
        Category fastFood = categoryRepository.findByName("Fast Food")
            .orElseThrow(() -> new RuntimeException("Fast Food not found"));

        UnitOfMeasure teaspoon = unitOfMeasureRepository.findByName("Teaspoon")
            .orElseThrow(() -> new RuntimeException("Teaspoon not found"));
        UnitOfMeasure tablespoon = unitOfMeasureRepository.findByName("Tablespoon")
            .orElseThrow(() -> new RuntimeException("Tablespoon not found"));
        UnitOfMeasure pound = unitOfMeasureRepository.findByName("Pound")
            .orElseThrow(() -> new RuntimeException("Pound not found"));
        UnitOfMeasure pinch = unitOfMeasureRepository.findByName("Pinch")
            .orElseThrow(() -> new RuntimeException("Pinch not found"));

        Recipe guacamole = createRecipe(
            "Perfect Guacamole",
            10,
            0,
            4,
            "SimplyRecipes",
            "https://www.simplyrecipes.com/recipes/perfect_guacamole",
            "Directions for making guacamole",
            Difficulty.MODERATE,
            Set.of(
                createIngredient("ripe avocados", BigDecimal.valueOf(2), null),
                createIngredient("kosher salt, plus more to taste", BigDecimal.valueOf(0.25), teaspoon),
                createIngredient("fresh lime or lemon juice", BigDecimal.valueOf(1), tablespoon),
                createIngredient("minced red onion or thinly sliced green onion", BigDecimal.valueOf(3), tablespoon),
                createIngredient("serrano (or jalapeño) chilis, stems and seeds removed, minced", BigDecimal.valueOf(2), null),
                createIngredient("cilantro (leaves and tender stems), finely chopped", BigDecimal.valueOf(2), tablespoon),
                createIngredient("freshly ground black pepper", BigDecimal.valueOf(1), pinch),
                createIngredient("ripe tomato, chopped (optional)", BigDecimal.valueOf(0.5), null),
                createIngredient("Red radish or jicama slices for garnish (optional)", null, null),
                createIngredient("Tortilla chips (to serve)", null, null)),
            Set.of(mexican),
            "Guacamole notes");
        recipeRepository.save(guacamole);

        Recipe tacos = createRecipe(
            "Easy Chicken Tacos",
            10,
            10,
            6,
            "SimplyRecipes",
            "https://www.simplyrecipes.com/ground-chicken-taco-recipe-8639156",
            "Directions for making easy chicken tacos",
            Difficulty.EASY,
            Set.of(
                createIngredient("neutral oil", BigDecimal.valueOf(1), tablespoon),
                createIngredient("ground chicken", BigDecimal.valueOf(1), pound),
                createIngredient("taco seasoning, plus more as needed to taste", BigDecimal.valueOf(2), tablespoon),
                createIngredient("(15-ounce) can black beans, drained and rinsed", BigDecimal.valueOf(1), null),
                createIngredient("(14.5-ounce) can diced tomatoes", BigDecimal.valueOf(1), null),
                createIngredient("Fresh lime juice to finish, optional", null, null),
                createIngredient("Tortillas, for serving", null, null)),
            Set.of(mexican, fastFood),
            "Taco notes");
        recipeRepository.save(tacos);
    }

    private Recipe createRecipe(String name, Integer prepTime, Integer cookTime, Integer servings,
                                String source, String url, String directions, Difficulty difficulty,
                                Set<Ingredient> ingredients, Set<Category> categories, String notesText) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setSource(source);
        recipe.setUrl(url);
        recipe.setDirections(directions);
        recipe.setDifficulty(difficulty);
        recipe.setIngredients(ingredients);
        recipe.setCategories(categories);
        Notes notes = new Notes();
        notes.setNotes(notesText);
        notes.setRecipe(recipe);
        recipe.setNotes(notes);
        return recipe;
    }

    private Ingredient createIngredient(String description, BigDecimal amount, UnitOfMeasure unit) {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(description);
        ingredient.setAmount(amount);
        ingredient.setUnitOfMeasure(unit);
        return ingredient;
    }
}
