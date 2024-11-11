package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipes")
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        log.debug("RecipeController.get");
        model.addAttribute("recipe", recipeService.get(id));
        return "recipe";
    }

    @GetMapping
    @RequestMapping("/edit")
    public String editNew(Model model) {
        log.debug("RecipeController.editNew");
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe-edit";
    }

    @GetMapping
    @RequestMapping("/edit/{id}")
    public String editExisting(@PathVariable Long id, Model model) {
        log.debug("RecipeController.editExisting");
        model.addAttribute("recipe", recipeService.get(id));
        return "recipe-edit";
    }

    @PostMapping
    @RequestMapping("")
    public String save(@ModelAttribute RecipeCommand command) {
        log.debug("RecipeController.save");
        RecipeCommand saved = recipeService.save(command);
        return String.format("redirect:/recipes/%s", saved.getId());
    }

    @GetMapping
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        log.debug("RecipeController.delete");
        recipeService.delete(id);
        return "redirect:/";
    }
}
