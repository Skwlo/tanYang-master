package org.jeecg.modules.demo.shedPen.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.shedPen.entity.ShedPen;
import org.jeecg.modules.demo.shedPen.service.IShedPenService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 场棚栏表
 * @Author: jeecg-boot
 * @Date:   2025-06-17
 * @Version: V1.0
 */
@Tag(name="场棚栏表")
@RestController
@RequestMapping("/shedPen/shedPen")
@Slf4j
public class ShedPenController extends JeecgController<ShedPen, IShedPenService> {
	@Autowired
	private IShedPenService shedPenService;
	
	/**
	 * 分页列表查询
	 *
	 * @param shedPen
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "场棚栏表-分页列表查询")
	@Operation(summary="场棚栏表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ShedPen>> queryPageList(ShedPen shedPen,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("type", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<ShedPen> queryWrapper = QueryGenerator.initQueryWrapper(shedPen, req.getParameterMap(),customeRuleMap);
		Page<ShedPen> page = new Page<ShedPen>(pageNo, pageSize);
		IPage<ShedPen> pageList = shedPenService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param shedPen
	 * @return
	 */
	@AutoLog(value = "场棚栏表-添加")
	@Operation(summary="场棚栏表-添加")
	@RequiresPermissions("shedPen:shed_pen:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ShedPen shedPen) {
		shedPenService.save(shedPen);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param shedPen
	 * @return
	 */
	@AutoLog(value = "场棚栏表-编辑")
	@Operation(summary="场棚栏表-编辑")
	@RequiresPermissions("shedPen:shed_pen:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ShedPen shedPen) {
		shedPenService.updateById(shedPen);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "场棚栏表-通过id删除")
	@Operation(summary="场棚栏表-通过id删除")
	@RequiresPermissions("shedPen:shed_pen:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		shedPenService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "场棚栏表-批量删除")
	@Operation(summary="场棚栏表-批量删除")
	@RequiresPermissions("shedPen:shed_pen:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shedPenService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "场棚栏表-通过id查询")
	@Operation(summary="场棚栏表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ShedPen> queryById(@RequestParam(name="id",required=true) String id) {
		ShedPen shedPen = shedPenService.getById(id);
		if(shedPen==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shedPen);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shedPen
    */
    @RequiresPermissions("shedPen:shed_pen:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShedPen shedPen) {
        return super.exportXls(request, shedPen, ShedPen.class, "场棚栏表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("shedPen:shed_pen:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ShedPen.class);
    }

}
