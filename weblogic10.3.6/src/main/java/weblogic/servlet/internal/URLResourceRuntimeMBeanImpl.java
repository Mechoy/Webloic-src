package weblogic.servlet.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.URLResourceRuntimeMBean;

public class URLResourceRuntimeMBeanImpl extends RuntimeMBeanDelegate implements URLResourceRuntimeMBean {
   public URLResourceRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1);
   }
}
