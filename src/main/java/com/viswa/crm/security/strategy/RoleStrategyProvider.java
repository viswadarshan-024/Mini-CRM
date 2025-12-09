package com.viswa.crm.security.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Factory Pattern
@Component
public class RoleStrategyProvider {

    private final Map<String, RoleStrategy> strategies;

    public RoleStrategyProvider(List<RoleStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getClass().getAnnotation(Component.class).value(),
                        s -> s
                ));
    }

    public RoleStrategy getStrategy(String roleName) {
        RoleStrategy strategy = strategies.get(roleName);
        if (strategy == null) {
            throw new RuntimeException("No strategy found for role: " + roleName);
        }
        return strategy;
    }
}
