package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ForeignServerBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ForeignServerBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("ForeignDestinations", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getForeignDestinations", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ForeignConnectionFactories", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getForeignConnectionFactories", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("InitialContextFactory", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getInitialContextFactory", "setInitialContextFactory");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionURL", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getConnectionURL", "setConnectionURL");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JNDIPropertiesCredentialEncrypted", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getJNDIPropertiesCredentialEncrypted", "setJNDIPropertiesCredentialEncrypted");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JNDIPropertiesCredential", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getJNDIPropertiesCredential", "setJNDIPropertiesCredential");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JNDIProperties", Class.forName("weblogic.j2ee.descriptor.wl.ForeignServerBeanDConfig"), "getJNDIProperties", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for ForeignServerBeanDConfigBeanInfo");
         }
      }
   }
}
