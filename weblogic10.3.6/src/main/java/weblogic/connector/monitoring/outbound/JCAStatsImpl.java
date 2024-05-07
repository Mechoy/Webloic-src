package weblogic.connector.monitoring.outbound;

import java.io.Serializable;
import javax.management.j2ee.statistics.JCAConnectionPoolStats;
import javax.management.j2ee.statistics.JCAConnectionStats;
import javax.management.j2ee.statistics.JCAStats;
import javax.management.j2ee.statistics.Statistic;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;

public class JCAStatsImpl implements JCAStats, Serializable {
   JCAConnectionStats[] jcaConnectionStatsArray;
   JCAConnectionPoolStats[] jcaConnectionPoolStatsArray;

   public JCAStatsImpl(ConnectorComponentRuntimeMBean var1) {
      this.jcaConnectionStatsArray = this.initConnections(var1);
      this.jcaConnectionPoolStatsArray = this.initConnectionPools(var1);
   }

   public JCAConnectionStats[] getConnections() {
      return this.jcaConnectionStatsArray;
   }

   public JCAConnectionPoolStats[] getConnectionPools() {
      return this.jcaConnectionPoolStatsArray;
   }

   private JCAConnectionStats[] initConnections(ConnectorComponentRuntimeMBean var1) {
      return new JCAConnectionStats[0];
   }

   private JCAConnectionPoolStats[] initConnectionPools(ConnectorComponentRuntimeMBean var1) {
      ConnectorConnectionPoolRuntimeMBean[] var2 = var1.getConnectionPools();
      JCAConnectionPoolStats[] var3 = new JCAConnectionPoolStats[0];
      if (var2 != null && var2.length > 0) {
         var3 = new JCAConnectionPoolStats[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = new JCAConnectionPoolStatsImpl(var2[var4]);
         }
      }

      return var3;
   }

   public Statistic getStatistic(String var1) {
      return null;
   }

   public String[] getStatisticNames() {
      String[] var1 = new String[0];
      return var1;
   }

   public Statistic[] getStatistics() {
      Statistic[] var1 = new Statistic[0];
      return var1;
   }
}
