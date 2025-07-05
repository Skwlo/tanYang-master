package org.jeecg.modules.demo.stockIn.service.impl;

import org.jeecg.modules.demo.stockIn.entity.StockIn;
import org.jeecg.modules.demo.stockIn.mapper.StockInMapper;
import org.jeecg.modules.demo.stockIn.service.IStockInService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.demo.inventory.entity.Inventory;
import org.jeecg.modules.demo.inventory.service.IInventoryService;
import java.math.BigDecimal;
/**
 * @Description: 入库记录表
 * @Author: jeecg-boot
 * @Date:   2025-06-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class StockInServiceImpl extends ServiceImpl<StockInMapper, StockIn> implements IStockInService {
    @Autowired
    private IInventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleStockIn(StockIn stockIn) throws Exception {
        // 自动计算总价：单价 × 入库数量
        if (stockIn.getUnitPrice() != null && stockIn.getQuantity() != null) {
            BigDecimal totalPrice = stockIn.getUnitPrice().multiply(stockIn.getQuantity());
            stockIn.setTotalPrice(totalPrice);
            log.info("自动计算总价: 单价={}, 数量={}, 总价={}",
                    stockIn.getUnitPrice(), stockIn.getQuantity(), totalPrice);
        }

        save(stockIn);
        String materialId = stockIn.getMaterialId();
        BigDecimal quantity = stockIn.getQuantity();

        // 查询库存记录
        Inventory inventory = inventoryService.lambdaQuery()
                .eq(Inventory::getMaterialId, materialId)
                .one();

        if (inventory != null) {
            // 库存数量
            inventory.setCurrentQuantity(inventory.getCurrentQuantity().add(quantity));
            inventory.setLastInDate(stockIn.getInDate());
            inventoryService.updateById(inventory);
            log.info("更新库存成功: 物资ID={}, 新库存={}", materialId, inventory.getCurrentQuantity());

            // 检查是否超过警戒值
            if (inventory.getCurrentQuantity().compareTo(BigDecimal.valueOf(200)) > 0) {
                String warning = materialId + " 库存已超过警戒数量";
                log.warn(warning);
                return warning; // 返回警告信息
            }
        } else {
            log.error("库存记录不存在，物资ID: {}", materialId);
            throw new Exception("库存记录不存在，无法完成入库操作");
        }

        return null; // 没有警告
    }
}

