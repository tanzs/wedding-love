package com.aiym.weddinglove.service;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

/**
 * 婚礼配置数据获取接口
 *
 * 负责从 Halo Setting 中读取配置原始数据。
 *
 * @author Tanzs
 * @since 1.0.0
 */
public interface SettingConfigGetter {


    /**
     * 获取基础配置
     * @return
     */
    Mono<BasicConfig> getBasicConfig();

    /**
     * 获取流程节点配置
     * @return
     */
    Mono<TimelineConfig> getTimelineConfig();

    /**
     * 获取相册配置
     * @return
     */
    Mono<AlbumConfig> getAlbumConfig();

    /**
     * 获取歌单配置
     * @return
     */
    Mono<AplayerConfig> getAplayerConfig();

    /**
     * 获取主题配置
     * @return
     */
    Mono<ThemeConfig> getThemeConfig();

    /**
     * 基础设置
     */
    @Data
    @Accessors(chain = true)
    class BasicConfig {

        public static final String GROUP = "basic";

        /**
         * 新郎姓名
         */
        private String groomName;

        /**
         * 新娘姓名
         */
        private String brideName;

        /**
         * 婚期（日期时间）
         */
        private String weddingDate;

        /**
         * 婚礼地点
         */
        private String weddingLocation;

        /**
         * 分享图 URL
         */
        private String shareImage;


        /**
         * 地图经纬度（经度,纬度）
         */
        private String mapLocation;

        /**
         * 是否启用天气显示
         */
        private Boolean enableWeather;

        /**
         * 城市名称（用于天气查询）
         */
        private String cityName;
    }

    /**
     * 流程节点配置
     */
    @Data
    @Accessors(chain = true)
    class TimelineConfig {

        public static final String GROUP = "timeline";

        /**
         * 流程节点列表
         */
        private List<TimelineNode> timelineNodes;
    }

    /**
     * 单个流程节点
     */
    @Data
    @Accessors(chain = true)
    class TimelineNode {
        /**
         * 节点标题
         */
        private String title;

        /**
         * 节点描述
         */
        private String description;

        /**
         * 开始时间
         */
        private String startTime;

        /**
         * 结束时间（可选）
         */
        private String endTime;

        /**
         * 节点类型（timepoint/timespan）
         */
        private String type;
    }

    /**
     * 相册配置
     */
    @Data
    @Accessors(chain = true)
    class AlbumConfig {

        public static final String GROUP = "album";

        /**
         * 选中的图库名称列表
         */
        private List<String> galleryName;

        /**
         * 展示模式（waterfall/carousel/rotating）
         */
        private String displayMode;

        /**
         * 每页显示数量
         */
        private Integer pageSize;
    }

    /**
     * 歌单配置
     */
    @Data
    @Accessors(chain = true)
    class AplayerConfig {
        public static final String GROUP = "aplayer";
        /**
         *  API Host
         */
        private String host;

        /**
         * 音乐服务商
         */
        private String server;

        /**
         * 类型
         */
        private String type;

        /**
         * 歌单ID
         */
        private String id;

        /**
         * 预加载
         */
        private String preload;

        /**
         * 默认音量大小
         */
        private Integer number;

        /**
         * 自动播放
         */
        private boolean autoplay;

        /**
         * 播放顺序
         */
        private String order;

        /**
         * 主题色
         */
        private String color;
    }


    /**
     * 主题配置
     */
    @Data
    @Accessors(chain = true)
    class ThemeConfig {
        public static final String GROUP = "theme";
        /**
         * 主题颜色
         */
        private Map<String, String> colors;

        /**
         * 字体设置
         */
        private Map<String, String> fonts;

        /**
         * 自定义 CSS
         */
        private String customCss;
    }


}
