package com.example.es.controller.merchant;

import com.example.es.auth.CurrentUserProvider;
import com.example.es.service.InventoriesService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.skuinventory.UpdateInventoryReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商家-库存管理")
@RestController
@RequestMapping("/merchant/inventories")
public class MerchantInventoryController {

    private final InventoriesService inventoriesService;
    private final CurrentUserProvider currentUserProvider;

    public MerchantInventoryController(InventoriesService inventoriesService,
                                       CurrentUserProvider currentUserProvider) {
        this.inventoriesService = inventoriesService;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "查询库存列表",
            description = "GET /merchant/inventories")
    @GetMapping
    public Response getInventories() {
        return inventoriesService.getMerchantInventories(
                currentUserProvider.getMerchantId()
        );
    }

    @Operation(summary = "修改库存",
            description = "PUT /merchant/inventories/{skuId}")
    @PutMapping("/{skuId}")
    public Response updateInventory(
            @PathVariable Long skuId,
            @RequestBody @Valid UpdateInventoryReqVO reqVO) {

        return inventoriesService.updateInventory(
                currentUserProvider.getMerchantId(),
                skuId,
                reqVO
        );
    }
}

