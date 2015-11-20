package com.next.eswaraj.admin.jsf.bean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;

@Aspect
public class JsfBeanPerformanceAspect {

    @Around("com.next.eswaraj.admin.jsf.bean.ComplaintsBean.setSelectedComplaint() && args(complaintSearchResult,..)")
    public Object doBasicProfiling(ProceedingJoinPoint pjp, ComplaintSearchResult complaintSearchResult) throws Throwable {
        // start stopwatch
        StopWatch stopWatch = new StopWatch("setSelectedComplaint");
        stopWatch.start("setSelectedComplaint");
        Object retVal = pjp.proceed();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        // stop stopwatch
        return retVal;
    }
}
