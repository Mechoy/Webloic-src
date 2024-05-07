package weblogic.ejb.container.monitoring;

import java.util.HashSet;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.EJBTransactionRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public abstract class EJBRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBRuntimeMBean {
   private EJBTransactionRuntimeMBean txRtMBean;
   private String ejbName;
   private Set resources = new HashSet();

   public EJBRuntimeMBeanImpl(String var1, String var2, EJBComponentRuntimeMBean var3) throws ManagementException {
      super(var1, var3, true, "EJBRuntimes");
      this.ejbName = var2;
      this.txRtMBean = new EJBTransactionRuntimeMBeanImpl(var1, this);
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public EJBTransactionRuntimeMBean getTransactionRuntime() {
      return this.txRtMBean;
   }

   public RuntimeMBean[] getResources() {
      return (RuntimeMBean[])((RuntimeMBean[])this.resources.toArray(new RuntimeMBean[this.resources.size()]));
   }

   public void addResource(RuntimeMBean var1) {
      this.resources.add(var1);
   }
}
