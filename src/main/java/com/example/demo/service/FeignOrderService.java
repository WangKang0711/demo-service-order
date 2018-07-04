package com.example.demo.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.vo.AccountBalanceVo;
import com.example.demo.vo.InventoryVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class FeignOrderService {

	private Logger logger = LoggerFactory.getLogger(FeignOrderService.class);

	@Autowired
	private WalletServiceFeign walletServiceFeign;
	@Autowired
	private InventoryServiceFeign inventoryServiceFeign;
	
	@ApiOperation(value="下单", notes="下单API")// 使用该注解描述接口方法信息  
    @ApiImplicitParams({@ApiImplicitParam(name = "customerCode", value = "用户编码", required = true, dataType = "String", paramType="query"),
    	@ApiImplicitParam(name = "sku", value = "商品编码", required = true, dataType = "String", paramType="query")})
	@RequestMapping(value = "/createOrderByFeign",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String createOrderByFeign(@RequestParam String customerCode,@RequestParam String sku) {
		logger.info("### FeignOrderService.createOrderByFeign called");

		// 1. call wallet service
		AccountBalanceVo accountBalanceVo = walletServiceFeign.getAccountBalance(customerCode);
		logger.info("### call wallet service return:{}",accountBalanceVo);

		// 2. call inventory service
		InventoryVo inventoryVo = inventoryServiceFeign.getInventory(sku);
		logger.info("### call inventory service return:{}",inventoryVo);

		String orderNo = UUID.randomUUID().toString();
		logger.info("### create order success,orderNo:{}",orderNo);
		return orderNo;
	}
	
	
}
