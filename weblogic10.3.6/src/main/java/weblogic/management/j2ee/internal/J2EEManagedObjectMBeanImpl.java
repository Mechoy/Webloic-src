package weblogic.management.j2ee.internal;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.QueryExp;

public class J2EEManagedObjectMBeanImpl {
   protected final String name;

   public J2EEManagedObjectMBeanImpl(String var1) {
      this.name = var1;
   }

   public boolean iseventProvider() {
      return false;
   }

   public boolean isstatisticsProvider() {
      return false;
   }

   public boolean isstateManageable() {
      return false;
   }

   public String getobjectName() {
      return this.name;
   }

   protected String[] queryNames(ObjectName var1) {
      return JMOService.getJMOService().getQueriedNames(var1);
   }

   protected String[] queryNames(QueryExp var1) {
      return JMOService.getJMOService().getQueriedNames(var1);
   }

   protected String getName(ObjectName var1) throws InstanceNotFoundException {
      MBeanServer var2 = JMOService.getJMOService().getJMOMBeanServer();
      return var2.getObjectInstance(var1).getObjectName().getCanonicalName();
   }
}
