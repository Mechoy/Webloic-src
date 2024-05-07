package weblogic.cluster.messaging.internal.server;

import java.util.ArrayList;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.cluster.messaging.internal.Group;
import weblogic.cluster.messaging.internal.GroupMember;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.UnicastMessagingRuntimeMBean;

public class UnicastMessagingRuntimeMBeanImpl extends RuntimeMBeanDelegate implements UnicastMessagingRuntimeMBean {
   private static UnicastMessagingRuntimeMBeanImpl THE_ONE;

   UnicastMessagingRuntimeMBeanImpl(RuntimeMBean var1) throws ManagementException {
      super("UnicastMessagingRuntime", var1, true);
      THE_ONE = this;
   }

   public static UnicastMessagingRuntimeMBeanImpl getInstance() {
      return THE_ONE;
   }

   public int getRemoteGroupsDiscoveredCount() {
      return this.getDiscoveredGroupLeaders().length - 1;
   }

   public String getLocalGroupLeaderName() {
      Group var1 = Environment.getGroupManager().getLocalGroup();
      GroupMember[] var2 = var1.getMembers();
      return var2[0].getConfiguration().getServerName();
   }

   public int getTotalGroupsCount() {
      Group[] var1 = Environment.getGroupManager().getRemoteGroups();
      return var1.length + 1;
   }

   public String[] getDiscoveredGroupLeaders() {
      Group[] var1 = Environment.getGroupManager().getRemoteGroups();
      ArrayList var2 = new ArrayList();
      var2.add(this.getLocalGroupLeaderName());

      for(int var3 = 0; var3 < var1.length; ++var3) {
         GroupMember[] var4 = var1[var3].getMembers();
         if (var4 != null && var4.length > 0) {
            var2.add(var4[0].getConfiguration().getServerName());
         }
      }

      String[] var5 = new String[var2.size()];
      var2.toArray(var5);
      return var5;
   }

   public String getGroups() {
      StringBuffer var1 = new StringBuffer();
      Group var2 = Environment.getGroupManager().getLocalGroup();
      printGroup(var2, var1);
      Group[] var3 = Environment.getGroupManager().getRemoteGroups();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         printGroup(var3[var4], var1);
      }

      return var1.toString();
   }

   private static void printGroup(Group var0, StringBuffer var1) {
      GroupMember[] var2 = var0.getMembers();
      var1.append("{");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.append(var2[var3].getConfiguration().getServerName());
         if (var3 < var2.length - 1) {
            var1.append(",");
         }
      }

      var1.append("}");
   }
}
