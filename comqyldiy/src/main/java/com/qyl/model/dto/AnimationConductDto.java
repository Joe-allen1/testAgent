package com.qyl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnimationConductDto {

    @Schema(description = "指挥人员id")
    private String conductId;

    @Schema(description = "指挥人员名称")
    private String conductName;
}
