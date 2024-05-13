package com.example.springbootintegratemongodb.controller.collection;

import com.example.springbootintegratemongodb.service.collection.CreateCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author fh
 */
@Api(tags = "集合操作-创建集合")
@RestController
@RequestMapping("/collection")
public class CreateCollectionController {

    @Resource
    private CreateCollectionService createCollectionService;

    @ApiOperation(value = "创建【集合】", notes = "创建【集合结果】。")
    @PostMapping("/create")
    public Object createCollection(String collectionName) {
        return createCollectionService.createCollection(collectionName);
    }

    @ApiOperation(value = "创建【固定大小】的【集合】", notes = "创建【固定大小】的【集合结果】。")
    @PostMapping("/createFixedSize")
    public Object createCollectionFixedSize() {
        return createCollectionService.createCollectionFixedSize();
    }

    @ApiOperation(value = "创建【验证文档数据】的集合", notes = "创建【验证文档数据】的集合。")
    @PostMapping("/createValidation")
    public Object createCollectionValidation() {
        return createCollectionService.createCollectionValidation();
    }

}
