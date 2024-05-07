package weblogic.cluster.singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.deploy.event.DeploymentEvent;
import weblogic.deploy.event.DeploymentEventListener;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.j2ee.descriptor.wl.SingletonServiceBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.configuration.TargetMBean;

public class SingletonMonitorServiceTracker implements ClusterMembersChangeListener, DeploymentEventListener, BeanUpdateListener {
   private static final boolean DEBUG = false;
   private ClusterMBean cluster;
   private DomainMBean domain;
   private HashMap allSingletons;
   private LeaseManager manager;
   private ArrayList allServers;

   SingletonMonitorServiceTracker(LeaseManager var1) {
      this.manager = var1;
   }

   void initialize(ClusterMBean var1, DomainMBean var2) {
      this.cluster = var1;
      this.domain = var2;
      this.allServers = new ArrayList();
      this.populateServerList();
      this.allSingletons = new HashMap();
      this.addAppscopedSingletons();
      this.addStaticallyDeployedSingletons();
   }

   public synchronized Object get(Object var1) {
      return this.allSingletons.get(var1);
   }

   public synchronized void remove(Object var1) {
      this.allSingletons.remove(var1);
   }

   public synchronized void put(Object var1, Object var2) {
      this.allSingletons.put(var1, var2);
   }

   public synchronized Collection values() {
      return ((HashMap)this.allSingletons.clone()).values();
   }

   private synchronized void addStaticallyDeployedSingletons() {
      MigratableTargetMBean[] var1 = this.domain.getMigratableTargets();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!var1[var2].getMigrationPolicy().equals("manual") && this.cluster.equals(var1[var2].getCluster())) {
            this.allSingletons.put(var1[var2].getName(), new SingletonDataObject(var1[var2].getName()));
         }
      }

      SingletonServiceMBean[] var6 = this.domain.getSingletonServices();

      for(int var3 = 0; var3 < var6.length; ++var3) {
         if (var6[var3].getCluster() != null && var6[var3].getCluster().getName().equals(this.cluster.getName()) && !this.allSingletons.containsKey(var6[var3].getName())) {
            this.allSingletons.put(var6[var3].getName(), new SingletonDataObject(var6[var3].getName()));
         }
      }

      ServerMBean[] var7 = this.domain.getServers();

      for(int var4 = 0; var4 < var7.length; ++var4) {
         JTAMigratableTargetMBean var5 = var7[var4].getJTAMigratableTarget();
         if (var5 != null && var5.getMigrationPolicy().equals("failure-recovery") && this.cluster.equals(var5.getCluster())) {
            this.allSingletons.put(var5.getName(), new SingletonDataObject(var5.getName(), true));
         }
      }

   }

   private synchronized void addAppscopedSingletons() {
      AppDeploymentMBean[] var1 = this.domain.getAppDeployments();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getApplicationContext(var1[var2].getApplicationIdentifier());
         if (var3 != null) {
            WeblogicApplicationBean var4 = null;

            try {
               ApplicationDescriptor var5 = var3.getApplicationDescriptor();
               if (var5 == null) {
                  continue;
               }

               var4 = var5.getWeblogicApplicationDescriptor();
            } catch (IOException var9) {
               continue;
            } catch (XMLStreamException var10) {
               continue;
            }

            if (var4 != null) {
               SingletonServiceBean[] var11 = var4.getSingletonServices();
               if (var11 != null) {
                  String var6 = ApplicationVersionUtils.getVersionId(var1[var2].getApplicationIdentifier());

                  for(int var7 = 0; var7 < var11.length; ++var7) {
                     String var8 = SingletonServicesManager.Util.getAppscopedSingletonServiceName(var11[var7].getName(), var6);
                     if (!this.allSingletons.containsKey(var8)) {
                        this.allSingletons.put(var8, new SingletonDataObject(var8, var1[var2].getApplicationIdentifier(), this.getApplicationTargets(var1[var2])));
                     }
                  }
               }
            }
         }
      }

   }

   private synchronized void populateServerList() {
      ServerMBean[] var1 = this.domain.getServers();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].getCluster() != null && var1[var2].getCluster().getName().equals(this.cluster.getName())) {
            this.allServers.add(var1[var2]);
         }
      }

   }

   public List getServerList(String var1) {
      SingletonDataObject var2 = (SingletonDataObject)this.allSingletons.get(var1);
      return (List)(var2 != null && var2.isAppScopedSingleton() ? var2.getTargets() : this.allServers);
   }

   public void register(String var1) {
      this.allSingletons.put(var1, new SingletonDataObject(var1));
   }

   public void registerJTA(String var1) {
      this.allSingletons.put(var1, new SingletonDataObject(var1, true));
   }

   public void unregister(String var1) {
      this.allSingletons.remove(var1);
   }

   private List getApplicationTargets(AppDeploymentMBean var1) {
      TargetMBean[] var2 = var1.getTargets();
      List var3 = null;
      if (var2 != null && var2.length > 0) {
         if (var2[0].getName().equals(this.cluster.getName())) {
            ServerMBean[] var4 = this.cluster.getServers();
            var3 = Arrays.asList(var4);
         } else {
            var3 = Arrays.asList(var2);
         }
      }

      return var3;
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
   }

   public synchronized void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      BeanUpdateEvent.PropertyUpdate[] var2 = var1.getUpdateList();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Object var4;
         ConfigurationMBean var5;
         MigratableTargetMBean var6;
         SingletonServiceMBean var7;
         if (var2[var3].getUpdateType() == 2) {
            var4 = var2[var3].getAddedObject();
            if (var4 instanceof ServerMBean) {
               var5 = (ConfigurationMBean)var4;
               this.allServers.add(var5);
            } else if (var4 instanceof MigratableTargetMBean) {
               var6 = (MigratableTargetMBean)var4;
               if (!var6.getMigrationPolicy().equals("manual") && this.cluster.equals(var6.getCluster())) {
                  this.allSingletons.put(var6.getName(), new SingletonDataObject(var6.getName()));
               }
            } else if (var4 instanceof SingletonServiceMBean) {
               var7 = (SingletonServiceMBean)var4;
               if (this.cluster.equals(var7.getCluster()) && !this.allSingletons.containsKey(var7.getName())) {
                  this.allSingletons.put(var7.getName(), new SingletonDataObject(var7.getName()));
               }
            }
         } else if (var2[var3].getUpdateType() == 3) {
            var4 = var2[var3].getRemovedObject();
            if (var4 instanceof ServerMBean) {
               var5 = (ConfigurationMBean)var4;
               this.allServers.remove(var5);
            } else if (var4 instanceof MigratableTargetMBean) {
               var6 = (MigratableTargetMBean)var4;
               if (this.allSingletons.containsKey(var6.getName())) {
                  this.allSingletons.remove(var6.getName());
               }
            } else if (var4 instanceof SingletonServiceMBean) {
               var7 = (SingletonServiceMBean)var4;
               if (this.allSingletons.containsKey(var7.getName())) {
                  this.allSingletons.remove(var7.getName());
               }
            }
         }
      }

   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   public void applicationRedeployed(DeploymentEvent var1) {
   }

   public synchronized void applicationDeleted(DeploymentEvent var1) {
      AppDeploymentMBean var2 = var1.getAppDeployment();
      if (var2 != null) {
         Iterator var3 = this.values().iterator();

         while(var3.hasNext()) {
            SingletonDataObject var4 = (SingletonDataObject)var3.next();
            if (var4.isAppScopedSingleton() && var4.getAppName().equals(var2.getApplicationIdentifier())) {
               this.allSingletons.remove(var4.getName());
            }
         }

      }
   }

   public synchronized void applicationActivated(DeploymentEvent var1) {
      AppDeploymentMBean var2 = var1.getAppDeployment();
      if (var2 != null) {
         ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getApplicationContext(var2.getApplicationIdentifier());
         if (var3 != null) {
            WeblogicApplicationBean var4 = var3.getWLApplicationDD();
            if (var4 != null) {
               SingletonServiceBean[] var5 = var4.getSingletonServices();
               if (var5 != null && var5.length != 0) {
                  List var6 = this.getApplicationTargets(var2);
                  String var7 = ApplicationVersionUtils.getVersionId(var2.getApplicationIdentifier());

                  for(int var8 = 0; var8 < var5.length; ++var8) {
                     if (!this.allSingletons.containsKey(var5[var8].getName())) {
                        String var9 = SingletonServicesManager.Util.getAppscopedSingletonServiceName(var5[var8].getName(), var7);
                        this.allSingletons.put(var9, new SingletonDataObject(var9, var2.getApplicationIdentifier(), var6));
                     }
                  }

               }
            }
         }
      }
   }

   public synchronized void applicationDeployed(DeploymentEvent var1) {
      this.applicationActivated(var1);
   }

   private void p(Object var1) {
      System.out.println("<SingletonMonitorServiceTracker> " + var1.toString());
   }

   private void p(Object var1, Exception var2) {
      System.out.println("<SingletonMonitorServiceTracker> " + var1.toString() + " --- " + var2);
   }
}
