package com.aiym.weddinglove;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 *
 * @author Tanzs
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class WeddingLovePlugin extends BasePlugin {


    public WeddingLovePlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        System.out.println("插件启动成功！");
        // schemeManager.register(AttendanceExtension.class);
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
        // schemeManager.unregister(Scheme.buildFromType(AttendanceExtension.class));
    }
}
