package weblogic.management.deploy.status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.management.TargetAvailabilityStatus;
import weblogic.management.deploy.internal.ComponentTarget;
import weblogic.utils.Debug;

/** @deprecated */
public abstract class BaseTargetAvailabilityStatus implements Serializable, TargetAvailabilityStatus {
   private static final long serialVersionUID = 346425021436708210L;
   protected String componentTargetName;
   protected int componentTargetType;
   protected boolean isStaged = false;
   protected HashSet availableServers = null;
   protected HashMap unavailableServersAvailabilityMap = null;

   public BaseTargetAvailabilityStatus(String var1, int var2, boolean var3) {
      this.componentTargetName = var1;
      this.componentTargetType = var2;
      this.isStaged = var3;
      this.availableServers = new HashSet();
      this.unavailableServersAvailabilityMap = new HashMap();
   }

   public String getTargetName() {
      return this.componentTargetName;
   }

   public int getTargetType() {
      return this.componentTargetType;
   }

   public int getDeploymentStatus() {
      byte var1 = 0;
      if (this.unavailableServersAvailabilityMap.size() == 0 && this.availableServers.size() > 0) {
         var1 = 1;
      } else if (this.unavailableServersAvailabilityMap.size() > 0 && this.availableServers.size() > 0) {
         var1 = 2;
      }

      return var1;
   }

   public int getAvailabilityStatus() {
      Debug.assertion(false, "This is not a valid call");
      return -1;
   }

   public Set getServersAvailabilityStatus() {
      Iterator var1 = this.availableServers.iterator();
      HashSet var2 = new HashSet();

      while(var1.hasNext()) {
         String var3 = (String)var1.next();
         ServerTargetAvailabilityStatus var4 = new ServerTargetAvailabilityStatus(var3, this.isStaged, true, 1);
         var2.add(var4);
      }

      Iterator var7 = this.unavailableServersAvailabilityMap.keySet().iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         int var5 = (Integer)this.unavailableServersAvailabilityMap.get(var8);
         ServerTargetAvailabilityStatus var6 = new ServerTargetAvailabilityStatus(var8, this.isStaged, false, var5);
         var2.add(var6);
      }

      return var2;
   }

   public Set getClustersAvailabilityStatus() {
      Debug.assertion(false, "This is not a valid call");
      return null;
   }

   public void updateAvailabilityStatus(ComponentTarget var1) {
      String var2 = var1.getPhysicalTarget();
      if (this.unavailableServersAvailabilityMap.keySet().contains(var2)) {
         this.unavailableServersAvailabilityMap.remove(var2);
         this.availableServers.add(var2);
      } else {
         String var3 = var1.getComponentTarget() + " not targeted to " + var2;
         Debug.assertion(false, var3);
      }

   }

   public void updateUnavailabilityStatus(List var1) {
      if (this.isStaged) {
         Iterator var2 = this.unavailableServersAvailabilityMap.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (!var1.contains(var3)) {
               this.unavailableServersAvailabilityMap.put(var3, new Integer(3));
            }
         }
      } else {
         Debug.assertion(false, "updateUnavailabilityStatus applicable only for staged applications.");
      }

   }
}
