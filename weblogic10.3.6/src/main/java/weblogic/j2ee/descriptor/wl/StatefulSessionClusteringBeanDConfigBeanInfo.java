package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class StatefulSessionClusteringBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(StatefulSessionClusteringBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("HomeIsClusterable", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "isHomeIsClusterable", "setHomeIsClusterable");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("HomeLoadAlgorithm", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "getHomeLoadAlgorithm", "setHomeLoadAlgorithm");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("HomeCallRouterClassName", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "getHomeCallRouterClassName", "setHomeCallRouterClassName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseServersideStubs", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "isUseServersideStubs", "setUseServersideStubs");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReplicationType", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "getReplicationType", "setReplicationType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PassivateDuringReplication", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "isPassivateDuringReplication", "setPassivateDuringReplication");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CalculateDeltaUsingReflection", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "isCalculateDeltaUsingReflection", "setCalculateDeltaUsingReflection");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionClusteringBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for StatefulSessionClusteringBeanDConfigBeanInfo");
         }
      }
   }
}
