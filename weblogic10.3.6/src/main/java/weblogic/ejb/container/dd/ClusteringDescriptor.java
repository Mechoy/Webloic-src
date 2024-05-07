package weblogic.ejb.container.dd;

import java.util.Collections;
import java.util.Iterator;

public final class ClusteringDescriptor extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   private boolean homeIsClusterable = DDDefaults.getHomeIsClusterable();
   private String homeLoadAlgorithm;
   private String homeCallRouterClassName;
   private boolean statelessBeanIsClusterable = DDDefaults.getStatelessBeanIsClusterable();
   private boolean useServersideStubs = DDDefaults.getUseServersideStubs();
   private boolean passivateDuringReplication = DDDefaults.getPassivateDuringReplication();
   private String statelessBeanLoadAlgorithm;
   private String statelessBeanCallRouterClassName;

   public ClusteringDescriptor() {
      super((String)null);
   }

   public void setHomeIsClusterable(boolean var1) {
      if (debug) {
         System.err.println("setHomeIsClusterable(" + var1 + ")");
      }

      this.homeIsClusterable = var1;
   }

   public boolean getHomeIsClusterable() {
      return this.homeIsClusterable;
   }

   public void setHomeLoadAlgorithm(String var1) {
      if (debug) {
         System.err.println("setHomeLoadAlgorithm(" + var1 + ")");
      }

      this.homeLoadAlgorithm = var1;
   }

   public String getHomeLoadAlgorithm() {
      return this.homeLoadAlgorithm;
   }

   public void setHomeCallRouterClassName(String var1) {
      if (debug) {
         System.err.println("setHomeCallRouterClassName(" + var1 + ")");
      }

      this.homeCallRouterClassName = var1;
   }

   public String getHomeCallRouterClassName() {
      return this.homeCallRouterClassName;
   }

   public void setStatelessBeanIsClusterable(boolean var1) {
      this.statelessBeanIsClusterable = var1;
   }

   public boolean getStatelessBeanIsClusterable() {
      return this.statelessBeanIsClusterable;
   }

   public void setStatelessBeanLoadAlgorithm(String var1) {
      if (debug) {
         System.err.println("setStatelessBeanLoadAlgorithm(" + var1 + ")");
      }

      this.statelessBeanLoadAlgorithm = var1;
   }

   public String getStatelessBeanLoadAlgorithm() {
      return this.statelessBeanLoadAlgorithm;
   }

   public void setStatelessBeanCallRouterClassName(String var1) {
      if (debug) {
         System.err.println("setStatelessBeanCallRouterClassName(" + var1 + ")");
      }

      this.statelessBeanCallRouterClassName = var1;
   }

   public String getStatelessBeanCallRouterClassName() {
      return this.statelessBeanCallRouterClassName;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public boolean getUseServersideStubs() {
      return this.useServersideStubs;
   }

   public void setUseServersideStubs(boolean var1) {
      this.useServersideStubs = var1;
   }

   public boolean getPassivateDuringReplication() {
      return this.passivateDuringReplication;
   }

   public void setPassivateDuringReplication(boolean var1) {
      this.passivateDuringReplication = var1;
   }
}
