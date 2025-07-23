//package com.qyl.model.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.tianxun.domian.entity.NcAnimationPlan;
//
//import javax.validation.constraints.NotEmpty;
//import java.io.Serial;
//import java.util.List;
//
//@EqualsAndHashCode(callSuper = true)
//@Data
//public class AnimationPlanDto extends NcAnimationPlan {
//
//
//    @Serial
//    private static final long serialVersionUID = -7440698849014776644L;
//
//    @Schema(description = "部门列表")
//    @NotEmpty(message = "部门列表不能为空")
//    private List<AnimationDeptDto> deptList;
//
//    @Schema(description = "指挥人员列表")
//    @NotEmpty(message = "指挥人员列表不能为空")
//    private List<AnimationConductDto> conductList;
//
//    @Schema(description = "模型列表")
//    @NotEmpty(message = "模型列表不能为空")
//    private List<String> modelList;
//
//    @Schema(description = "轨迹列表")
//    @NotEmpty(message = "轨迹列表不能为空")
//    private List<String> trackList;
//}
