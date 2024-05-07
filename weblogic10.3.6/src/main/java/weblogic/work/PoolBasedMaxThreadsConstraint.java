package weblogic.work;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.Iterator;
import weblogic.application.ApplicationAccess;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorDiff;
import weblogic.descriptor.DescriptorUpdateEvent;
import weblogic.descriptor.DescriptorUpdateListener;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolParamsBean;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.MaxThreadsConstraintBean;
import weblogic.j2ee.descriptor.wl.SizeParamsBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.jdbc.module.JDBCModule;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JoltConnectionPoolMBean;
import weblogic.management.configuration.MaxThreadsConstraintMBean;
import weblogic.management.configuration.WLECConnectionPoolMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

final class PoolBasedMaxThreadsConstraint extends MaxThreadsConstraint implements PropertyChangeListener, BeanUpdateListener, DescriptorUpdateListener {
   private static final DebugCategory debugMTC = Debug.getCategory("weblogic.maxthreadsconstraint");
   private static final String WLEC_POOL_TYPE = "WLECConnectionPoolConfig";
   private static final String JOLT_POOL_TYPE = "JoltConnectionPoolConfig";
   private static final String CONNECTOR_POOL_TYPE = "ConnectorComponentConfig";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   PoolBasedMaxThreadsConstraint(String var1, String var2) throws ManagementException {
      super(var1);
      this.setCountUsingBean(var2);
   }

   PoolBasedMaxThreadsConstraint(String var1, String var2, WeblogicApplicationBean var3) throws DeploymentException {
      super(var1);
      JDBCConnectionPoolBean var4 = this.getAppScopedBean(var3, var2);
      if (var4 != null) {
         SizeParamsBean var5 = var4.getPoolParams().getSizeParams();
         this.setCount(var5.getMaxCapacity());
         this.registerForUpdates((DescriptorBean)var5);
      } else {
         this.setCountUsingBean(var2);
      }

   }

   private JDBCConnectionPoolBean getAppScopedBean(WeblogicApplicationBean var1, String var2) {
      debug("looking up data source '" + var2 + ", within " + var1);
      if (var1 != null && var2 != null) {
         JDBCConnectionPoolBean[] var3 = var1.getJDBCConnectionPools();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               debug("got app scoped pool - " + var3[var4].getDataSourceJNDIName());
               if (var2.equalsIgnoreCase(var3[var4].getDataSourceJNDIName())) {
                  debug("found a match for app scoped pool - " + var2);
                  return var3[var4];
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void setCountUsingBean(String var1) throws DeploymentException {
      JDBCSystemResourceMBean var2 = this.getJDBCSystemResource(var1);
      if (var2 != null) {
         this.setCount(var2.getJDBCResource().getJDBCConnectionPoolParams().getMaxCapacity());
         var2.getResource().getDescriptor().addUpdateListener(this);
      } else {
         JDBCConnectionPoolParamsBean var3 = this.getJDBCConnectionPoolParams(var1);
         if (var3 != null) {
            this.setCount(var3.getMaxCapacity());
            this.registerForUpdates((DescriptorBean)var3);
         } else {
            ConfigurationMBean var4 = this.getPoolMBean("ConnectorComponentConfig", var1);
            if (var4 == null) {
               var4 = this.getPoolMBean("WLECConnectionPoolConfig", var1);
            }

            if (var4 == null) {
               var4 = this.getPoolMBean("JoltConnectionPoolConfig", var1);
            }

            if (var4 == null) {
               throw new DeploymentException("Unable to lookup pool '" + var1 + "'. Please check if the pool name is valid and points to a valid " + "data source");
            } else {
               var4.addPropertyChangeListener(this);
               if (var4 instanceof WLECConnectionPoolMBean) {
                  this.setCount(((WLECConnectionPoolMBean)var4).getMaximumPoolSize());
               } else if (var4 instanceof JoltConnectionPoolMBean) {
                  this.setCount(((JoltConnectionPoolMBean)var4).getMaximumPoolSize());
               } else if (var4 instanceof ConnectorComponentMBean) {
                  this.setCount(((ConnectorComponentMBean)var4).getMaxCapacity());
               }

            }
         }
      }
   }

   private JDBCSystemResourceMBean getJDBCSystemResource(String var1) {
      debug("looking up connection pool - " + var1);
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (debugMTC.isEnabled()) {
         dumpAllJDBCResources(var2);
      }

      JDBCSystemResourceMBean var3 = var2.lookupJDBCSystemResource(var1);
      debug("found JDBCSystemResource with name " + var1 + " - " + var3);
      return var3;
   }

   private JDBCConnectionPoolParamsBean getJDBCConnectionPoolParams(String var1) {
      JDBCDataSourceBean var2 = getJDBCDataSourceBean(var1);
      debug("found jdbc connection pool using JDBCUtil - " + var2 + " for name --- " + var1);
      return var2 != null ? var2.getJDBCConnectionPoolParams() : null;
   }

   private static JDBCDataSourceBean getJDBCDataSourceBean(String var0) {
      JDBCDataSourceBean[] var1 = null;
      var1 = JDBCModule.getJDBCDataSourceBean(var0);
      return var1 != null ? var1[0] : null;
   }

   private ConfigurationMBean getPoolMBean(String var1, String var2) {
      DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      String var4 = var1.intern();
      if (var4 == "WLECConnectionPoolConfig") {
         debug("looking up WLEC POOL with name - " + var2);
         return var3.lookupWLECConnectionPool(var2);
      } else if (var4 == "JoltConnectionPoolConfig") {
         debug("looking up JOLT POOL with name - " + var2);
         return var3.lookupJoltConnectionPool(var2);
      } else if (var4 == "ConnectorComponentConfig") {
         String var5 = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
         if (var5 == null) {
            return null;
         } else {
            debug("looking up CONNECTOR POOL with name " + var2 + " in application " + var5);
            ApplicationMBean var6 = var3.lookupApplication(var5);
            return var6 == null ? null : var6.lookupConnectorComponent(var2);
         }
      } else {
         return null;
      }
   }

   private void registerForUpdates(DescriptorBean var1) {
      var1.addBeanUpdateListener(this);
   }

   public void prepareUpdate(DescriptorUpdateEvent var1) {
   }

   public void activateUpdate(DescriptorUpdateEvent var1) {
      DescriptorDiff var2 = var1.getDiff();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         BeanUpdateEvent var4 = (BeanUpdateEvent)var3.next();
         if (var4.getSource() instanceof JDBCConnectionPoolParamsBean) {
            this.setCount(((JDBCConnectionPoolParamsBean)var4.getSource()).getMaxCapacity());
            debug("Dynamic update of PoolBasedMaxThreadsConstraint " + this.getName() + " to count " + this.getCount());
            break;
         }

         if (var4.getSource() instanceof SizeParamsBean) {
            this.setCount(((SizeParamsBean)var4.getSource()).getMaxCapacity());
            debug("Dynamic update of PoolBasedMaxThreadsConstraint " + this.getName() + " to count " + this.getCount());
            break;
         }
      }

   }

   public void rollbackUpdate(DescriptorUpdateEvent var1) {
   }

   public final void prepareUpdate(BeanUpdateEvent var1) {
   }

   public final void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof MaxThreadsConstraintMBean) {
         this.setCountInternal(((MaxThreadsConstraintMBean)var2).getCount());
      } else if (var2 instanceof MaxThreadsConstraintBean) {
         this.setCountInternal(((MaxThreadsConstraintBean)var2).getCount());
      }

   }

   public final void rollbackUpdate(BeanUpdateEvent var1) {
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1 != null) {
         String var2 = var1.getPropertyName();
         if (var2.equals("MaxCapacity") || var2.equals("MaximumPoolSize")) {
            int var3 = (Integer)var1.getNewValue();
            this.setCount(var3);
         }
      }
   }

   private static void dumpAllJDBCResources(DomainMBean var0) {
      if (debugMTC.isEnabled()) {
         JDBCSystemResourceMBean[] var1 = var0.getJDBCSystemResources();
         if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               debug("found JDBCSystemResource - " + var1[var2].getName());
            }
         } else {
            debug("There are no JDBCSystemResources in the configuration !");
         }
      }

   }

   private static void debug(String var0) {
      if (debugMTC.isEnabled()) {
         WorkManagerLogger.logDebug("<WM_MaxTC>" + var0);
      }

   }
}
