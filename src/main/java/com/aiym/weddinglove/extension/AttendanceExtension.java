package com.aiym.weddinglove.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

/**
 * 搂席登记 Extension
 *
 * @author Tanzs
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@GVK(group = "wedding.aiym.fun", version = "v1alpha1", kind = "AttendanceRegistration", plural = "attendances", singular = "attendance")
public class AttendanceExtension extends AbstractExtension {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AttendanceRegistration spec;

    @Data
    @Schema(name = "AttendanceRegistrationSpc")
    public static class AttendanceRegistration {
        /**
         * 唯一标识
         */
        @Schema(description = "姓名", maxLength = 100)
        private String name;

        /**
         * 联系电话
         */
        @Schema(description = "手机号", format = "phone")
        private String phone;

        /**
         * 邮箱（非必填，用于接收审核通知）
         */
        @Schema(description = "用户电子邮箱", example = "user@example.com", format = "email")
        private String email;

        /**
         * 人数
         */
        private Integer guestCount;

        /**
         * 备注
         */
        private String remark;

        /**
         * 提交时间
         */
        private LocalDateTime submitTime;

        /**
         * 审核状态
         */
        private Boolean approved;

        /**
         * 备注（管理员）
         */
        private String adminRemark;
    }
}

