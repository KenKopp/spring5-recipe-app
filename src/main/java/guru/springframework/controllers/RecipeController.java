package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipes")
@Slf4j
public class RecipeController {
    private static final String RECIPE_EDIT_VIEW = "recipe-edit";

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String get(@PathVariable String id, Model model) {
        log.debug("RecipeController.get");
        model.addAttribute("recipe", recipeService.get(Long.parseLong(id)));
        return "recipe";
    }

    @GetMapping
    @RequestMapping("/edit")
    public String editNew(Model model) {
        log.debug("RecipeController.editNew");
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_EDIT_VIEW;
    }

    @GetMapping
    @RequestMapping("/edit/{id}")
    public String editExisting(@PathVariable Long id, Model model) {
        log.debug("RecipeController.editExisting");
        model.addAttribute("recipe", recipeService.get(id));
        return RECIPE_EDIT_VIEW;
    }

    @PostMapping
    @RequestMapping("")
    public String save(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {
        log.debug("RecipeController.save");
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                log.debug(error.toString());
            });
            return RECIPE_EDIT_VIEW;
        }
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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(Exception ex) {
        log.error("Handling not found exception", ex);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }
}
