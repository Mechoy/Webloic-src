package weblogic.deploy.api.spi.deploy.mbeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.shared.WebLogicTargetType;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.deploy.TargetImpl;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;

public class TargetCache extends MBeanCache {
   private static final boolean ddebug = Debug.isDebug("internal");
   private transient boolean loadedAllTargets = false;
   private transient List targets = null;

   public TargetCache(DomainMBean var1, WebLogicDeploymentManager var2) {
      super(var2);
      this.currDomain = var1;
      this.listenType = new String[]{"Servers", "Clusters", "VirtualHosts"};
      this.addNotificationListener();
   }

   public synchronized ConfigurationMBean[] getTypedMBeans() {
      Object var1 = this.currDomain.getTargets();
      if (var1 == null) {
         var1 = new ConfigurationMBean[0];
      }

      return (ConfigurationMBean[])var1;
   }

   public synchronized List getTargets() throws ServerConnectionException {
      return this.getTargets(true);
   }

   public synchronized TargetImpl getTarget(String var1) throws ServerConnectionException {
      Iterator var2 = this.getTargets(false).iterator();

      TargetImpl var3;
      do {
         if (!var2.hasNext()) {
            TargetMBean var5 = this.currDomain.lookupTarget(var1);
            if (var5 == null) {
               return null;
            }

            TargetImpl var4 = new TargetImpl(var1, this.getTypeForTarget(var5), this.getDM());
            this.targets.add(var4);
            return var4;
         }

         var3 = (TargetImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public List getTargets(WebLogicTargetType var1) throws ServerConnectionException {
      Iterator var2 = this.getTargets().iterator();
      ArrayList var3 = new ArrayList();

      while(var2.hasNext()) {
         WebLogicTargetType var4 = (WebLogicTargetType)var2.next();
         if (var4.getValue() == var1.getValue()) {
            var3.add(var4);
         }
      }

      return var3;
   }

   public synchronized void reset() {
      super.reset();
      this.loadedAllTargets = false;
      this.targets = null;
   }

   private WebLogicTargetType getTypeForTarget(TargetMBean var1) {
      if (var1 instanceof ServerMBean) {
         return WebLogicTargetType.SERVER;
      } else if (var1 instanceof ClusterMBean) {
         return WebLogicTargetType.CLUSTER;
      } else if (var1 instanceof VirtualHostMBean) {
         return WebLogicTargetType.VIRTUALHOST;
      } else if (var1 instanceof JMSServerMBean) {
         return WebLogicTargetType.JMSSERVER;
      } else {
         return var1 instanceof SAFAgentMBean ? WebLogicTargetType.SAFAGENT : null;
      }
   }

   private synchronized List getTargets(boolean var1) throws ServerConnectionException {
      if (this.isStale()) {
         this.reset();
      }

      if (!this.loadedAllTargets && var1) {
         Iterator var2 = this.getMBeans().iterator();
         this.targets = new ArrayList();

         while(var2.hasNext()) {
            TargetMBean var3 = (TargetMBean)var2.next();
            WebLogicTargetType var4 = this.getTypeForTarget(var3);
            if (var4 != null) {
               this.targets.add(new TargetImpl(var3.getObjectName().getName(), var4, this.getDM()));
            }
         }

         this.loadedAllTargets = true;
      }

      if (this.targets == null) {
         this.targets = new ArrayList();
      }

      return this.targets;
   }

   public void dumpTargets() {
      Iterator var1 = this.getTargets().iterator();

      while(var1.hasNext()) {
         TargetImpl var2 = (TargetImpl)var1.next();
         Debug.say(var2.toString());
      }

   }
}
