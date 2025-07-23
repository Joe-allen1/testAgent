package com.qyl.controller;


import com.qyl.model.entity.LayerGroupRequest;
import com.qyl.model.entity.LayerItem;
import com.qyl.service.impl.LayerGroupServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/layergroup")
@RequiredArgsConstructor
@Tag(name = "fafaf")
public class LayerGroupController {

    private final LayerGroupServiceImpl layerGroupService;
//    @Autowired
//    private LayerGroupService layerGroupService;

    /**
     * 创建图层 + 图层组（首次使用或增量更新）
     */
    @PostMapping("/create")
    @Operation(summary = "创建图层组")
    public ResponseEntity<String> createLayerGroup(@RequestBody LayerGroupRequest request) {
        try {
            String groupName = request.getName();

            // Step 1: 判断图层组是否存在
            boolean groupExists = layerGroupService.layerGroupExists(groupName);

            // Step 2: 创建传入图层（仅不存在的图层）
            for (LayerItem item : request.getItems()) {
                if (!layerGroupService.layerExists(item.getLayer())) {
                    // qyl重点 创建图层 本体
                    layerGroupService.createLayer(item);
                }
            }

            // Step 3: 创建 or 更新图层组
            if (groupExists) {
                // 融合 图层组
                layerGroupService.mergeIntoLayerGroup(request);
                return ResponseEntity.ok("图层组已存在，已合并更新图层与样式");
            } else {
                // 创建 新的 图层组
                layerGroupService.createLayerGroup(request);
                return ResponseEntity.ok("图层组首次创建成功");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("图层组创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新图层组内容 + 刷新 BBOX（非首次）
     */
    @PostMapping("/update")
    @Operation(summary = "更新图层组")
    public ResponseEntity<String> updateLayerGroup(@RequestBody LayerGroupRequest request, @RequestParam LayerGroupServiceImpl.BoundingBox bbox) {
        try {
            // Step 1: 更新图层（如有必要）
            for (LayerItem item : request.getItems()) {
                if (!layerGroupService.layerExists(item.getLayer())) {
                    layerGroupService.createLayer(item);
                }
            }

            // Step 2: 更新图层组及其边界框
            layerGroupService.updateLayerGroup(request.getName(), request.getItems(), bbox);
            layerGroupService.updateLayerGroupBoundingBox(request.getName(), bbox);
            return ResponseEntity.ok("图层组更新成功（包含图层与边界框）");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("更新失败: " + e.getMessage());
        }
    }

    /**
     * 查询图层组详情（调试或配置可视化使用）
     */
    @GetMapping("/{name}")
    @Operation(summary = "查询")
    public ResponseEntity<?> getLayerGroupDetails(@PathVariable String name) {
        try {
            return ResponseEntity.ok(layerGroupService.getLayerGroup(name));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("图层组查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除图层组（可选）
     */
    @DeleteMapping("/{name}")
    @Operation(summary = "删除")
    public ResponseEntity<String> deleteLayerGroup(@PathVariable String name) {
        try {
            layerGroupService.deleteLayerGroup(name);
            return ResponseEntity.ok("图层组删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("删除失败: " + e.getMessage());
        }
    }



}

