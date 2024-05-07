package weblogic.management.mbeanservers.internal.utils.typing;

import java.io.Serializable;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;

class QueryFilter implements QueryExp, Serializable {
   static final long serialVersionUID = 1L;
   private MBeanCategorizer categorizer;
   private MBeanServerConnection mbs;

   QueryFilter(MBeanCategorizer var1) {
      this.categorizer = var1;
   }

   public void setMBeanServer(MBeanServer var1) {
      this.mbs = var1;
   }

   public boolean apply(ObjectName var1) {
      return this.categorizer.categorize(this.mbs, var1) != null;
   }
}
