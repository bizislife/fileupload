package com.example.fileupload.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * RPC request payload for evaluating a flowchart decision condition.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConditionRpcRequest {

    private String requestId;
    private String conditionType;
    private Map<String, String> conditionPayload;
    private String formInstanceUuid;
    private String timestamp;
    private String sourceSystem;

}
