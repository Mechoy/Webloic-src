package weblogic.management.j2ee.internal;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import weblogic.management.j2ee.EJBModuleMBean;

public final class EJBModuleMBeanImpl extends J2EEModuleMBeanImpl implements EJBModuleMBean {
   public EJBModuleMBeanImpl(String var1, String var2, String var3, ApplicationInfo var4) {
      super(var1, var2, var3, var4);
   }

   public String[] getejbs() {
      QueryExp var1 = new QueryExp() {
         public boolean apply(ObjectName var1) {
            return JMOTypesHelper.ejbList.contains(var1.getKeyProperty("j2eeType")) && var1.getKeyProperty("EJBModule").equals(EJBModuleMBeanImpl.this.getEJBModuleName());
         }

         public void setMBeanServer(MBeanServer var1) {
         }
      };
      return this.queryNames(var1);
   }

   private String getEJBModuleName() {
      try {
         return (new ObjectName(this.name)).getKeyProperty("name");
      } catch (MalformedObjectNameException var2) {
         throw new AssertionError("MalformedObjectName" + var2);
      }
   }
}
