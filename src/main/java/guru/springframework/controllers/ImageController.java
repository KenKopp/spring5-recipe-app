package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/recipes/image")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/edit/{id}")
    public String showUploadForm(@PathVariable Long id, Model model) {
        log.debug("ImageController.showUploadForm");
        model.addAttribute("recipe", recipeService.get(id));
        return "image-upload";
    }

    @GetMapping("/{id}")
    public void showImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        byte[] bytes = imageService.getImage(id);
        if (bytes != null) {
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(bytes);
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @PostMapping("/{id}")
    public String saveImageFile(@PathVariable Long id, @RequestParam("imagefile")MultipartFile file)
        throws IOException {
        log.debug("ImageController.saveImageFile");
        imageService.saveImageFile(id, file);
        return "redirect:/recipes/" + id;
    }
}
