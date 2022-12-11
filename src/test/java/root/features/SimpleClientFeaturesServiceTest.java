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
class SimpleClientFeaturesServiceTest extends BasicIT {

    @Autowired
    SimpleClientFeaturesService subject;

    @Test
    void findAllIsEmpty() {
        var actual = subject.findAll("client1");
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldSaveAll() {
        var id = "client1";

        var features1 = Set.of(new Feature("1", true), new Feature("2", true));
        var actual1 = subject.saveAll(id, features1);
        assertThat(actual1).hasSameElementsAs(features1);

        var features2 = Set.of(new Feature("2", true), new Feature("3", true), new Feature("4", true));
        var actual2 = subject.saveAll(id, features2);
        assertThat(actual2).hasSameElementsAs(features2);
    }


    @Test
    void shouldNotHaveDuplicates() {
        var id = "client1";
        subject.saveAll(id, Set.copyOf(List.of(new Feature("1", true), new Feature("1", false))));
        var actual = subject.findAll(id);
        assertThat(actual).hasSameElementsAs(Set.of(new Feature("1", true)));
    }

    @Test
    void shouldDeleteAll() {
        var id = "client1";

        subject.saveAll(id, Set.of(new Feature("1", true), new Feature("2", false)));
        assertThat(subject.findAll(id)).hasSize(2);

        subject.saveAll(id, Set.of());
        assertThat(subject.findAll(id)).isEmpty();
    }

    @Test
    void shouldThrowIfSaveAllIsNull() {
        assertThatThrownBy(() -> subject.saveAll("any", null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must not be null");
    }
}
