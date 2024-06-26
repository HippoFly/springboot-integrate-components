package com.example.springbootintegratemongodb.service.collection;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 删除集合操作
 *
 * @author fh
 */
@Service
public class RemoveCollectionService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 删除【集合】
     *
     * @return 创建集合结果
     */
    public Object dropCollection( String collectionName) {
        // 执行删除集合
        mongoTemplate.getCollection(collectionName).drop();
        // 检测新的集合是否存在，返回删除结果
        return !mongoTemplate.collectionExists(collectionName) ? "删除集合成功" : "删除集合失败";
    }

}