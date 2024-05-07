package weblogic.management.mbeanservers.internal;

import weblogic.management.mbeanservers.Service;

public class ServiceImpl extends weblogic.management.provider.ServiceImpl implements Service {
   public ServiceImpl(String var1, String var2, Service var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public ServiceImpl(String var1, String var2, Service var3) {
      this(var1, var2, var3, (String)null);
   }
}
