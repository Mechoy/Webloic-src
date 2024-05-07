package weblogic.management.deploy.internal;

import java.io.Serializable;
import weblogic.utils.Debug;

/** @deprecated */
public class ComponentTarget implements Serializable {
   private static final long serialVersionUID = 3862450250430200114L;
   public static final int COMPONENT_TARGET_SERVER = 1;
   public static final int COMPONENT_TARGET_CLUSTER = 2;
   public static final int COMPONENT_TARGET_VIRTUALHOST = 3;
   private String componentTarget = null;
   private String clusterTarget = null;
   private String physicalTarget = null;
   private int targetType;
   private String signature = null;
   private int hashCodeValue;

   public ComponentTarget(String var1, String var2, int var3) {
      this.componentTarget = var1;
      this.physicalTarget = var2;
      this.targetType = var3;
      this.signature = this.computeSignature();
      this.hashCodeValue = this.signature.hashCode();
   }

   public ComponentTarget(String var1, String var2, String var3, int var4) {
      if (var4 == 3) {
         this.clusterTarget = var2;
      } else {
         Debug.assertion(false, "ClusterTarget can be set only for VirtualHost");
      }

      this.componentTarget = var1;
      this.physicalTarget = var3;
      this.targetType = var4;
      this.signature = this.computeSignature();
      this.hashCodeValue = this.signature.hashCode();
   }

   public String getComponentTarget() {
      return this.componentTarget;
   }

   public String getClusterTarget() {
      return this.clusterTarget;
   }

   public String getPhysicalTarget() {
      return this.physicalTarget;
   }

   public boolean isVirtualHostClustered() {
      return this.clusterTarget != null;
   }

   public int getTargetType() {
      return this.targetType;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == this) {
         var2 = true;
      } else if (var1 instanceof ComponentTarget) {
         ComponentTarget var3 = (ComponentTarget)var1;
         if (var3.toString().equals(this.signature)) {
            var2 = true;
         }
      }

      return var2;
   }

   public String toString() {
      return this.signature;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }

   private String computeSignature() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ComponentTarget:");
      var1.append(this.componentTarget);
      var1.append(":");
      if (this.clusterTarget != null) {
         var1.append(this.getTargetTypeString(2));
         var1.append(this.clusterTarget);
         var1.append(":");
      }

      var1.append(this.getTargetTypeString(1));
      var1.append(this.physicalTarget);
      return var1.toString();
   }

   private String getTargetTypeString(int var1) {
      switch (var1) {
         case 1:
            return "Server:";
         case 2:
            return "Cluster:";
         case 3:
            return "VirtualHost:";
         default:
            Debug.assertion(false, "Illegal TargetType");
            return null;
      }
   }
}
