package weblogic.j2ee;

import weblogic.management.ManagementException;
import weblogic.management.runtime.MailSessionRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class MailSessionRuntimeMBeanImpl extends RuntimeMBeanDelegate implements MailSessionRuntimeMBean {
   public MailSessionRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
   }
}
