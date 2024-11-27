package guru.springframework.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {
    Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void getId() {
        String id = "4";
        category.setId(id);
        assertThat(category.getId()).isEqualTo(id);
    }

    @Test
    public void getName() {
    }

    @Test
    public void getRecipes() {
    }
}