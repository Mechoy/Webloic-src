package weblogic.jms.module;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;

public class TargetListSave {
   private List savedTargets = null;

   public TargetListSave(List var1) {
      if (var1 == null) {
         this.savedTargets = null;
      } else {
         this.savedTargets = new LinkedList();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            TargetMBean var3 = (TargetMBean)var2.next();
            this.savedTargets.add(new TargetSave(var3));
         }
      }

   }

   public List restoreTargets(DomainMBean var1) {
      LinkedList var2 = new LinkedList();
      if (this.savedTargets == null) {
         return var2;
      } else {
         Iterator var3 = this.savedTargets.iterator();

         while(var3.hasNext()) {
            TargetSave var4 = (TargetSave)var3.next();
            TargetMBean var5 = var4.restore(var1);
            if (var5 != null) {
               var2.add(var5);
            }
         }

         return var2;
      }
   }

   public int size() {
      return this.savedTargets == null ? 0 : this.savedTargets.size();
   }

   private static class TargetSave {
      private static final int JMS_SERVER = 0;
      private static final int WLS_SERVER = 1;
      private static final int CLUSTER = 2;
      private static final int SAF_AGENT = 3;
      private static final int MIGRATABLE_TARGET = 4;
      private String name;
      private int type;

      public TargetSave(TargetMBean var1) {
         this.name = var1.getName();
         if (var1 instanceof JMSServerMBean) {
            this.type = 0;
         } else if (var1 instanceof ServerMBean) {
            this.type = 1;
         } else if (var1 instanceof ClusterMBean) {
            this.type = 2;
         } else if (var1 instanceof SAFAgentMBean) {
            this.type = 3;
         } else {
            if (!(var1 instanceof MigratableTargetMBean)) {
               throw new AssertionError("Bad type: " + var1.getClass().getName());
            }

            this.type = 4;
         }

      }

      public TargetMBean restore(DomainMBean var1) {
         switch (this.type) {
            case 0:
               return var1.lookupJMSServer(this.name);
            case 1:
               return var1.lookupServer(this.name);
            case 2:
               return var1.lookupCluster(this.name);
            case 3:
               return var1.lookupSAFAgent(this.name);
            case 4:
               return var1.lookupMigratableTarget(this.name);
            default:
               throw new AssertionError("Cannot get here");
         }
      }
   }
}
