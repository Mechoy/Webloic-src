package weblogic.connector.monitoring.outbound;

import java.io.Serializable;
import javax.management.j2ee.statistics.BoundedRangeStatistic;
import javax.management.j2ee.statistics.CountStatistic;
import javax.management.j2ee.statistics.JCAConnectionPoolStats;
import javax.management.j2ee.statistics.RangeStatistic;
import javax.management.j2ee.statistics.Statistic;
import javax.management.j2ee.statistics.TimeStatistic;
import weblogic.connector.common.Debug;
import weblogic.management.j2ee.statistics.BoundedRangeStatisticImpl;
import weblogic.management.j2ee.statistics.CountStatisticImpl;
import weblogic.management.j2ee.statistics.RangeStatisticImpl;
import weblogic.management.j2ee.statistics.StatException;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;

public class JCAConnectionPoolStatsImpl implements JCAConnectionPoolStats, Serializable {
   private String key;
   private long closeCount;
   private String connectionFactory;
   private long createCount;
   private String managedConnectionFactory;
   private long freeConnectionsCurrentCount;
   private long freePoolSizeHighWaterMark;
   private long freePoolSizeLowWaterMark;
   private long maxCapacity;
   private long poolSize;
   private long numWaiters;
   private long highestNumWaiters;
   protected static final String STAT_CLOSE_COUNT = Debug.getStringCloseCount();
   protected static final String STAT_CREATE_COUNT = Debug.getStringCreateCount();
   protected static final String STAT_FREE_POOL_SIZE = Debug.getStringFreePoolSize();
   protected static final String STAT_POOL_SIZE = Debug.getStringPoolSize();
   protected static final String STAT_WAITING_THREAD_COUNT = Debug.getStringWaitingThreadCount();
   protected static final String STAT_CLOSE_COUNT_DESCRIPTION = Debug.getStringCloseCountDescription();
   protected static final String STAT_CREATE_COUNT_DESCRIPTION = Debug.getStringCreateCountDescription();
   protected static final String STAT_FREE_POOL_SIZE_DESCRIPTION = Debug.getStringFreePoolSizeDescription();
   protected static final String STAT_POOL_SIZE_DESCRIPTION = Debug.getStringPoolSizeDescription();
   protected static final String STAT_WAITING_THREAD_COUNT_DESCRIPTION = Debug.getStringWaitingThreadCountDescription();

   public JCAConnectionPoolStatsImpl(ConnectorConnectionPoolRuntimeMBean var1) {
      this.key = var1.getKey();
      this.closeCount = var1.getCloseCount();
      this.connectionFactory = var1.getConnectionFactoryClassName();
      var1.getManagedConnectionFactoryClassName();
      this.managedConnectionFactory = var1.getManagedConnectionFactoryClassName();
      this.createCount = (long)var1.getConnectionsCreatedTotalCount();
      this.freeConnectionsCurrentCount = (long)var1.getFreeConnectionsCurrentCount();
      this.freePoolSizeHighWaterMark = var1.getFreePoolSizeHighWaterMark();
      this.freePoolSizeLowWaterMark = var1.getFreePoolSizeLowWaterMark();
      this.maxCapacity = (long)var1.getMaxCapacity();
      this.poolSize = var1.getCurrentCapacity();
      this.numWaiters = var1.getNumWaiters();
      this.highestNumWaiters = var1.getHighestNumWaiters();
   }

   public CountStatistic getCloseCount() {
      CountStatisticImpl var1 = null;

      try {
         var1 = new CountStatisticImpl(STAT_CLOSE_COUNT_DESCRIPTION, STAT_CLOSE_COUNT, this.key);
         var1.setCount(this.closeCount);
      } catch (StatException var3) {
      }

      return var1;
   }

   public CountStatistic getCreateCount() {
      CountStatisticImpl var1 = null;

      try {
         var1 = new CountStatisticImpl(STAT_CREATE_COUNT_DESCRIPTION, STAT_CREATE_COUNT, this.key);
         var1.setCount(this.createCount);
      } catch (StatException var3) {
      }

      return var1;
   }

   public BoundedRangeStatistic getFreePoolSize() {
      BoundedRangeStatisticImpl var1 = null;

      try {
         var1 = new BoundedRangeStatisticImpl(STAT_FREE_POOL_SIZE_DESCRIPTION, STAT_FREE_POOL_SIZE, this.key);
         var1.setCurrent(this.freeConnectionsCurrentCount);
         var1.setHighWaterMark(this.freePoolSizeHighWaterMark);
         var1.setLowWaterMark(this.freePoolSizeLowWaterMark);
         var1.setUpperBound(this.maxCapacity);
         var1.setLowerBound(0L);
      } catch (StatException var3) {
      }

      return var1;
   }

   public BoundedRangeStatistic getPoolSize() {
      BoundedRangeStatisticImpl var1 = null;

      try {
         var1 = new BoundedRangeStatisticImpl(STAT_POOL_SIZE_DESCRIPTION, STAT_POOL_SIZE, this.key);
         var1.setCurrent(this.poolSize);
         var1.setHighWaterMark(this.freePoolSizeHighWaterMark);
         var1.setLowWaterMark(this.freePoolSizeLowWaterMark);
         var1.setUpperBound(this.maxCapacity);
         var1.setLowerBound(0L);
      } catch (StatException var3) {
      }

      return var1;
   }

   public RangeStatistic getWaitingThreadCount() {
      RangeStatisticImpl var1 = null;

      try {
         var1 = new RangeStatisticImpl(STAT_WAITING_THREAD_COUNT_DESCRIPTION, STAT_WAITING_THREAD_COUNT, this.key);
         var1.setCurrent(this.numWaiters);
         var1.setHighWaterMark(this.highestNumWaiters);
         var1.setLowWaterMark(0L);
      } catch (StatException var3) {
      }

      return var1;
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
      if (var1 != null && var1.length() > 0) {
         if (var1.equals(STAT_CLOSE_COUNT)) {
            var2 = this.getCloseCount();
         } else if (var1.equals(STAT_CREATE_COUNT)) {
            var2 = this.getCreateCount();
         } else if (var1.equals(STAT_FREE_POOL_SIZE)) {
            var2 = this.getFreePoolSize();
         } else if (var1.equals(STAT_POOL_SIZE)) {
            var2 = this.getPoolSize();
         } else if (var1.equals(STAT_WAITING_THREAD_COUNT)) {
            var2 = this.getWaitingThreadCount();
         }
      }

      return (Statistic)var2;
   }

   public String[] getStatisticNames() {
      String[] var1 = new String[]{STAT_CLOSE_COUNT, STAT_CREATE_COUNT, STAT_FREE_POOL_SIZE, STAT_POOL_SIZE, STAT_WAITING_THREAD_COUNT};
      return var1;
   }

   public Statistic[] getStatistics() {
      Statistic[] var1 = new Statistic[]{this.getCloseCount(), this.getCreateCount(), this.getFreePoolSize(), this.getPoolSize(), this.getWaitingThreadCount()};
      return var1;
   }
}
