package root.features;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Validated
class SimpleCommonFeaturesService implements CommonFeaturesService {

    private final JdbcOperations jdbc;
    private final ObjectMapper jackson;

    @Override
    public Set<Feature> findAll() {
        return jdbc.queryForObject(
                "select features from all_features",
                (rs, i) -> toObject(rs.getString("features"))
        );
    }

    @Override
    public Set<Feature> saveAll(@NotNull @Valid Set<Feature> features) {
        jdbc.update("update all_features set features = ?::jsonb", toText(features));
        return features;
    }

    @SneakyThrows
    private Set<Feature> toObject(String text) {
        return jackson.readValue(text, new TypeReference<>() {
        });
    }

    @SneakyThrows
    private String toText(Set<Feature> features) {
        return jackson.writeValueAsString(features);
    }
}
