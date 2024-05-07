package weblogic.connector.outbound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.ResourcePoolProfiler;
import weblogic.connector.common.Debug;
import weblogic.diagnostics.instrumentation.EventDispatcher.Helper;
import weblogic.utils.StackTraceUtils;

public class ConnectionPoolProfiler implements ResourcePoolProfiler {
   private int NUM_TYPES = 4;
   private int TYPE_CONN_USAGE = 0;
   private int TYPE_CONN_WAIT = 1;
   private int TYPE_CONN_LEAK = 2;
   private int TYPE_CONN_RESV_FAIL = 3;
   private ConnectionPool pool;
   private HashMap[] profileData;
   private boolean resProfEnabled = false;

   ConnectionPoolProfiler(ConnectionPool var1) {
      this.pool = var1;
      this.profileData = new HashMap[this.NUM_TYPES];
      this.profileData[this.TYPE_CONN_USAGE] = new HashMap();
      this.profileData[this.TYPE_CONN_WAIT] = new HashMap();
      this.profileData[this.TYPE_CONN_LEAK] = new HashMap();
      this.profileData[this.TYPE_CONN_RESV_FAIL] = new HashMap();
      this.resProfEnabled = Boolean.getBoolean("weblogic.connector.ConnectionPoolProfilingEnabled");
   }

   private boolean isResourceProfilingEnabled() {
      return this.resProfEnabled;
   }

   public boolean isResourceUsageProfilingEnabled() {
      return this.isResourceProfilingEnabled();
   }

   public boolean isResourceReserveWaitProfilingEnabled() {
      return this.isResourceProfilingEnabled();
   }

   public boolean isResourceReserveFailProfilingEnabled() {
      return this.isResourceProfilingEnabled();
   }

   public boolean isResourceLeakProfilingEnabled() {
      return this.isResourceProfilingEnabled();
   }

   public synchronized void dumpData() {
      this.printData(this.profileData[this.TYPE_CONN_USAGE].values().iterator());
      this.printData(this.profileData[this.TYPE_CONN_WAIT].values().iterator());
      this.printData(this.profileData[this.TYPE_CONN_LEAK].values().iterator());
      this.printData(this.profileData[this.TYPE_CONN_RESV_FAIL].values().iterator());
   }

   public synchronized void harvestData() {
      if (this.isResourceProfilingEnabled()) {
         this.persistData("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.USAGE", this.profileData[this.TYPE_CONN_USAGE].values().iterator());
         this.persistData("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.WAIT", this.profileData[this.TYPE_CONN_WAIT].values().iterator());
         this.persistData("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.LEAK", this.profileData[this.TYPE_CONN_LEAK].values().iterator());
         this.persistData("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.RESVFAIL", this.profileData[this.TYPE_CONN_RESV_FAIL].values().iterator());
      }

   }

   public synchronized void deleteData() {
      this.deleteLeakData();
      this.deleteResvFailData();
   }

   public void addUsageData(PooledResource var1) {
      Properties var2 = new Properties();
      var2.setProperty("Connection", var1.toString());
      ProfileDataRecord var3 = new ProfileDataRecord("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.USAGE", this.pool.getName(), var2);
      synchronized(this) {
         this.profileData[this.TYPE_CONN_USAGE].put(var1, var3);
      }
   }

   public synchronized void deleteUsageData(PooledResource var1) {
      this.profileData[this.TYPE_CONN_USAGE].remove(var1);
   }

   public void addWaitData() {
      String var1 = Thread.currentThread().getName();
      Properties var2 = new Properties();
      var2.setProperty("ThreadID", var1);
      var2.setProperty("StackTrace", StackTraceUtils.throwable2StackTrace(new Exception()));
      ProfileDataRecord var3 = new ProfileDataRecord("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.WAIT", this.pool.getName(), var2);
      synchronized(this) {
         this.profileData[this.TYPE_CONN_WAIT].put(var1, var3);
      }
   }

   public void deleteWaitData() {
      synchronized(this) {
         this.profileData[this.TYPE_CONN_WAIT].remove(Thread.currentThread().getName());
      }
   }

   public void addLeakData(PooledResource var1) {
      Properties var2 = new Properties();
      var2.setProperty("Connection", var1.toString());
      ProfileDataRecord var3 = new ProfileDataRecord("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.LEAK", this.pool.getName(), var2);
      synchronized(this) {
         this.profileData[this.TYPE_CONN_LEAK].put(var1, var3);
      }
   }

   private void deleteLeakData() {
      this.profileData[this.TYPE_CONN_LEAK].clear();
   }

   public void addResvFailData(String var1) {
      Properties var2 = new Properties();
      var2.setProperty("ThreadID", Thread.currentThread().getName());
      var2.setProperty("StackTrace", var1);
      String var3 = Thread.currentThread().getName();
      ProfileDataRecord var4 = new ProfileDataRecord("WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.RESVFAIL", this.pool.getName(), var2);
      synchronized(this) {
         this.profileData[this.TYPE_CONN_RESV_FAIL].put(var3, var4);
      }
   }

   private void deleteResvFailData() {
      this.profileData[this.TYPE_CONN_RESV_FAIL].clear();
   }

   private synchronized void printData(Iterator var1) {
      while(var1.hasNext()) {
         ProfileDataRecord var2 = (ProfileDataRecord)var1.next();
         Debug.logPoolProfilingRecord(var2);
      }

   }

   private void persistData(String var1, Iterator var2) {
      while(var2.hasNext()) {
         Helper.dispatch(var1, (ProfileDataRecord)var2.next());
      }

   }
}
