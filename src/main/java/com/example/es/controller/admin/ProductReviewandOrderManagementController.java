package com.example.es.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "商品审核与订单管理", description = "商品审核与订单管理相关接口")
public class ProductReviewandOrderManagementController {

}
