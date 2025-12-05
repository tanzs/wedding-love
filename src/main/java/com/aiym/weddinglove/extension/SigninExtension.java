package com.aiym.weddinglove.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

/**
 * 签到墙 Extension
 *
 * @author Tanzs
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@GVK(group = "wedding.aiym.fun", version = "v1alpha1", kind = "SigninRecord", plural = "signins", singular = "signin")
public class SigninExtension extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private SigninRecord spec;

    @Data
    @Schema(name = "SigninRecordSpc")
    public static class SigninRecord {
        /**
         * 姓名
         */
        private String name;

        /**
         * 签名/寄语
         */
        private String signature;

        /**
         * 生成的海报 URL
         */
        private String poster;

        /**
         * 海报模板
         */
        private String posterTemplate;

        /**
         * 签到时间
         */
        private LocalDateTime signinTime;

        /**
         * 来源（web/mobile）
         */
        private String source;

        /**
         * IP 地址
         */
        private String ipAddress;
    }
}

