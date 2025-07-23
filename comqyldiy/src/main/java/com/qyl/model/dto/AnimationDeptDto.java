package com.qyl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnimationDeptDto {

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;
}
