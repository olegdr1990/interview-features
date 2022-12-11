package root.features;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface CommonFeaturesService {

    Set<Feature> findAll();

    Set<Feature> saveAll(@NotNull @Valid Set<Feature> features);
}
