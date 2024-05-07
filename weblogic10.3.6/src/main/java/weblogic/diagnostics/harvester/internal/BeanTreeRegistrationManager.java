package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.jmx.AttributeSpec;
import com.bea.adaptive.harvester.jmx.AttributeTerm;
import com.bea.adaptive.harvester.jmx.BaseHarvesterImpl;
import com.bea.adaptive.harvester.jmx.RegistrationManager;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.management.JMException;
import javax.management.ObjectName;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RegistrationHandler;
import weblogic.management.provider.Service;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class BeanTreeRegistrationManager extends RegistrationManager implements RegistrationHandler {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDiagnosticsHarvesterTreeBeanPlugin");
   public static final String CATEGORY_NAME = "WLS-MBean";
   private ConcurrentHashMap<String, Class> classByTypes = new ConcurrentHashMap();
   private ConcurrentHashMap typeAttrMetaData = new ConcurrentHashMap();

   static BeanInfo getBeanInfoForInterface(String var0) {
      return TreeBeanHarvestableDataProviderHelper.getBeanInfo(var0);
   }

   public BeanTreeRegistrationManager(BaseHarvesterImpl var1) throws IOException, JMException {
      super(var1);
      this.initKnownTypes();
      ManagementService.getRuntimeAccess(KERNEL_ID).initiateRegistrationHandler(this);
   }

   private void addInstances(Object var1, String var2) throws Exception {
      Class var3 = var1.getClass();
      Iterator var4 = this.classByTypes.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Class var6 = (Class)this.classByTypes.get(var5);
         if (var6.isAssignableFrom(var3)) {
            this.newInstance(var5, var2, "WLS-MBean");
         }
      }

   }

   private void cacheClassNames(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];

         try {
            Class var4 = Class.forName(var3);
            this.classByTypes.put(var3, var4);
            this.addKnownTypeToCache(var3);
         } catch (ClassNotFoundException var5) {
         }
      }

   }

   public String getDescriptionForType(String var1) {
      BeanInfo var2 = getBeanInfoForInterface(var1);
      if (var2 != null) {
         BeanDescriptor var3 = var2.getBeanDescriptor();
         if (var3 != null) {
            return var3.getDisplayName();
         }
      }

      return mtf_base.getUnknownLabel();
   }

   public String[][] getHarvestableAttributes(String var1, String var2) {
      try {
         if (this.dbg.isDebugTEnabled() || this.dbg.isDebugCEnabled()) {
            this.harvester.beforeSync("getHarvestableAttributes(BeanTreeRegistrationManager)", this, "   typeName=" + var1 + "   attrNameRegex=" + var2);
         }

         synchronized(this) {
            this.harvester.afterSync("getHarvestableAttributes(BeanTreeRegistrationManager)");
            Pattern var4 = var2 != null ? Pattern.compile(var2) : null;
            BeanInfo var5 = getBeanInfoForInterface(var1);
            if (var5 == null) {
               String[][] var22 = (String[][])null;
               return var22;
            } else {
               PropertyDescriptor[] var6 = var5.getPropertyDescriptors();
               int var7 = var6 != null ? var6.length : 0;
               String[][] var8 = new String[var7][3];
               int var9 = 0;

               for(int var10 = 0; var10 < var7; ++var10) {
                  PropertyDescriptor var11 = var6[var10];
                  String var12 = var11.getName();
                  if (this.isUnharvestable(var12, var11) || var4 != null && !var4.matcher(var12).matches()) {
                     ++var9;
                     var8[var10] = null;
                  } else {
                     String var13 = var11.getPropertyType().getName();
                     String var14 = var11.getShortDescription();
                     var8[var10][0] = var12;
                     var8[var10][1] = var13;
                     var8[var10][2] = var14;
                     Object var15 = (Map)this.typeAttrMetaData.get(var1);
                     if (var15 == null) {
                        var15 = new ConcurrentHashMap();
                        this.typeAttrMetaData.put(var1, var15);
                     }

                     if (!((Map)var15).containsKey(var12)) {
                        ((Map)var15).put(var12, var11.getReadMethod());
                     }
                  }
               }

               String[][] var23;
               if (var9 <= 0) {
                  var23 = var8;
                  return var23;
               } else {
                  var23 = new String[var7 - var9][3];
                  int var24 = 0;

                  for(int var25 = 0; var25 < var7; ++var25) {
                     if (var8[var25] != null) {
                        var23[var24] = var8[var25];
                        ++var24;
                     }
                  }

                  String[][] var26 = var23;
                  return var26;
               }
            }
         }
      } finally {
         this.harvester.unsynced("getHarvestableAttributes(BeanTreeRegistrationManager)");
      }
   }

   private boolean isUnharvestable(String var1, PropertyDescriptor var2) {
      for(int var3 = 0; var3 < UNHARVESTABLE_ATTRIBUTE_DESCRIPTORS.length; ++var3) {
         String var4 = UNHARVESTABLE_ATTRIBUTE_DESCRIPTORS[var3];
         Boolean var5 = (Boolean)var2.getValue(var4);
         if (var5 != null && (var4.equals("unharvestable") || var5)) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Attribute " + var1 + " is unharvestable, tagged with @" + var4);
            }

            return true;
         }
      }

      return false;
   }

   public List<AttributeSpec> getHarvestableAttributesForInstance(String var1, String var2) {
      ArrayList var3 = new ArrayList();
      String[][] var4 = this.getHarvestableAttributes(var2, (String)null);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String[] var6 = var4[var5];
            AttributeSpec var7 = new AttributeSpec(var6[0], var6[1], var2, new AttributeTerm.SimpleTerm(var6[0], (AttributeTerm)null));
            var3.add(var7);
         }
      }

      return var3;
   }

   private void initKnownTypes() throws IOException {
      String[] var1 = ManagementService.getBeanInfoAccess().getSubtypes(RuntimeMBean.class.getName());
      this.cacheClassNames(var1);
      String[] var2 = ManagementService.getBeanInfoAccess().getSubtypes(Service.class.getName());
      this.cacheClassNames(var2);
   }

   private void processRegistration(Object var1) throws Exception {
      String var2 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(var1);
      String var3 = TreeBeanHarvestableDataProviderHelper.getTypeNameForInstance(var2);
      this.newInstance(var3, var2, "WLS-MBean");
   }

   private void processUnregistration(Object var1) {
      try {
         String var2 = TreeBeanHarvestableDataProviderHelper.getRegisteredObjectNameForBean(var1);
         if (var2 != null) {
            this.instanceDeleted(var2);
         } else if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("No ObjectName found for bean " + var1.toString());
         }
      } catch (Exception var3) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception deleting instance", var3);
         }
      }

   }

   public void registered(RuntimeMBean var1, DescriptorBean var2) {
      try {
         this.processRegistration(var1);
      } catch (Exception var4) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception while registering bean", var4);
         }
      }

   }

   public void registered(Service var1) {
      try {
         this.processRegistration(var1);
      } catch (Exception var3) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Exception while registering bean", var3);
         }
      }

   }

   public void registeredCustom(ObjectName var1, Object var2) {
   }

   public void shutdown() {
      this.classByTypes = new ConcurrentHashMap();
      ManagementService.getRuntimeAccess(KERNEL_ID).removeRegistrationHandler(this);
   }

   public void unregistered(RuntimeMBean var1) {
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Processing unregistration for " + var1.getName());
      }

      this.processUnregistration(var1);
   }

   public void unregistered(Service var1) {
      this.processUnregistration(var1);
   }

   public void unregisteredCustom(ObjectName var1) {
   }

   Method getReadMethod(String var1, String var2) {
      Method var3 = null;
      Map var4 = (Map)this.typeAttrMetaData.get(var1);
      if (var4 != null) {
         var3 = (Method)var4.get(var2);
      }

      return var3;
   }
}
