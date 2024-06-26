package com.example.springbootintegratemongodb.controller.index;

import com.example.springbootintegratemongodb.service.index.RemoveIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author fh
 */
@Api(tags = "索引操作-删除索引")
@RestController
@RequestMapping("/index")
public class RemoveIndexController {

    @Resource
    private RemoveIndexService removeIndexService;

    @ApiOperation(value = "移除【集合】中索引", notes = "移除【集合】中索引。")
    @DeleteMapping("/remove/one")
    public void removeIndex() {
        removeIndexService.removeIndex();
    }

    @ApiOperation(value = "移除【集合】中【全部】索引", notes = "移除【集合】中【全部】索引。")
    @DeleteMapping("/remove/all")
    public void removeIndexAll() {
        removeIndexService.removeIndexAll();
    }

}
