package weblogic.xml.util.cache.entitycache;

import java.util.Enumeration;
import java.util.Vector;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EntityCacheRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public abstract class EntityCacheStats extends RuntimeMBeanDelegate implements EntityCacheRuntimeMBean {
   EntityCache cache = null;
   Statistics oldValues = null;

   public EntityCacheStats(String var1, RuntimeMBean var2, EntityCache var3) throws ManagementException {
      super(var1, var2);
      this.cache = var3;
   }

   abstract Statistics getStats();

   boolean changesMade() {
      return true;
   }

   synchronized void doNotifications() {
      if (this.changesMade()) {
         Vector var1 = null;
         Statistics var2 = this.getStats().copy();
         if (this.oldValues != null && (var1 = this.diff(this.oldValues, var2)) != null) {
            Enumeration var3 = var1.elements();

            while(var3.hasMoreElements()) {
               AttributeDiff var4 = (AttributeDiff)var3.nextElement();
               this._postSet(var4.attributeName, var4.oldValue, var4.newValue);
            }
         }

         this.oldValues = var2;
      }
   }

   Vector diff(Statistics var1, Statistics var2) {
      Vector var3 = new Vector();
      long var4 = 0L;
      long var6 = 0L;
      double var8 = 0.0;
      double var10 = 0.0;
      if ((var4 = var1.getTotalEntries()) != (var6 = var2.getTotalEntries())) {
         var3.addElement(new AttributeDiff("TotalCurrentEntries", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var8 = var1.getMinEntryTimeout()) != (var10 = var2.getMinEntryTimeout())) {
         var3.addElement(new AttributeDiff("MinEntryTimeout", "" + var8, "" + var10, "java.lang.double"));
      }

      if ((var8 = var1.getMaxEntryTimeout()) != (var10 = var2.getMaxEntryTimeout())) {
         var3.addElement(new AttributeDiff("MaxEntryTimeout", "" + var8, "" + var10, "java.lang.double"));
      }

      if ((var4 = var1.getMinEntryMemorySize()) != (var6 = var2.getMinEntryMemorySize())) {
         var3.addElement(new AttributeDiff("MinEntryMemorySize", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getMaxEntryMemorySize()) != (var6 = var2.getMaxEntryMemorySize())) {
         var3.addElement(new AttributeDiff("MaxEntryMemorySize", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getMaxEntryMemorySizeRequested()) != (var6 = var2.getMaxEntryMemorySizeRequested())) {
         var3.addElement(new AttributeDiff("MaxEntryMemorySizeRequested", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getTotalNumberMemoryPurges()) != (var6 = var2.getTotalNumberMemoryPurges())) {
         var3.addElement(new AttributeDiff("TotalNumberMemoryPurges", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getTotalNumberDiskPurges()) != (var6 = var2.getTotalNumberDiskPurges())) {
         var3.addElement(new AttributeDiff("TotalNumberDiskPurges", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getTotalNumberOfRejections()) != (var6 = var2.getTotalNumberOfRejections())) {
         var3.addElement(new AttributeDiff("TotalNumberOfRejections", "" + var4, "" + var6, "java.lang.long"));
      }

      if ((var4 = var1.getTotalNumberOfRenewals()) != (var6 = var2.getTotalNumberOfRenewals())) {
         var3.addElement(new AttributeDiff("TotalNumberOfRenewals", "" + var4, "" + var6, "java.lang.long"));
      }

      return var3;
   }

   public synchronized long getTotalCurrentEntries() {
      return this.getStats().getTotalEntries();
   }

   public synchronized long getTotalPersistentCurrentEntries() {
      return this.getStats().getTotalPersistentEntries();
   }

   public synchronized long getTotalTransientCurrentEntries() {
      return this.getStats().getTotalTransientEntries();
   }

   public synchronized double getAvgPercentTransient() {
      return this.getStats().getAvgPercentTransient();
   }

   public synchronized double getAvgPercentPersistent() {
      return this.getStats().getAvgPercentPersistent();
   }

   public synchronized double getAvgTimeout() {
      return this.getStats().getAvgTimout();
   }

   public synchronized double getMinEntryTimeout() {
      return this.getStats().getMinEntryTimeout();
   }

   public synchronized double getMaxEntryTimeout() {
      return this.getStats().getMaxEntryTimeout();
   }

   public synchronized double getAvgPerEntryMemorySize() {
      return this.getStats().getAvgPerEntryMemorySize();
   }

   public synchronized long getMaxEntryMemorySize() {
      return this.getStats().getMaxEntryMemorySize();
   }

   public synchronized long getMinEntryMemorySize() {
      return this.getStats().getMinEntryMemorySize();
   }

   public synchronized double getAvgPerEntryDiskSize() {
      return this.getStats().getAvgPerEntryDiskSize();
   }

   class AttributeDiff {
      String attributeName = null;
      String newValue = null;
      String oldValue = null;
      String type = null;

      AttributeDiff(String var2, String var3, String var4, String var5) {
         this.attributeName = var2;
         this.newValue = var3;
         this.oldValue = var4;
         this.type = var5;
      }
   }
}
