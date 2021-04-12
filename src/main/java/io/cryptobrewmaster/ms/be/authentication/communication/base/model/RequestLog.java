package io.cryptobrewmaster.ms.be.authentication.communication.base.model;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class RequestLog {

    private String preLog;
    private List<Object> preLogArgs;

    private String postLog;
    private List<Object> postLogArgs;

    private String errorLog;
    private List<Object> errorLogArgs;

    public RequestLog(String preLog, List<Object> preLogArgs,
                      String postLog, List<Object> postLogArgs,
                      String errorLog, List<Object> errorLogArgs) {
        this.preLog = preLog;
        this.preLogArgs = new ArrayList<>(preLogArgs);
        this.postLog = postLog;
        this.postLogArgs = new ArrayList<>(postLogArgs);
        this.errorLog = errorLog;
        this.errorLogArgs = new ArrayList<>(errorLogArgs);
    }

    public String getPreLogMessage() {
        return String.format(preLog, preLogArgs.toArray());
    }

    public <T> String getPostLogMessage(T response) {
        postLogArgs.add(response.toString());
        postLog += " %s";
        return String.format(postLog, postLogArgs.toArray());
    }

    public String getPostLogMessage(HttpStatus httpStatus) {
        postLogArgs.add(httpStatus);
        postLog += " Status code = %s.";
        return String.format(postLog, postLogArgs.toArray());
    }

    public String getErrorLogMessage(Exception e) {
        errorLogArgs.add(e.getMessage());
        errorLog += " Error = %s.";
        return String.format(errorLog, errorLogArgs.toArray());
    }

}
