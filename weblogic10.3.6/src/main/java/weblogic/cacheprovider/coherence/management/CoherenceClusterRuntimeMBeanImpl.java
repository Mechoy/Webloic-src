package weblogic.cacheprovider.coherence.management;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import weblogic.management.ManagementException;
import weblogic.management.runtime.CoherenceClusterRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class CoherenceClusterRuntimeMBeanImpl extends RuntimeMBeanDelegate implements CoherenceClusterRuntimeMBean {
   private ObjectName objectName;
   private MBeanServer mbeanSrvr;

   public CoherenceClusterRuntimeMBeanImpl(ObjectName var1, MBeanServer var2, RuntimeMBean var3) throws ManagementException {
      super(var1.getDomain(), var3, true, "CoherenceClusterRuntime");
      this.objectName = var1;
      this.mbeanSrvr = var2;
   }

   public MBeanServer getMBeanServer() {
      return this.mbeanSrvr;
   }

   public String getClusterName() {
      return (String)this.get("ClusterName");
   }

   public Integer getClusterSize() {
      return (Integer)this.get("ClusterSize");
   }

   public String getLicenseMode() {
      return (String)this.get("LicenseMode");
   }

   public String[] getMembers() {
      Object var1 = this.get("Members");
      return (String[])((String[])var1);
   }

   public String getVersion() {
      return (String)this.get("Version");
   }

   private Object get(String var1) {
      try {
         return this.mbeanSrvr.getAttribute(this.objectName, var1);
      } catch (JMException var3) {
         return null;
      }
   }
}
