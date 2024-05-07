package weblogic.management.runtime;

import weblogic.management.ManagementException;

public interface LogRuntimeMBean extends RuntimeMBean {
   void forceLogRotation() throws ManagementException;

   void ensureLogOpened() throws ManagementException;
}
