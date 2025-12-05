package com.aiym.weddinglove.service.impl;

import com.aiym.weddinglove.service.SettingConfigGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;

/**
 * 获取各个配置信息，并提供聚合后的 WeddingConfig。
 *
 * @author Tanzs
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SettingConfigGetterImpl implements SettingConfigGetter {

    private final ReactiveSettingFetcher settingFetcher;


    @Override
    public Mono<BasicConfig> getBasicConfig() {
        return settingFetcher.fetch(BasicConfig.GROUP, BasicConfig.class)
            .defaultIfEmpty(new BasicConfig());
    }

    @Override
    public Mono<TimelineConfig> getTimelineConfig() {
        return settingFetcher.fetch(TimelineConfig.GROUP, TimelineConfig.class)
            .defaultIfEmpty(new TimelineConfig());
    }

    @Override
    public Mono<AlbumConfig> getAlbumConfig() {
        return settingFetcher.fetch(AlbumConfig.GROUP, AlbumConfig.class)
            .defaultIfEmpty(new AlbumConfig());
    }

    @Override
    public Mono<AplayerConfig> getAplayerConfig() {
        return settingFetcher.fetch(AplayerConfig.GROUP, AplayerConfig.class)
            .defaultIfEmpty(new AplayerConfig());
    }

    @Override
    public Mono<ThemeConfig> getThemeConfig() {
        return settingFetcher.fetch(ThemeConfig.GROUP, ThemeConfig.class)
            .defaultIfEmpty(new ThemeConfig());
    }
}
