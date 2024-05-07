package weblogic.management.mbeanservers.internal;

import java.beans.BeanInfo;
import java.util.Hashtable;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.WebLogicMBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.ObjectNameManagerBase;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.Service;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.runtime.DomainRuntimeMBean;

public class WLSObjectNameManager extends ObjectNameManagerBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXCore");
   private static final String BEA_DOMAIN_NAME = "com.bea";
   private final String domainName;
   private final BeanInfoAccess beanInfoAccess;
   private boolean addDomainToReadOnly = false;

   public void setAddDomainToReadOnly(boolean var1) {
      this.addDomainToReadOnly = var1;
   }

   public WLSObjectNameManager(String var1) {
      super(DescriptorBean.class);
      this.domainName = var1;
      this.beanInfoAccess = ManagementService.getBeanInfoAccess();
   }

   public boolean isClassMapped(Class var1) {
      if (var1.isPrimitive()) {
         return false;
      } else if (var1 == String.class) {
         return false;
      } else if (Service.class.isAssignableFrom(var1)) {
         return true;
      } else if (StandardInterface.class.isAssignableFrom(var1)) {
         return true;
      } else if (WebLogicMBean.class.isAssignableFrom(var1)) {
         return true;
      } else if (DescriptorBean.class.isAssignableFrom(var1)) {
         return true;
      } else if (this.beanInfoAccess.hasBeanInfo(var1)) {
         BeanInfo var2 = this.beanInfoAccess.getBeanInfoForInterface(var1.getName(), false, (String)null);
         Boolean var3 = (Boolean)var2.getBeanDescriptor().getValue("valueObject");
         return var3 == null || !var3;
      } else {
         return false;
      }
   }

   protected String getDomainName() {
      return this.domainName;
   }

   public ObjectName lookupObjectName(Object var1) {
      ObjectName var2 = super.lookupObjectName(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.buildObjectName(var1);
         if (var2 == null) {
            throw new Error("Unable to build an ObjectName for the instance " + var1 + " of class " + (var1 == null ? "null" : var1.getClass().getName()));
         } else {
            super.registerObject(var2, var1);
            if (debug.isDebugEnabled()) {
               String var3;
               if (var1 == null) {
                  var3 = "null";
               } else {
                  var3 = var1.getClass().getName() + "{" + var1.hashCode() + "}";
               }

               debug.debug("WLSObjectNameManager.lookupObjectName: registered in " + this + ":" + var2 + ":" + var3);
            }

            return var2;
         }
      }
   }

   public ObjectName buildObjectName(Object var1) {
      ObjectName var2 = null;
      if (var1 instanceof WebLogicMBean) {
         var2 = this.lookupWebLogicObjectName(var1);
      } else {
         if (var1 instanceof StandardInterface) {
            return buildCommoObjectName(var1);
         }

         if (var1 instanceof AbstractDescriptorBean) {
            var2 = this.lookupDescriptorObjectName((AbstractDescriptorBean)var1);
         } else if (var1 instanceof Service) {
            var2 = this.lookupServiceObjectName((Service)var1);
         }
      }

      return var2;
   }

   private ObjectName lookupServiceObjectName(Service var1) {
      ObjectName var2 = null;
      Hashtable var3 = new Hashtable();
      String var4 = var1.getType();
      if (var4 != null) {
         var3.put("Type", var1.getType());
      }

      String var5 = var1.getName();
      if (var5 != null) {
         var3.put("Name", var1.getName());
      }

      Service var6 = var1.getParentService();
      if (var6 != null) {
         var3.put("Path", var1.getPath());
      }

      try {
         var2 = new ObjectName("com.bea", quoteObjectNameEntries(var3));
         return var2;
      } catch (MalformedObjectNameException var8) {
         throw new RuntimeException(var8);
      }
   }

   private ObjectName lookupDescriptorObjectName(AbstractDescriptorBean var1) {
      ObjectName var2 = null;
      Hashtable var3 = new Hashtable();
      AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1.getDescriptor().getRootBean();
      DescriptorImpl var5 = (DescriptorImpl)var1.getDescriptor();
      AbstractDescriptorBean var6 = (AbstractDescriptorBean)var5.getContext().get("DescriptorConfigExtension");
      String var7 = (String)var5.getContext().get("DescriptorConfigExtensionAttribute");
      var3.put("Parent", var6._getQualifiedName());
      Object var8 = var1._getKey();

      for(AbstractDescriptorBean var9 = (AbstractDescriptorBean)var1.getParentBean(); var8 == null && var9 != null; var9 = (AbstractDescriptorBean)var9.getParentBean()) {
         var8 = var9._getKey();
      }

      if (var8 == null && var6 != null) {
         var8 = var6._getKey();
      }

      if (var8 != null) {
         var3.put("Name", var8.toString());
      }

      Object var10 = var4._getKey();
      if (var10 == null) {
         var10 = var6._getKey();
      }

      String var11 = var10.toString();
      StringBuffer var12 = new StringBuffer(var7);
      String var13 = var1._getQualifiedName();
      if (var13 != null && var13.length() > 0) {
         if (!var13.startsWith("[")) {
            var12.append("[");
            var12.append(var11);
            var12.append("]");
         }

         var12.append(var13);
      }

      var3.put("Path", var12.toString());
      var3.put("Type", this.beanInfoAccess.getInterfaceForInstance(var1).getName());
      if (this.addDomainToReadOnly && var1 instanceof AbstractDescriptorBean && !var1.getDescriptor().isEditable()) {
         var3.put("Location", this.domainName);
      }

      try {
         var2 = new ObjectName("com.bea", quoteObjectNameEntries(var3));
      } catch (MalformedObjectNameException var15) {
         return null;
      }

      if (debug.isDebugEnabled()) {
         debug.debug("Created new ObjectName =>" + var2.getCanonicalName());
      }

      return var2;
   }

   private ObjectName lookupWebLogicObjectName(Object var1) {
      ObjectName var2 = null;
      Hashtable var3 = new Hashtable();
      WebLogicMBean var4 = (WebLogicMBean)var1;
      var3.put("Type", var4.getType());
      String var5 = var4.getName();

      for(WebLogicMBean var6 = var4.getParent(); var5 == null && var6 != null; var6 = var6.getParent()) {
         var5 = var6.getName();
      }

      if (var5 == null) {
         throw new Error("Unable to determine name for bean " + var1);
      } else {
         var3.put("Name", var5);
         if (this.addDomainToReadOnly && var1 instanceof AbstractDescriptorBean) {
            AbstractDescriptorBean var7 = (AbstractDescriptorBean)var1;
            if (!var7.getDescriptor().isEditable()) {
               var3.put("Location", this.domainName);
            }
         }

         for(WebLogicMBean var10 = var4.getParent(); var10 != null && !(var10 instanceof DomainMBean) && !(var10 instanceof DomainRuntimeMBean); var10 = var10.getParent()) {
            var3.put(var10.getType(), var10.getName());
         }

         try {
            var2 = new ObjectName("com.bea", quoteObjectNameEntries(var3));
            return var2;
         } catch (MalformedObjectNameException var9) {
            throw new AssertionError("There is problem in constructing ObjectName " + var9);
         }
      }
   }

   String getShortName(DescriptorBean var1) {
      if (var1 instanceof WebLogicMBean) {
         return ((WebLogicMBean)var1).getName();
      } else {
         return var1 instanceof StandardInterface ? ((StandardInterface)var1).getName() : null;
      }
   }

   String getShortType(DescriptorBean var1) {
      if (var1 instanceof WebLogicMBean) {
         return ((WebLogicMBean)var1).getType();
      } else {
         return var1 instanceof StandardInterface ? ((StandardInterface)var1).wls_getInterfaceClassName() : null;
      }
   }

   private static ObjectName buildCommoObjectName(Object var0) {
      return ((StandardInterface)var0).wls_getObjectName();
   }

   static boolean isBEADomain(String var0) {
      return "com.bea".equals(var0);
   }
}
