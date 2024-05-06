package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService(endpointInterface = "com.analysis.Calculator")
public class CalculatorWebServiceImpl implements CalculatorWebService {
    @WebMethod
    public int add(int num1, int num2) {
        return num1 + num2;
    }
}