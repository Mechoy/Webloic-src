package weblogic.cluster.singleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SimpleLeasingBasis implements LeasingBasis {
   private final boolean DEBUG;
   private final Map leaseTable;

   public SimpleLeasingBasis() {
      this(new HashMap());
   }

   public SimpleLeasingBasis(Map var1) {
      this.DEBUG = false;
      this.leaseTable = var1;
   }

   protected Map getLeaseTable() {
      return this.leaseTable;
   }

   public synchronized boolean acquire(String var1, String var2, int var3) {
      LeaseEntry var4 = (LeaseEntry)this.leaseTable.get(var1);
      if (var4 != null && var4.timestamp + (long)var4.leaseTimeout >= System.currentTimeMillis()) {
         if (var2.equalsIgnoreCase(var4.owner)) {
            var4.leaseTimeout = var3;
            return true;
         } else {
            return false;
         }
      } else {
         this.leaseTable.put(var1, new LeaseEntry(var2, var1, var3));
         return true;
      }
   }

   public synchronized void release(String var1, String var2) throws IOException {
      LeaseEntry var3 = (LeaseEntry)this.leaseTable.get(var1);
      if (var3 != null && var3.owner.equals(var2)) {
         this.leaseTable.remove(var1);
      } else {
         throw new IOException("Lease \"" + var1 + "\" is not currently owned");
      }
   }

   public synchronized String findOwner(String var1) {
      LeaseEntry var2 = (LeaseEntry)this.leaseTable.get(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.timestamp + (long)var2.leaseTimeout < System.currentTimeMillis() ? null : var2.owner.toString();
      }
   }

   public synchronized String findPreviousOwner(String var1) {
      LeaseEntry var2 = (LeaseEntry)this.leaseTable.get(var1);
      return var2 == null ? null : var2.owner;
   }

   public synchronized int renewAllLeases(int var1, String var2) throws MissedHeartbeatException {
      HashMap var3 = new HashMap();
      Iterator var4 = this.leaseTable.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         LeaseEntry var6 = (LeaseEntry)var5.getValue();
         if (var6.owner.equals(var2)) {
            var6.timestamp = System.currentTimeMillis();
            var6.leaseTimeout = var1;
            var3.put(var5.getKey(), var6);
         }
      }

      this.leaseTable.putAll(var3);
      return var3.size();
   }

   public int renewLeases(String var1, Set var2, int var3) throws MissedHeartbeatException {
      HashMap var4 = new HashMap();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         LeaseEntry var7 = (LeaseEntry)this.leaseTable.get(var6);
         if (var7 != null && var7.owner.equals(var1)) {
            var7.timestamp = System.currentTimeMillis();
            var7.leaseTimeout = var3;
            var4.put(var6, var7);
         }
      }

      this.leaseTable.putAll(var4);
      return var4.size();
   }

   public synchronized String[] findExpiredLeases(int var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.leaseTable.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         LeaseEntry var5 = (LeaseEntry)var4.getValue();
         if (var5.timestamp + (long)var5.leaseTimeout + (long)var1 < System.currentTimeMillis()) {
            var2.add(var5.getLeaseName());
         }
      }

      String[] var6 = new String[var2.size()];
      var2.toArray(var6);
      return var6;
   }

   private static final void p(String var0) {
      System.out.println("<SimpleLeasingBasis>: " + var0);
   }

   public static final class LeaseEntry implements Serializable {
      private static final long serialVersionUID = 2765581341661213160L;
      private long timestamp = System.currentTimeMillis();
      private final String owner;
      private final String leaseName;
      private int leaseTimeout;

      public LeaseEntry(String var1, String var2, int var3) {
         this.owner = var1;
         this.leaseName = var2;
         this.leaseTimeout = var3;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof LeaseEntry)) {
            return false;
         } else {
            return ((LeaseEntry)var1).owner.equals(this.owner) && ((LeaseEntry)var1).leaseName.equals(this.leaseName);
         }
      }

      public int hashCode() {
         return this.leaseName.hashCode() ^ this.owner.hashCode();
      }

      public Object getLeaseName() {
         return this.leaseName;
      }

      public void setTimestamp(long var1) {
         this.timestamp = var1;
      }

      public String toString() {
         return "[LeaseEntry owner " + this.owner + ", lease name " + this.leaseName + ", timestamp " + this.timestamp + "]";
      }
   }
}
