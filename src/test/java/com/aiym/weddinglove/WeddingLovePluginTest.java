package com.aiym.weddinglove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.PluginContext;

@ExtendWith(MockitoExtension.class)
class WeddingLovePluginTest {

    @Mock
    PluginContext context;

    @InjectMocks
    WeddingLovePlugin plugin;

    @Mock
    SchemeManager schemeManager;

    @Test
    void contextLoads() {
        plugin.start();
        plugin.stop();
    }
}
