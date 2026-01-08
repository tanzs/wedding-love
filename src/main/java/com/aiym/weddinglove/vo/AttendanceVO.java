package com.aiym.weddinglove.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Tanzs
 * @date 2025/12/9 15:23
 * @description
 */
@Data
public class AttendanceVO {

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "人数")
    @NotNull(message = "人数不能为空")
    private Integer guestCount;

    @Schema(description = "备注")
    private String remark;

    @Schema
    private boolean approved = false;

}
