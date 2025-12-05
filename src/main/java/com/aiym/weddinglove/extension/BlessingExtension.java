package com.aiym.weddinglove.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

/**
 * 祝福墙 Extension
 *
 * @author Tanzs
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@GVK(group = "wedding.aiym.fun", version = "v1alpha1", kind = "Blessing", plural = "blessings", singular = "blessing")
public class BlessingExtension extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Blessing spec;

    @Data
    @Schema(name = "BlessingSpc")
    public static class Blessing {
        /**
         * 姓名
         */
        private String name;

        /**
         * 祝福内容
         */
        private String blessing;

        /**
         * 头像 URL
         */
        private String avatar;

        /**
         * 提交时间
         */
        private LocalDateTime submitTime;

        /**
         * 是否显示（默认显示，后台可更改）
         */
        private Boolean visible;

        /**
         * IP 地址
         */
        private String ipAddress;

        /**
         * 用户代理
         */
        private String userAgent;
    }

}

