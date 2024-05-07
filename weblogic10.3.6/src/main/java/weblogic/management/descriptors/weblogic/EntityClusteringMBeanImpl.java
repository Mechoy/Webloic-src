package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EntityClusteringMBeanImpl extends XMLElementMBeanDelegate implements EntityClusteringMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_homeLoadAlgorithm = false;
   private String homeLoadAlgorithm;
   private boolean isSet_useServersideStubs = false;
   private boolean useServersideStubs = false;
   private boolean isSet_homeIsClusterable = false;
   private boolean homeIsClusterable = true;
   private boolean isSet_homeCallRouterClassName = false;
   private String homeCallRouterClassName;

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity-clustering");
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

      var2.append(ToXML.indent(var1)).append("</entity-clustering>\n");
      return var2.toString();
   }
}
