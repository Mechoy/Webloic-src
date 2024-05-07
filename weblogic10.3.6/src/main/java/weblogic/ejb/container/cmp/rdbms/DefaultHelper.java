package weblogic.ejb.container.cmp.rdbms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.EntityBeanQuery;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl.CompatibilityBean;
import weblogic.j2ee.descriptor.wl.MethodParamsBean;
import weblogic.j2ee.descriptor.wl.QueryMethodBean;
import weblogic.j2ee.descriptor.wl.WeblogicQueryBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;

public final class DefaultHelper {
   private static final DebugLogger debugLogger;
   private boolean isSet_checkExistsOnMethod = false;
   private Set queriesWithIncludeUpdatesSet = null;
   private boolean isSet_orderDatabaseOperations = false;
   private boolean isSet_enableBatchOperations = false;
   private float version = 0.0F;

   public void setVersion(float var1) {
      this.version = var1;
   }

   public float getVersion() {
      return this.version;
   }

   public void setIsSet_checkExistsOnMethod(boolean var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("checkExistsOnMethod has been explicitely set in the DD to '" + var2 + "'");
      }

      this.isSet_checkExistsOnMethod = var1;
   }

   public boolean isSet_checkExistsOnMethod() {
      return this.isSet_checkExistsOnMethod;
   }

   public void addQueryWithIncludeUpdates(WeblogicQueryBean var1, String var2) {
      if (this.queriesWithIncludeUpdatesSet == null) {
         this.queriesWithIncludeUpdatesSet = new HashSet();
      }

      this.queriesWithIncludeUpdatesSet.add(var1);
      if (debugLogger.isDebugEnabled()) {
         debug("added WeblogicQueryMBean for query '" + var1.getQueryMethod().getMethodName() + "' to QueryWithIncludeUpdates List with include-updates " + "set in the DD to '" + var2 + "'");
      }

   }

   public boolean isSet_IncludeUpdates(WeblogicQueryBean var1) {
      return this.queriesWithIncludeUpdatesSet == null ? false : this.queriesWithIncludeUpdatesSet.contains(var1);
   }

   public void setIsSet_orderDatabaseOperations(boolean var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("orderDatabaseOperations has been explicitely set in the DD to '" + var2 + "'");
      }

      this.isSet_orderDatabaseOperations = var1;
   }

   public boolean isSet_orderDatabaseOperations() {
      return this.isSet_orderDatabaseOperations;
   }

   public void setIsSet_enableBatchOperations(boolean var1, String var2) {
      if (debugLogger.isDebugEnabled()) {
         debug("enableBatchOperations has been set to true, since delay-database-insert-until has been explicitely set in the DD to '" + var2 + "'");
      }

      this.isSet_enableBatchOperations = var1;
   }

   public boolean isSet_enableBatchOperations() {
      return this.isSet_enableBatchOperations;
   }

   public void adjustDefaults(String var1, Collection var2, int var3, WeblogicRdbmsJarBean var4, WeblogicRdbmsBeanBean var5) throws RDBMSException {
      if (debugLogger.isDebugEnabled()) {
         debug("adjustDefaults: ejbName- " + var5.getEjbName());
         debug("version- " + this.getVersion());
      }

      if (this.getVersion() < 8.1F) {
         if (!this.isSet_orderDatabaseOperations()) {
            if (debugLogger.isDebugEnabled()) {
               debug(" order-database-operations not set, setting pre 8.1 default 'False'");
            }

            var4.setOrderDatabaseOperations(false);
         }

         if (!this.isSet_enableBatchOperations()) {
            if (debugLogger.isDebugEnabled()) {
               debug(" enable-batch-operations not set, setting pre 8.1 default 'False'");
            }

            var4.setEnableBatchOperations(false);
         }

         if (!this.isSet_checkExistsOnMethod()) {
            if (debugLogger.isDebugEnabled()) {
               debug(" check-exists-on-method  not set, setting pre J2EE default 'False'");
            }

            var5.setCheckExistsOnMethod(false);
         }
      }

      CompatibilityBean var6 = var4.getCompatibility();
      if (var6 == null) {
         var6 = var4.createCompatibility();
      }

      var6.setSerializeCharArrayToBytes(true);
      var6.setDisableStringTrimming(true);
      var6.setFindersReturnNulls(false);
      WeblogicQueryBean[] var7 = var5.getWeblogicQueries();
      Iterator var8 = var2.iterator();

      while(var8.hasNext()) {
         EntityBeanQuery var9 = (EntityBeanQuery)var8.next();
         this.processIncludeUpdatesForQuery(var7, var9, var3, var5);
      }

   }

   private void processIncludeUpdatesForQuery(WeblogicQueryBean[] var1, EntityBeanQuery var2, int var3, WeblogicRdbmsBeanBean var4) throws RDBMSException {
      String var5 = var2.getMethodName();
      String[] var6 = var2.getMethodParams();
      if (debugLogger.isDebugEnabled()) {
         debug("processIncludeUpdatesForQuery checking method: " + DDUtils.getMethodSignature(var5, var6));
      }

      WeblogicQueryBean var7 = this.getWLQueryMBeanForEntityBeanQuery(var2, var1);
      if (var7 != null) {
         if (this.isSet_IncludeUpdates(var7)) {
            if (debugLogger.isDebugEnabled()) {
               debug("existing weblogic-query has include-updates set.  leaving setting as is.");
            }

            return;
         }

         if (debugLogger.isDebugEnabled()) {
            debug("existing weblogic-query does NOT have include-updates set.  setting pre J2EE defaults");
         }

         if (this.getVersion() < 8.1F) {
            if (debugLogger.isDebugEnabled()) {
               debug("pre 8.1: setting default include-updates to false");
            }

            var7.setIncludeUpdates(false);
         }
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("no matching weblogic-query found. setting include-updates to default value. ");
         }

         var7 = var4.createWeblogicQuery();
         QueryMethodBean var8 = var7.createQueryMethod();
         var8.setMethodName(var5);
         MethodParamsBean var9 = var8.createMethodParams();
         var9.setMethodParams(var6);
         if (this.getVersion() < 8.1F) {
            if (debugLogger.isDebugEnabled()) {
               debug("pre 8.1: setting default include-updates to false");
            }

            var7.setIncludeUpdates(false);
         }
      }

   }

   private WeblogicQueryBean getWLQueryMBeanForEntityBeanQuery(EntityBeanQuery var1, WeblogicQueryBean[] var2) {
      if (var2.length <= 0) {
         return null;
      } else {
         String var3 = var1.getMethodName();
         String[] var4 = var1.getMethodParams();

         for(int var5 = 0; var5 < var2.length; ++var5) {
            WeblogicQueryBean var6 = var2[var5];
            QueryMethodBean var7 = var6.getQueryMethod();
            if (var3.equals(var7.getMethodName())) {
               String[] var8 = var7.getMethodParams().getMethodParams();
               if (var4.length == var8.length) {
                  if (var4.length == 0 && var8.length == 0) {
                     return var6;
                  }

                  boolean var9 = false;

                  for(int var10 = 0; var10 < var4.length; ++var10) {
                     String var11 = var4[var10];
                     String var12 = var8[var10];
                     if (!var11.equals(var12)) {
                        var9 = true;
                        break;
                     }
                  }

                  if (!var9) {
                     return var6;
                  }
               }
            }
         }

         return null;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[DefaultHelper] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
