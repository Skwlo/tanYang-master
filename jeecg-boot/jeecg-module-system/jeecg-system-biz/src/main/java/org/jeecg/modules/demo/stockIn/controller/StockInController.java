package org.jeecg.modules.demo.stockIn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.stockIn.entity.StockIn;
import org.jeecg.modules.demo.stockIn.service.IStockInService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 入库记录表
 * @Author: jeecg-boot
 * @Date:   2025-06-13
 * @Version: V1.0
 */
@Tag(name="入库记录表")
@RestController
@RequestMapping("/stockIn/stockIn")
@Slf4j
public class StockInController extends JeecgController<StockIn, IStockInService> {
	@Autowired
	private IStockInService stockInService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stockIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "入库记录表-分页列表查询")
	@Operation(summary="入库记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StockIn>> queryPageList(StockIn stockIn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<StockIn> queryWrapper = QueryGenerator.initQueryWrapper(stockIn, req.getParameterMap());
		Page<StockIn> page = new Page<StockIn>(pageNo, pageSize);
		IPage<StockIn> pageList = stockInService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stockIn
	 * @return
	 */
	@AutoLog(value = "入库记录表-添加")
	@Operation(summary="入库记录表-添加")
	@RequiresPermissions("stockIn:stock_in:add")
	@PostMapping(value = "/add")
	public Result<Map<String, Object>> add(@RequestBody StockIn stockIn) {
		Result<Map<String, Object>> result = new Result<>();
		Map<String, Object> data = new HashMap<>();

		try {
			// 调用服务层处理入库逻辑，获取警告信息
			String warningMessage = stockInService.handleStockIn(stockIn);

			// 设置返回数据
			data.put("stockIn", stockIn);
			if (warningMessage != null) {
				data.put("warning", warningMessage);
				result.setMessage("添加成功，但库存已超过警戒值");
			} else {
				result.setMessage("添加成功");
			}

			result.setSuccess(true);
			result.setResult(data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败：" + e.getMessage());
		}
		return result;
	}
	
	/**
	 *  编辑
	 *
	 * @param stockIn
	 * @return
	 */
	@AutoLog(value = "入库记录表-编辑")
	@Operation(summary="入库记录表-编辑")
	@RequiresPermissions("stockIn:stock_in:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StockIn stockIn) {
		stockInService.updateById(stockIn);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入库记录表-通过id删除")
	@Operation(summary="入库记录表-通过id删除")
	@RequiresPermissions("stockIn:stock_in:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		stockInService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "入库记录表-批量删除")
	@Operation(summary="入库记录表-批量删除")
	@RequiresPermissions("stockIn:stock_in:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stockInService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "入库记录表-通过id查询")
	@Operation(summary="入库记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StockIn> queryById(@RequestParam(name="id",required=true) String id) {
		StockIn stockIn = stockInService.getById(id);
		if(stockIn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stockIn);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stockIn
    */
    @RequiresPermissions("stockIn:stock_in:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StockIn stockIn) {
        return super.exportXls(request, stockIn, StockIn.class, "入库记录表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("stockIn:stock_in:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StockIn.class);
    }

}
