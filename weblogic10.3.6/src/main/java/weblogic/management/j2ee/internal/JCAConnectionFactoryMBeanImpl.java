package weblogic.management.j2ee.internal;

import weblogic.management.j2ee.JCAConnectionFactoryMBean;

public final class JCAConnectionFactoryMBeanImpl extends J2EEManagedObjectMBeanImpl implements JCAConnectionFactoryMBean {
   private final String mmanagedConnectionFactory;

   public JCAConnectionFactoryMBeanImpl(String var1, String var2) {
      super(var1);
      this.mmanagedConnectionFactory = var2;
   }

   public String getmanagedConnectionFactory() {
      return this.mmanagedConnectionFactory;
   }
}
