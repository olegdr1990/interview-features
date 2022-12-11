package root.features;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import root.BasicIT;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class SimpleCommonFeaturesServiceTest extends BasicIT {

    @Autowired
    SimpleCommonFeaturesService subject;

    @Test
    void findAllIsEmpty() {
        var actual = subject.findAll();
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldSaveAll() {
        var features1 = Set.of(new Feature("1", true), new Feature("2", false));
        subject.saveAll(features1);
        var actual1 = subject.findAll();
        assertThat(actual1).hasSameElementsAs(features1);

        var features2 = Set.of(new Feature("2", false), new Feature("3", true), new Feature("5", true));
        subject.saveAll(features2);
        var actual2 = subject.findAll();
        assertThat(actual2).hasSameElementsAs(features2);
    }

    @Test
    void shouldNotHaveDuplicates() {
        subject.saveAll(Set.copyOf(List.of(new Feature("1", true), new Feature("1", false))));
        var actual = subject.findAll();
        assertThat(actual).hasSameElementsAs(Set.of(new Feature("1", true)));
    }

    @Test
    void shouldDeleteAll() {
        subject.saveAll(Set.of(new Feature("1", true), new Feature("2", false)));
        assertThat(subject.findAll()).hasSize(2);

        subject.saveAll(Set.of());
        assertThat(subject.findAll()).isEmpty();
    }

    @Test
    void shouldThrowIfSaveAllIsNull() {
        assertThatThrownBy(() -> subject.saveAll(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must not be null");
    }
}
