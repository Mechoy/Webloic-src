package weblogic.management.mbeanservers.internal;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import javax.management.ObjectName;
import weblogic.management.NoAccessRuntimeException;
import weblogic.management.internal.SecurityHelper;
import weblogic.management.jmx.ObjectSecurityManager;
import weblogic.security.service.MBeanResource.ActionType;

public class WLSObjectSecurityManagerImpl implements ObjectSecurityManager {
   public static WLSObjectSecurityManagerImpl getInstance() {
      return WLSObjectSecurityManagerImpl.Maker.SINGLETON;
   }

   public boolean isAnonAccessAllowed(ObjectName var1, String var2, String var3) {
      return SecurityHelper.isAllowedAnon(var1, ActionType.READ, var2, var3, (PropertyDescriptor)null);
   }

   public void isAccessAllowed(ObjectName var1, String var2, String var3, BeanDescriptor var4, PropertyDescriptor var5) throws NoAccessRuntimeException {
      SecurityHelper.isAccessAllowed(var1, ActionType.READ, var2, var3, var4, var5);
   }

   private static class Maker {
      private static WLSObjectSecurityManagerImpl SINGLETON = new WLSObjectSecurityManagerImpl();
   }
}
