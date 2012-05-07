package org.app.ws.rest;

import org.app.schemas.base.complextype.v10.Result;
import org.app.schemas.base.simpletype.v10.ResultCode;
import org.app.schemas.domain1.category1.v10.QueryOrderRequest;
import org.app.schemas.domain1.category1.v10.QueryOrderResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/domain1")
public class EchoEndpoint {

	@RequestMapping(value = "/category1/print", produces = { "application/json", "application/xml" })
	@ResponseBody
	public QueryOrderResponse print() {
		QueryOrderResponse response = new QueryOrderResponse();
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS);
		result.setMessage("hello");
		response.setResult(result);
		return response;
	}

	@RequestMapping(value = "/category1/echo", produces = { "application/json", "application/xml" })
	@ResponseBody
	public QueryOrderResponse echo(@RequestBody QueryOrderRequest checkingRequest) {
		QueryOrderResponse response = new QueryOrderResponse();
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS);
		result.setMessage("hello");
		response.setResult(result);
		return response;
	}
}
