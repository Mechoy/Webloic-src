package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatelessClusteringMBeanImpl extends XMLElementMBeanDelegate implements StatelessClusteringMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_homeLoadAlgorithm = false;
   private String homeLoadAlgorithm;
   private boolean isSet_statelessBeanLoadAlgorithm = false;
   private String statelessBeanLoadAlgorithm;
   private boolean isSet_useServersideStubs = false;
   private boolean useServersideStubs = false;
   private boolean isSet_homeIsClusterable = false;
   private boolean homeIsClusterable = true;
   private boolean isSet_statelessBeanIsClusterable = false;
   private boolean statelessBeanIsClusterable = true;
   private boolean isSet_homeCallRouterClassName = false;
   private String homeCallRouterClassName;
   private boolean isSet_statelessBeanMethodsAreIdempotent = false;
   private boolean statelessBeanMethodsAreIdempotent = false;
   private boolean isSet_statelessBeanCallRouterClassName = false;
   private String statelessBeanCallRouterClassName;

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

   public String getStatelessBeanLoadAlgorithm() {
      return this.statelessBeanLoadAlgorithm;
   }

   public void setStatelessBeanLoadAlgorithm(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.statelessBeanLoadAlgorithm;
      this.statelessBeanLoadAlgorithm = var1;
      this.isSet_statelessBeanLoadAlgorithm = var1 != null;
      this.checkChange("statelessBeanLoadAlgorithm", var2, this.statelessBeanLoadAlgorithm);
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

   public boolean getStatelessBeanIsClusterable() {
      return this.statelessBeanIsClusterable;
   }

   public void setStatelessBeanIsClusterable(boolean var1) {
      boolean var2 = this.statelessBeanIsClusterable;
      this.statelessBeanIsClusterable = var1;
      this.isSet_statelessBeanIsClusterable = true;
      this.checkChange("statelessBeanIsClusterable", var2, this.statelessBeanIsClusterable);
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

   public boolean getStatelessBeanMethodsAreIdempotent() {
      return this.statelessBeanMethodsAreIdempotent;
   }

   public void setStatelessBeanMethodsAreIdempotent(boolean var1) {
      boolean var2 = this.statelessBeanMethodsAreIdempotent;
      this.statelessBeanMethodsAreIdempotent = var1;
      this.isSet_statelessBeanMethodsAreIdempotent = true;
      this.checkChange("statelessBeanMethodsAreIdempotent", var2, this.statelessBeanMethodsAreIdempotent);
   }

   public String getStatelessBeanCallRouterClassName() {
      return this.statelessBeanCallRouterClassName;
   }

   public void setStatelessBeanCallRouterClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.statelessBeanCallRouterClassName;
      this.statelessBeanCallRouterClassName = var1;
      this.isSet_statelessBeanCallRouterClassName = var1 != null;
      this.checkChange("statelessBeanCallRouterClassName", var2, this.statelessBeanCallRouterClassName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateless-clustering");
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

      if (this.isSet_statelessBeanIsClusterable || !this.getStatelessBeanIsClusterable()) {
         var2.append(ToXML.indent(var1 + 2)).append("<stateless-bean-is-clusterable>").append(ToXML.capitalize(Boolean.valueOf(this.getStatelessBeanIsClusterable()).toString())).append("</stateless-bean-is-clusterable>\n");
      }

      if (null != this.getStatelessBeanLoadAlgorithm()) {
         var2.append(ToXML.indent(var1 + 2)).append("<stateless-bean-load-algorithm>").append(this.getStatelessBeanLoadAlgorithm()).append("</stateless-bean-load-algorithm>\n");
      }

      if (null != this.getStatelessBeanCallRouterClassName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<stateless-bean-call-router-class-name>").append(this.getStatelessBeanCallRouterClassName()).append("</stateless-bean-call-router-class-name>\n");
      }

      if (this.isSet_statelessBeanMethodsAreIdempotent || this.getStatelessBeanMethodsAreIdempotent()) {
         var2.append(ToXML.indent(var1 + 2)).append("<stateless-bean-methods-are-idempotent>").append(ToXML.capitalize(Boolean.valueOf(this.getStatelessBeanMethodsAreIdempotent()).toString())).append("</stateless-bean-methods-are-idempotent>\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateless-clustering>\n");
      return var2.toString();
   }
}
