package com.sistema.financeiro;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

    // Falha se um módulo usar algo interno de outro, ou se houver dependência circular (ADR 0004)
    @Test
    void verifiesModuleBoundaries() {
        ApplicationModules.of(FinanceiroApplication.class).verify();
    }
}
