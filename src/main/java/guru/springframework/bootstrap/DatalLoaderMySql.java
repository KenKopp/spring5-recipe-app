package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile({"dev", "prod"})
public class DatalLoaderMySql implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DatalLoaderMySql(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadData();
    }

    private void loadData() {
        if (categoryRepository.count() == 0) {
            createCategory("American");
            createCategory("Italian");
            createCategory("Mexican");
            createCategory("Fast Food");
        }

        if (unitOfMeasureRepository.count() == 0) {
            createUnitOfMeasure("Teaspoon");
            createUnitOfMeasure("Tablespoon");
            createUnitOfMeasure("Cup");
            createUnitOfMeasure("Pinch");
            createUnitOfMeasure("Ounce");
            createUnitOfMeasure("Pound");
        }
    }

    private void createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }

    private void createUnitOfMeasure(String name) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setName(name);
        unitOfMeasureRepository.save(unitOfMeasure);
    }
}
