package com.analysis;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CalculatorWebService{
    @WebMethod
    int add(int num1, int num2);
}