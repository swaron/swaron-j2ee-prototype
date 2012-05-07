package org.app.ws.soap.endpoint;

import org.app.schemas.base.complextype.v10.Result;
import org.app.schemas.base.simpletype.v10.ResultCode;
import org.app.schemas.domain1.category1.v10.QueryOrderRequest;
import org.app.schemas.domain1.category1.v10.QueryOrderResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SayHello {

	@PayloadRoot(localPart = "checkingRequest", namespace = "http://app.org/schemas/domain1/category1/v10")
	@ResponsePayload
	public QueryOrderResponse echo(@RequestPayload QueryOrderRequest element) {
		QueryOrderResponse response = new QueryOrderResponse();
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS);
		result.setMessage("soap say hello");
		response.setResult(result);
		return response;
	}

}
