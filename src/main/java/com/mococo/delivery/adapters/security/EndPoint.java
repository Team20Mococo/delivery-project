package com.mococo.delivery.adapters.security;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public class EndPoint {

	private HttpMethod method;
	private String endPointName;

	public static EndPoint of(HttpMethod method, String endPointName) {
		EndPoint endPoint = new EndPoint();
		endPoint.method = method;
		endPoint.endPointName = endPointName;
		return endPoint;
	}
}
