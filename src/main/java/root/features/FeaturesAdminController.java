package root.features;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/features")
@PreAuthorize("hasRole('ADMIN')")
class FeaturesAdminController {

    private final CommonFeaturesService commonFeaturesService;
    private final ClientFeaturesService clientFeaturesService;

    @GetMapping("/common")
    Set<Feature> findAllCommon() {
        return commonFeaturesService.findAll();
    }

    @PostMapping("/common")
    Set<Feature> saveAllCommon(@RequestBody Set<Feature> features) {
        return commonFeaturesService.saveAll(features);
    }

    @GetMapping("/client/{id}")
    Set<Feature> findAllClient(@PathVariable String id) {
        return clientFeaturesService.findAll(id);
    }

    @PostMapping("/client/{id}")
    Set<Feature> saveAllClient(@PathVariable String id, @RequestBody Set<Feature> features) {
        return clientFeaturesService.saveAll(id, features);
    }
}
