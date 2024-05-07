package weblogic.management.j2ee.internal;

import weblogic.management.j2ee.JDBCDataSourceMBean;

public final class JDBCDataSourceMBeanImpl extends J2EEManagedObjectMBeanImpl implements JDBCDataSourceMBean {
   private String jdbcDriver = null;

   public JDBCDataSourceMBeanImpl(String var1, String var2) {
      super(var1);
      this.jdbcDriver = var2;
   }

   public String getjdbcDriver() {
      return this.jdbcDriver;
   }
}
