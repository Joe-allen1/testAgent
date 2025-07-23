package com.qyl.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateGeoServerStyleDto {

    @Schema(description = "样式名称")
    @NotEmpty(message = "样式名称不能为空")
    private String oldName;

    @Schema(description = "工作空间")
    private String oldWorkSpaceName;

    @Schema(description = "样式名称")
    private String newName;

    @Schema(description = "工作空间")
    private String newWorkSpaceName;

    @Schema(description = "样式类型0--Point,1-- Line,2--Polygon")
    private Integer type;

//    @Schema(description = "SLD样式")
//    @NotEmpty(message = "SLD样式不能为空")
//    private String sld;

    private String styleName;      // 样式名称
    private String fillColor;      // 填充色，如 "#FF0000"
    private String strokeColor;    // 边框色，如 "#000000"
    private int    strokeWidth;    // 边框宽度，px
    private double opacity;        // 透明度，0~1
    private int    size;           // 点大小，px
}
