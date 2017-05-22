package com.riskRating.servicesapi.exception;

public class RiskRatingServicesRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;		

	public RiskRatingServicesRuntimeException(String message) {
		super(message);
	}		
	public RiskRatingServicesRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	public RiskRatingServicesRuntimeException(Throwable cause) {
        super(cause);
    }
}
