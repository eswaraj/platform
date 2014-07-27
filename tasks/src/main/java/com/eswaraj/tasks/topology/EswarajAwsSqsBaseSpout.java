package com.eswaraj.tasks.topology;


public abstract class EswarajAwsSqsBaseSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    private String awsQueueName;
    public EswarajAwsSqsBaseSpout(String awsQueueName) {

    }

    public String getAwsQueueName() {
        return awsQueueName;
    }

    public void setAwsQueueName(String awsQueueName) {
        this.awsQueueName = awsQueueName;
    }
}
