package weblogic.jms.saf;

import java.util.HashMap;
import java.util.HashSet;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SAFAgentRuntimeMBean;
import weblogic.management.runtime.SAFRuntimeMBean;

public class SAFRuntimeMBeanImpl extends RuntimeMBeanDelegate implements SAFRuntimeMBean {
   private static final HashMap agents = new HashMap();

   SAFRuntimeMBeanImpl(String var1) throws ManagementException {
      super(var1, true);
   }

   public synchronized void addAgent(SAFAgentRuntimeMBeanAggregator var1) {
      agents.put(var1.getName(), var1);
   }

   public synchronized void removeAgent(SAFAgentRuntimeMBeanAggregator var1) {
      agents.remove(var1.getName());
   }

   public synchronized SAFAgentRuntimeMBeanAggregator getAgent(String var1) {
      return (SAFAgentRuntimeMBeanAggregator)agents.get(var1);
   }

   public synchronized HealthState getHealthState() {
      int var1 = 0;
      HashSet var2 = new HashSet();
      SAFAgentRuntimeMBean[] var3 = this.getAgents();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         HealthState var5 = var3[var4].getHealthState();
         var1 = Math.max(var5.getState(), var1);
         String[] var6 = var5.getReasonCode();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var2.add(var6[var7]);
         }
      }

      String[] var8 = (String[])((String[])var2.toArray(new String[var2.size()]));
      return new HealthState(var1, var8);
   }

   public synchronized SAFAgentRuntimeMBean[] getAgents() {
      SAFAgentRuntimeMBean[] var1 = new SAFAgentRuntimeMBean[agents.size()];
      return (SAFAgentRuntimeMBean[])((SAFAgentRuntimeMBean[])agents.values().toArray(var1));
   }
}
