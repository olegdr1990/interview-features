package root.features;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface ClientFeaturesService {

    Set<Feature> findAll(String id);

    Set<Feature> saveAll(String id, @NotNull @Valid Set<Feature> features);
}
