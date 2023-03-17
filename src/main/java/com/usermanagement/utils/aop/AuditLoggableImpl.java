package com.usermanagement.utils.aop;

import com.usermanagement.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLoggableImpl implements AuditLoggable{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void auditLog(Object o, String eventName) {
        String auditMsg = "";
        if (eventName.equals("INSERT")) {
            auditMsg += "Inserting ";
        } else if (eventName.equals("UPDATE")) {
            auditMsg += "Updating ";
        }

        if (o instanceof User) {
            auditMsg += "User";
        }

        logger.info(auditMsg);
    }
}
