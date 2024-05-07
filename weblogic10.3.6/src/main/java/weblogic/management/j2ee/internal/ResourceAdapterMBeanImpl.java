package weblogic.management.j2ee.internal;

import weblogic.management.j2ee.ResourceAdapterMBean;

public class ResourceAdapterMBeanImpl extends J2EEManagedObjectMBeanImpl implements ResourceAdapterMBean {
   private final String mjcaResource;

   public ResourceAdapterMBeanImpl(String var1, String var2) {
      super(var1);
      this.mjcaResource = var2;
   }

   public String getjcaResource() {
      return this.mjcaResource;
   }
}
