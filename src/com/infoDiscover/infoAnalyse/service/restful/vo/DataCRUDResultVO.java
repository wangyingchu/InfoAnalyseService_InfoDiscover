package com.infoDiscover.infoAnalyse.service.restful.vo;

public class DataCRUDResultVO {

    public enum operationResultCodeValue {
        SUCCESS,FAILURE,INVALIDINPUT,UNKNOWN
    }

    public DataCRUDResultVO(){
        this.setOperationReturnCode(operationResultCodeValue.UNKNOWN);
    }

    private long operationExecuteTime;
    private operationResultCodeValue operationReturnCode;

    public long getOperationExecuteTime() {
        return operationExecuteTime;
    }

    public void setOperationExecuteTime(long operationExecuteTime) {
        this.operationExecuteTime = operationExecuteTime;
    }

    public operationResultCodeValue getOperationReturnCode() {
        return operationReturnCode;
    }

    public void setOperationReturnCode(operationResultCodeValue operationReturnCode) {
        this.operationReturnCode = operationReturnCode;
    }
}
