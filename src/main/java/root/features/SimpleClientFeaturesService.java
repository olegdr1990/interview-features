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
class SimpleClientFeaturesService implements ClientFeaturesService {

    private final JdbcOperations jdbc;
    private final ObjectMapper jackson;

    @Override
    public Set<Feature> findAll(String id) {
        return jdbc.query(
                "select features from client_features where id = ?",
                rs -> rs.next() ? toObject(rs.getString("features")) : Set.of(),
                id
        );
    }

    @Override
    public Set<Feature> saveAll(String id, @NotNull @Valid Set<Feature> features) {
        var sql = """
                    INSERT INTO client_features (id, features)
                    VALUES (?, ?::jsonb)
                    ON CONFLICT (id) DO UPDATE
                    SET features = EXCLUDED.features
                """;
        jdbc.update(sql, id, toText(features));
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
