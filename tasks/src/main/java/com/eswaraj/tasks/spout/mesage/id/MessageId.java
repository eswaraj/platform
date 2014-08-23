package com.eswaraj.tasks.spout.mesage.id;

import java.io.Serializable;

public class MessageId<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private T data;
    private int retryCount = 0;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "MessageId [data=" + data + ", retryCount=" + retryCount + "]";
    }

}
