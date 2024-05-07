package weblogic.deploy.internal;

import java.util.Comparator;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.CacheMBean;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.management.configuration.CustomResourceMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.StartupClassMBean;
import weblogic.management.configuration.WLDFSystemResourceMBean;
import weblogic.management.deploy.internal.DeploymentManagerLogger;

public class DeploymentType implements Comparator {
   private final String name;
   private final Class cls;
   public static final DeploymentType CUSTOM_SYS_RES = new DeploymentType("Custom", CustomResourceMBean.class);
   public static final DeploymentType JDBC_SYS_RES = new DeploymentType("JDBC", JDBCSystemResourceMBean.class) {
      public int compare(Object var1, Object var2) {
         this.ensureType(var1, var2);
         JDBCSystemResourceMBean var3 = (JDBCSystemResourceMBean)var1;
         JDBCSystemResourceMBean var4 = (JDBCSystemResourceMBean)var2;
         boolean var5 = this.isMultiDS(var3);
         boolean var6 = this.isMultiDS(var4);
         if (!var5 && var6) {
            return -1;
         } else {
            return var5 && !var6 ? 1 : this.compare(var3, var4);
         }
      }

      private boolean isMultiDS(JDBCSystemResourceMBean var1) {
         try {
            return JDBCMBeanConverter.getLegacyType(var1.getJDBCResource()) == 0 && var1.getJDBCResource().getJDBCDataSourceParams().getDataSourceList() != null;
         } catch (NullPointerException var3) {
            return false;
         }
      }
   };
   public static final DeploymentType JMS_SYS_RES = new DeploymentType("JMS", JMSSystemResourceMBean.class);
   public static final DeploymentType WLDF_SYS_RES = new DeploymentType("WLDF", WLDFSystemResourceMBean.class);
   public static final DeploymentType COHERENCE_CLUSTER_SYS_RES = new DeploymentType("CoherenceCluster", CoherenceClusterSystemResourceMBean.class);
   public static final DeploymentType LIBRARY = new DeploymentType("Library", LibraryMBean.class);
   public static final DeploymentType INTERNAL_APP = new DeploymentType("Internal", AppDeploymentMBean.class) {
      public boolean isInstance(Object var1) {
         return super.isInstance(var1) && ((AppDeploymentMBean)var1).isInternalApp();
      }
   };
   public static final DeploymentType DEFAULT_APP = new DeploymentType("Default", AppDeploymentMBean.class) {
      public boolean isInstance(Object var1) {
         return super.isInstance(var1) && !((AppDeploymentMBean)var1).isInternalApp() && !(var1 instanceof LibraryMBean);
      }
   };
   public static final Object PSEUDO_DEPLOYMENT_HANDLER_MBEAN = new String("PseudoDeploymentHandlerMBean");
   public static final DeploymentType DEPLOYMENT_HANDLER = new DeploymentType("DeploymentHandler", DeploymentMBean.class) {
      public boolean isInstance(Object var1) {
         return var1 == PSEUDO_DEPLOYMENT_HANDLER_MBEAN ? true : super.isInstance(var1);
      }

      public int compare(Object var1, Object var2) {
         this.ensureType(var1, var2);
         DeploymentMBean var3 = (DeploymentMBean)var1;
         DeploymentMBean var4 = (DeploymentMBean)var2;
         int var5 = var3.getDeploymentOrder();
         int var6 = var4.getDeploymentOrder();
         if (var5 < var6) {
            return -1;
         } else {
            return var6 < var5 ? 1 : this.defaultCompare(var3, var4);
         }
      }
   };
   public static final Object PSEUDO_RESOURCE_DEPENDENT_DEP_HANDLER_MBEAN = new String("PseudoResourceDependentDeploymentHandlerMBean");
   public static final DeploymentType RESOURCE_DEPENDENT_DEPLOYMENT_HANDLER = new DeploymentType("ResourceDependentDeploymentHandler", DeploymentMBean.class) {
      public boolean isInstance(Object var1) {
         return var1 == PSEUDO_RESOURCE_DEPENDENT_DEP_HANDLER_MBEAN;
      }
   };
   public static final Object PSEUDO_STARTUP_CLASS_MBEAN = new String("PseudoStartupClassMBean");
   public static final DeploymentType STARTUP_CLASS = new DeploymentType("StartupClass", StartupClassMBean.class) {
      public boolean isInstance(Object var1) {
         return var1 == PSEUDO_STARTUP_CLASS_MBEAN ? true : super.isInstance(var1);
      }

      public int compare(Object var1, Object var2) {
         return DEPLOYMENT_HANDLER.compare(var1, var2);
      }
   };
   public static final DeploymentType CACHE = new DeploymentType("Cache", CacheMBean.class);

   private DeploymentType(String var1, Class var2) {
      this.name = var1;
      this.cls = var2;
   }

   public String toString() {
      return this.name;
   }

   public boolean isInstance(Object var1) {
      return this.cls.isInstance(var1);
   }

   public Comparator getComparator() {
      return this;
   }

   public int compare(Object var1, Object var2) {
      this.ensureType(var1, var2);
      return var1 instanceof BasicDeploymentMBean ? this.compare((BasicDeploymentMBean)var1, (BasicDeploymentMBean)var2) : this.defaultCompare(var1, var2);
   }

   protected void ensureType(Object var1, Object var2) {
      String var3;
      if (!this.isInstance(var1)) {
         var3 = DeploymentManagerLogger.unrecognizedType(var1.getClass().getName());
         throw new ClassCastException(var3);
      } else if (!this.isInstance(var2)) {
         var3 = DeploymentManagerLogger.unrecognizedType(var2.getClass().getName());
         throw new ClassCastException(var3);
      }
   }

   protected int defaultCompare(Object var1, Object var2) {
      return var1 instanceof WebLogicMBean && var2 instanceof WebLogicMBean ? ((WebLogicMBean)var1).getName().compareTo(((WebLogicMBean)var2).getName()) : 0;
   }

   protected int compare(BasicDeploymentMBean var1, BasicDeploymentMBean var2) {
      int var3 = DeploymentOrder.getCachedDeploymentOrder(var1);
      int var4 = DeploymentOrder.getCachedDeploymentOrder(var2);
      if (var3 < var4) {
         return -1;
      } else {
         return var4 < var3 ? 1 : this.defaultCompare(var1, var2);
      }
   }

   // $FF: synthetic method
   DeploymentType(String var1, Class var2, Object var3) {
      this(var1, var2);
   }
}
