package weblogic.jms.forwarder.dd.internal;

import weblogic.jms.forwarder.DestinationName;
import weblogic.jms.forwarder.dd.DDInfo;
import weblogic.jms.forwarder.internal.DestinationNameImpl;

public class DDInfoImpl implements DDInfo {
   private DestinationName destinationName;
   private int ddType;
   private String applicationName;
   private String moduleName;
   private int ddLoadBalancingPolicy;
   private int forwardingPolicy;
   private int forwardDelay;
   private String pathServiceJNDIName;
   private String safExportPolicy;
   private String uuoRouting;

   public DDInfoImpl(String var1, String var2, int var3, String var4, String var5, int var6, int var7) {
      this(var1, var2, var3, var4, var5, var6, var7, -1, (String)null, (String)null, (String)null);
   }

   public DDInfoImpl(String var1, String var2, int var3, String var4, String var5, int var6, int var7, int var8, String var9, String var10, String var11) {
      this.destinationName = new DestinationNameImpl(var2, var1);
      this.ddType = var3;
      this.applicationName = var4;
      this.moduleName = var5;
      this.ddLoadBalancingPolicy = var6;
      this.forwardingPolicy = var7;
      this.forwardDelay = var8;
      this.pathServiceJNDIName = var9;
      this.safExportPolicy = var10;
      this.uuoRouting = var11;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DDInfoImpl)) {
         return false;
      } else {
         DDInfoImpl var2 = (DDInfoImpl)var1;
         if (this.destinationName != null) {
            if (!this.destinationName.equals(var2.destinationName)) {
               return false;
            }
         } else if (var2.destinationName != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      return this.destinationName != null ? this.destinationName.hashCode() : 0;
   }

   public DestinationName getDestinationName() {
      return this.destinationName;
   }

   public int getType() {
      return this.ddType;
   }

   public String getPathServiceJNDIName() {
      return this.pathServiceJNDIName;
   }

   public String getSAFExportPolicy() {
      return this.safExportPolicy;
   }

   public String getUnitOfOrderRouting() {
      return this.uuoRouting;
   }

   public int getLoadBalancingPolicy() {
      return this.ddLoadBalancingPolicy;
   }

   public int getForwardingPolicy() {
      return this.forwardingPolicy;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public int getForwardDelay() {
      return this.forwardDelay;
   }
}
