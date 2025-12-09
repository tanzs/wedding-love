package com.aiym.weddinglove;

import com.aiym.weddinglove.extension.AttendanceExtension;
import com.aiym.weddinglove.extension.BlessingExtension;
import com.aiym.weddinglove.extension.SigninExtension;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Scheme;
import run.halo.app.extension.SchemeManager;
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

    @Autowired
    private SchemeManager schemeManager;

    public WeddingLovePlugin(PluginContext pluginContext, SchemeManager schemeManager) {
        super(pluginContext);
        this.schemeManager = schemeManager;
    }

    @Override
    public void start() {
        schemeManager.register(AttendanceExtension.class);
        schemeManager.register(BlessingExtension.class);
        schemeManager.register(SigninExtension.class);
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        schemeManager.unregister(Scheme.buildFromType(AttendanceExtension.class));
        schemeManager.unregister(Scheme.buildFromType(BlessingExtension.class));
        schemeManager.unregister(Scheme.buildFromType(SigninExtension.class));
        System.out.println("插件停止！");
    }
}
