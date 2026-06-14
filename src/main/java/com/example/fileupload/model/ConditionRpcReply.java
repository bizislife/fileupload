package com.example.fileupload.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * RPC reply payload returned after evaluating a flowchart decision condition.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConditionRpcReply {

    private String requestId;
    private boolean result;
    private String status;
    private String timestamp;
    private String processingNode;

}
