package weblogic.management.mbeanservers.compatibility.internal;

import java.security.AccessController;
import java.util.Hashtable;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.ObjectNameManagerBase;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class CompatibilityObjectNameManager extends ObjectNameManagerBase {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public CompatibilityObjectNameManager() {
      super(DescriptorBean.class);
   }

   public boolean isFiltered(Object var1) {
      return !(var1 instanceof WebLogicMBean);
   }

   public boolean isClassMapped(Class var1) {
      if (WebLogicMBean.class.isAssignableFrom(var1)) {
         return true;
      } else {
         return StandardInterface.class.isAssignableFrom(var1);
      }
   }

   public ObjectName lookupObjectName(Object var1) {
      ObjectName var2 = super.lookupObjectName(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.buildObjectName(var1);

         assert var2 != null : "Unable to build ObjectName for " + var1;

         if (!this.isFiltered(var1)) {
            super.registerObject(var2, var1);
         }

         return var2;
      }
   }

   public ObjectName buildObjectName(Object var1) {
      if (var1 instanceof WebLogicMBean) {
         return buildWLSObjectName(var1);
      } else {
         return var1 instanceof StandardInterface ? buildCommoObjectName(var1) : null;
      }
   }

   public static ObjectName buildWLSObjectName(Object var0) {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getDomainName();
      String var2 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      WebLogicObjectName var3 = null;
      Hashtable var4 = new Hashtable();
      WebLogicMBean var5 = (WebLogicMBean)var0;
      String var6 = var5.getType();
      boolean var7 = false;
      if (var5 instanceof ConfigurationMBean) {
         ConfigurationMBean var8 = (ConfigurationMBean)var5;
         if (!var8.isEditable()) {
            var6 = var6 + "Config";
            var7 = true;
         }
      }

      var4.put("Type", var6);
      String var13 = var5.getName();

      for(WebLogicMBean var9 = var5.getParent(); var13 == null && var9 != null; var9 = var9.getParent()) {
         var13 = var9.getName();
      }

      var4.put("Name", var13);
      if (var7 || var5 instanceof RuntimeMBean) {
         var4.put("Location", var2);
      }

      for(WebLogicMBean var10 = var5.getParent(); var10 != null && !(var10 instanceof DomainMBean); var10 = var10.getParent()) {
         String var11 = var7 ? var10.getType() + "Config" : var10.getType();
         var4.put(var11, var10.getName());
      }

      try {
         if (var6.equals("AdminServer")) {
            var1 = "weblogic";
         }

         var3 = new WebLogicObjectName(var1, quoteObjectNameEntries(var4));
         return var3;
      } catch (MalformedObjectNameException var12) {
         throw new AssertionError("There is problem in constructing ObjectName " + var12);
      }
   }

   private static ObjectName buildCommoObjectName(Object var0) {
      if (!(var0 instanceof StandardInterface)) {
         throw new AssertionError(" Invalid object to register" + var0);
      } else {
         return ((StandardInterface)var0).wls_getObjectName();
      }
   }
}
