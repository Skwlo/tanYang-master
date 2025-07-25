package org.jeecg.modules.demo.treatmentPlan.controller;

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
import org.jeecg.modules.demo.treatmentPlan.entity.TreatmentPlan;
import org.jeecg.modules.demo.treatmentPlan.service.ITreatmentPlanService;

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
 * @Description: 疾病治疗方案表
 * @Author: jeecg-boot
 * @Date:   2025-06-13
 * @Version: V1.0
 */
@Tag(name="疾病治疗方案表")
@RestController
@RequestMapping("/treatmentPlan/treatmentPlan")
@Slf4j
public class TreatmentPlanController extends JeecgController<TreatmentPlan, ITreatmentPlanService> {
	@Autowired
	private ITreatmentPlanService treatmentPlanService;
	
	/**
	 * 分页列表查询
	 *
	 * @param treatmentPlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "疾病治疗方案表-分页列表查询")
	@Operation(summary="疾病治疗方案表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TreatmentPlan>> queryPageList(TreatmentPlan treatmentPlan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("diseaseType", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("medicationType", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("usageMethod", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<TreatmentPlan> queryWrapper = QueryGenerator.initQueryWrapper(treatmentPlan, req.getParameterMap(),customeRuleMap);
		Page<TreatmentPlan> page = new Page<TreatmentPlan>(pageNo, pageSize);
		IPage<TreatmentPlan> pageList = treatmentPlanService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param treatmentPlan
	 * @return
	 */
	@AutoLog(value = "疾病治疗方案表-添加")
	@Operation(summary="疾病治疗方案表-添加")
	@RequiresPermissions("treatmentPlan:treatment_plan:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TreatmentPlan treatmentPlan) {
		treatmentPlanService.save(treatmentPlan);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param treatmentPlan
	 * @return
	 */
	@AutoLog(value = "疾病治疗方案表-编辑")
	@Operation(summary="疾病治疗方案表-编辑")
	@RequiresPermissions("treatmentPlan:treatment_plan:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TreatmentPlan treatmentPlan) {
		treatmentPlanService.updateById(treatmentPlan);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "疾病治疗方案表-通过id删除")
	@Operation(summary="疾病治疗方案表-通过id删除")
	@RequiresPermissions("treatmentPlan:treatment_plan:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		treatmentPlanService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "疾病治疗方案表-批量删除")
	@Operation(summary="疾病治疗方案表-批量删除")
	@RequiresPermissions("treatmentPlan:treatment_plan:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.treatmentPlanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "疾病治疗方案表-通过id查询")
	@Operation(summary="疾病治疗方案表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TreatmentPlan> queryById(@RequestParam(name="id",required=true) String id) {
		TreatmentPlan treatmentPlan = treatmentPlanService.getById(id);
		if(treatmentPlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(treatmentPlan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param treatmentPlan
    */
    @RequiresPermissions("treatmentPlan:treatment_plan:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TreatmentPlan treatmentPlan) {
        return super.exportXls(request, treatmentPlan, TreatmentPlan.class, "疾病治疗方案表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("treatmentPlan:treatment_plan:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TreatmentPlan.class);
    }

}
