package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatefulSessionClusteringMBeanImpl extends XMLElementMBeanDelegate implements StatefulSessionClusteringMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_replicationType = false;
   private String replicationType = "None";
   private boolean isSet_homeLoadAlgorithm = false;
   private String homeLoadAlgorithm;
   private boolean isSet_useServersideStubs = false;
   private boolean useServersideStubs = false;
   private boolean isSet_homeIsClusterable = false;
   private boolean homeIsClusterable = true;
   private boolean isSet_homeCallRouterClassName = false;
   private String homeCallRouterClassName;
   private boolean isSet_passivateDuringReplication = false;
   private boolean passivateDuringReplication = true;

   public String getReplicationType() {
      return this.replicationType;
   }

   public void setReplicationType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.replicationType;
      this.replicationType = var1;
      this.isSet_replicationType = var1 != null;
      this.checkChange("replicationType", var2, this.replicationType);
   }

   public String getHomeLoadAlgorithm() {
      return this.homeLoadAlgorithm;
   }

   public void setHomeLoadAlgorithm(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.homeLoadAlgorithm;
      this.homeLoadAlgorithm = var1;
      this.isSet_homeLoadAlgorithm = var1 != null;
      this.checkChange("homeLoadAlgorithm", var2, this.homeLoadAlgorithm);
   }

   public boolean getUseServersideStubs() {
      return this.useServersideStubs;
   }

   public void setUseServersideStubs(boolean var1) {
      boolean var2 = this.useServersideStubs;
      this.useServersideStubs = var1;
      this.isSet_useServersideStubs = true;
      this.checkChange("useServersideStubs", var2, this.useServersideStubs);
   }

   public boolean getHomeIsClusterable() {
      return this.homeIsClusterable;
   }

   public void setHomeIsClusterable(boolean var1) {
      boolean var2 = this.homeIsClusterable;
      this.homeIsClusterable = var1;
      this.isSet_homeIsClusterable = true;
      this.checkChange("homeIsClusterable", var2, this.homeIsClusterable);
   }

   public String getHomeCallRouterClassName() {
      return this.homeCallRouterClassName;
   }

   public void setHomeCallRouterClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.homeCallRouterClassName;
      this.homeCallRouterClassName = var1;
      this.isSet_homeCallRouterClassName = var1 != null;
      this.checkChange("homeCallRouterClassName", var2, this.homeCallRouterClassName);
   }

   public boolean getPassivateDuringReplication() {
      return this.passivateDuringReplication;
   }

   public void setPassivateDuringReplication(boolean var1) {
      boolean var2 = this.passivateDuringReplication;
      this.passivateDuringReplication = var1;
      this.isSet_passivateDuringReplication = true;
      this.checkChange("passivateDuringReplication", var2, this.passivateDuringReplication);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateful-session-clustering");
      var2.append(">\n");
      if (this.isSet_homeIsClusterable || !this.getHomeIsClusterable()) {
         var2.append(ToXML.indent(var1 + 2)).append("<home-is-clusterable>").append(ToXML.capitalize(Boolean.valueOf(this.getHomeIsClusterable()).toString())).append("</home-is-clusterable>\n");
      }

      if (null != this.getHomeLoadAlgorithm()) {
         var2.append(ToXML.indent(var1 + 2)).append("<home-load-algorithm>").append(this.getHomeLoadAlgorithm()).append("</home-load-algorithm>\n");
      }

      if (null != this.getHomeCallRouterClassName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<home-call-router-class-name>").append(this.getHomeCallRouterClassName()).append("</home-call-router-class-name>\n");
      }

      if (this.isSet_useServersideStubs || this.getUseServersideStubs()) {
         var2.append(ToXML.indent(var1 + 2)).append("<use-serverside-stubs>").append(ToXML.capitalize(Boolean.valueOf(this.getUseServersideStubs()).toString())).append("</use-serverside-stubs>\n");
      }

      if ((this.isSet_replicationType || !"None".equals(this.getReplicationType())) && null != this.getReplicationType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<replication-type>").append(this.getReplicationType()).append("</replication-type>\n");
      }

      if (this.isSet_passivateDuringReplication || !this.getPassivateDuringReplication()) {
         var2.append(ToXML.indent(var1 + 2)).append("<passivate-during-replication>").append(ToXML.capitalize(Boolean.valueOf(this.getPassivateDuringReplication()).toString())).append("</passivate-during-replication>\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateful-session-clustering>\n");
      return var2.toString();
   }
}
