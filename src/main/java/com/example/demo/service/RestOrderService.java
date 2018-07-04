package com.example.demo.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.vo.AccountBalanceVo;
import com.example.demo.vo.InventoryVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
public class RestOrderService {

	private Logger logger = LoggerFactory.getLogger(RestOrderService.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@ApiOperation(value="下单", notes="下单API")// 使用该注解描述接口方法信息  
    @ApiImplicitParams({@ApiImplicitParam(name = "customerCode", value = "用户编码", required = true, dataType = "String", paramType="query"),
    	@ApiImplicitParam(name = "sku", value = "商品编码", required = true, dataType = "String", paramType="query")})
	@RequestMapping(value = "/createOrder",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String createOrder(@RequestParam String customerCode,@RequestParam String sku) {
		logger.info("### RestOrderService.createOrder called");

		// 1. call wallet service
		ResponseEntity<AccountBalanceVo> accountBalanceVo = restTemplate.getForEntity("http://service-wallet/getAccountBalance?customerCode="+customerCode,
				AccountBalanceVo.class);
		logger.info("### call wallet service return:{}",accountBalanceVo.getBody());

		// 2. call inventory service
		ResponseEntity<InventoryVo> inventoryVo = restTemplate.getForEntity("http://service-inventory/getInventory?sku="+sku,
				InventoryVo.class);
		logger.info("### call inventory service return:{}",inventoryVo.getBody());

		String orderNo = UUID.randomUUID().toString();
		logger.info("### create order success,orderNo:{}",orderNo);
		return orderNo;
	}
	
}
