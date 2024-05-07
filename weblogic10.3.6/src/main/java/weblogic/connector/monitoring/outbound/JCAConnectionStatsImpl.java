package weblogic.connector.monitoring.outbound;

import java.io.Serializable;
import javax.management.j2ee.statistics.JCAConnectionStats;
import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.TimeStatistic;
import weblogic.management.runtime.ConnectorConnectionRuntimeMBean;

public class JCAConnectionStatsImpl implements JCAConnectionStats, Serializable {
   String connectionFactory;
   String managedConnectionFactory;

   public JCAConnectionStatsImpl(ConnectorConnectionRuntimeMBean var1) {
      this.connectionFactory = var1.getConnectionFactoryClassName();
      this.managedConnectionFactory = var1.getManagedConnectionFactoryClassName();
   }

   public String getConnectionFactory() {
      return this.connectionFactory;
   }

   public String getManagedConnectionFactory() {
      return this.managedConnectionFactory;
   }

   public TimeStatistic getWaitTime() {
      return null;
   }

   public TimeStatistic getUseTime() {
      return null;
   }

   public Statistic getStatistic(String var1) {
      Object var2 = null;
      return (Statistic)var2;
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
