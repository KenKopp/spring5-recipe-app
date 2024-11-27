package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    private final RecipeRepository recipeRepository;

    public ImageService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public byte[] getImage(String id) {
        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));
        byte[] bytes = null;
        if (recipe.getImage() != null) {
            bytes = new byte[recipe.getImage().length];
            int i = 0;
            for (byte b : recipe.getImage()) {
                bytes[i++] = b;
            }
        }
        return bytes;
    }

    public void saveImageFile(String id, MultipartFile file) throws IOException {
        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        Byte[] bytes = new Byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            bytes[i++] = b;
        }

        recipe.setImage(bytes);
        recipeRepository.save(recipe);
    }
}
