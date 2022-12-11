package root.features;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client/features")
@PreAuthorize("hasRole('CLIENT')")
class FeaturesClientController {

    private final CommonFeaturesService commonFeaturesService;
    private final ClientFeaturesService clientFeaturesService;

    @GetMapping
    Features findAll(@AuthenticationPrincipal UserDetails principal) {
        var common = commonFeaturesService.findAll();
        var custom = clientFeaturesService.findAll(principal.getUsername());
        return new Features(common, custom);
    }

    private record Features (Set<Feature> common, Set<Feature> custom) {
    }
}
