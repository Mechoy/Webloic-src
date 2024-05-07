package weblogic.cluster.leasing.databaseless;

import java.util.ArrayList;
import java.util.List;
import weblogic.health.HealthFeedback;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.work.WorkManagerFactory;

public final class ClusterState implements HealthFeedback {
   static final String DISCOVERY = "discovery";
   static final String FORMATION = "formation";
   static final String FORMATION_LEADER = "formation_leader";
   static final String STABLE = "stable";
   public static final String STABLE_LEADER = "stable_leader";
   static final String FAILED = "failed";
   private String currentState;
   private final List listeners;
   private HealthState healthState;

   public synchronized boolean setState(String var1) {
      return this.setState(var1, (String)null);
   }

   public synchronized boolean setState(String var1, String var2) {
      String var3 = var1.intern();
      this.setHealthState(var1, var2);
      if (var3 != "discovery" && var3 != "failed") {
         if (this.currentState == null) {
            return var3 != "formation" && var3 != "stable" ? false : this.setInternalState(var3);
         } else if (this.currentState == "discovery") {
            return var3 != "formation" && var3 != "stable" && var3 != "formation_leader" ? false : this.setInternalState(var3);
         } else if (this.currentState == "formation_leader") {
            return var3 == "stable_leader" ? this.setInternalState(var3) : false;
         } else if (this.currentState == "formation") {
            return var3 == "stable" ? this.setInternalState(var3) : false;
         } else {
            throw new AssertionError("Invalid state transition from " + this.currentState + " to " + var1);
         }
      } else {
         return this.setInternalState(var3);
      }
   }

   public HealthState getHealthState() {
      return this.healthState;
   }

   private ClusterState() {
      this.listeners = new ArrayList();
      this.healthState = new HealthState(0);
      HealthMonitorService.register("DatabaseLessLeasing", this, true);
   }

   private void setHealthState(String var1, String var2) {
      if (var1 != null) {
         if (var1 == "failed") {
            this.healthState = new HealthState(3, var2);
         } else {
            if (this.healthState.getState() == 3 && var1 != "failed") {
               this.healthState = new HealthState(0);
            }

         }
      }
   }

   private boolean setInternalState(final String var1) {
      final String var2 = this.currentState;
      this.currentState = var1;
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            synchronized(ClusterState.this.listeners) {
               for(int var2x = 0; var2x < ClusterState.this.listeners.size(); ++var2x) {
                  ((ClusterStateChangeListener)ClusterState.this.listeners.get(var2x)).stateChanged(var2, var1);
               }

            }
         }
      });
      return true;
   }

   void addStateChangeListener(ClusterStateChangeListener var1) {
      synchronized(this.listeners) {
         this.listeners.add(var1);
      }
   }

   void removeStateChangeListener(ClusterStateChangeListener var1) {
      synchronized(this.listeners) {
         this.listeners.remove(var1);
      }
   }

   public synchronized String getErrorMessage(String var1) {
      return "unable to transition from " + this.currentState + " to " + var1;
   }

   public static ClusterState getInstance() {
      return ClusterState.Factory.THE_ONE;
   }

   synchronized String getState() {
      return this.currentState;
   }

   // $FF: synthetic method
   ClusterState(Object var1) {
      this();
   }

   private static final class Factory {
      static final ClusterState THE_ONE = new ClusterState();
   }
}
