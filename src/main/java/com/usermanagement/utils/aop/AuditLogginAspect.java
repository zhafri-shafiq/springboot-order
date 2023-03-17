package com.usermanagement.utils.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogginAspect {

    @DeclareParents(value = "com.usermanagement.services.*", defaultImpl = AuditLoggableImpl.class)
    public static AuditLoggable auditLoggable;
}
