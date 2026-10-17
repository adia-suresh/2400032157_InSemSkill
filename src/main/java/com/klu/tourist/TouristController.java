
package com.klu.tourist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@RestController
@RequestMapping("/api/tourists")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "Token"
)
public class TouristController {

    private final TouristRepository repository;

    public TouristController(TouristRepository repository) {
        this.repository = repository;
    }

   
    @PostMapping
    public ResponseEntity<Tourist> addTourist(
            @RequestBody Tourist tourist) {

        Tourist saved = repository.save(tourist);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete tourist by ID",
        security = @SecurityRequirement(
            name = "bearerAuth")
    )
    public ResponseEntity<?> deleteTourist(
            @PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Tourist not found"
                    ));
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
