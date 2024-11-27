package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/edit")
    public String editNew(@RequestParam("recipeId") String recipeId, Model model) {
        log.debug("IngredientController.editNew");
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(recipeId);
        model.addAttribute("ingredient", command);
        model.addAttribute("unitOfMeasures", unitOfMeasureService.getAll());
        return "ingredient-edit";
    }

    @GetMapping
    @RequestMapping("/edit/{recipeId}/{id}")
    public String editExisting(@PathVariable String recipeId, @PathVariable String id, Model model) {
        log.debug("IngredientController.editExisting");
        model.addAttribute("ingredient", ingredientService.get(recipeId, id));
        model.addAttribute("unitOfMeasures", unitOfMeasureService.getAll());
        return "ingredient-edit";
    }

    @PostMapping
    @RequestMapping("")
    public String save(@ModelAttribute IngredientCommand command) {
        log.debug("IngredientController.save");
        IngredientCommand saved = ingredientService.save(command);
        return String.format("redirect:/recipes/edit/%s", saved.getRecipeId());
    }

    @GetMapping
    @RequestMapping("/delete/{recipeId}/{id}")
    public String delete(@PathVariable String recipeId, @PathVariable String id) {
        log.debug("IngredientController.delete");
        IngredientCommand ingredient = ingredientService.get(recipeId, id);
        ingredientService.delete(recipeId, id);
        return String.format("redirect:/recipes/edit/%s", ingredient.getRecipeId());
    }
}
