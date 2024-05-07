package weblogic.jms.backend;

import javax.management.InvalidAttributeValueException;
import weblogic.jms.JMSLogger;
import weblogic.management.ManagementException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.management.configuration.JMSFileStoreMBean;
import weblogic.management.configuration.JMSJDBCStoreMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSStoreMBean;
import weblogic.management.configuration.JMSTemplateMBean;
import weblogic.management.configuration.PersistentStoreMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class BEConfigUpdater implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      try {
         JMSServerMBean[] var3 = var1.getJMSServers();

         int var2;
         for(var2 = 0; var3 != null && var2 < var3.length; ++var2) {
            this.updateJMSServer(var1, var3[var2]);
         }

         JMSStoreMBean[] var4 = var1.getJMSStores();

         for(var2 = 0; var4 != null && var2 < var4.length; ++var2) {
            if (var4[var2] instanceof JMSFileStoreMBean) {
               this.updateFileStore(var1, (JMSFileStoreMBean)var4[var2]);
            } else {
               if (!(var4[var2] instanceof JMSJDBCStoreMBean)) {
                  throw new AssertionError("Unknown JMSStoreMBean type");
               }

               this.updateJDBCStore(var1, (JMSJDBCStoreMBean)var4[var2]);
            }
         }

      } catch (InvalidAttributeValueException var5) {
         throw new UpdateException(var5);
      } catch (ManagementException var6) {
         throw new UpdateException(var6);
      }
   }

   private void updateJMSServer(DomainMBean var1, JMSServerMBean var2) throws InvalidAttributeValueException, ManagementException {
      JMSStoreMBean var3 = var2.getStore();
      JMSFileStoreMBean var5;
      if (var3 != null) {
         var2.setStore((JMSStoreMBean)null);
         Object var4;
         if (var3 instanceof JMSFileStoreMBean) {
            var5 = (JMSFileStoreMBean)var3;
            var4 = this.updateFileStore(var1, var5);
         } else {
            if (!(var3 instanceof JMSJDBCStoreMBean)) {
               throw new AssertionError("Unknown JMSStoreMBean type");
            }

            JMSJDBCStoreMBean var7 = (JMSJDBCStoreMBean)var3;
            var4 = this.updateJDBCStore(var1, var7);
         }

         ((PersistentStoreMBean)var4).setTargets(var2.getTargets());
         var2.setPersistentStore((PersistentStoreMBean)var4);
      } else if (var2.getPersistentStore() == null) {
         var2.setStoreEnabled(false);
      }

      JMSStoreMBean var6 = var2.getPagingStore();
      if (var6 != null) {
         var2.setPagingStore((JMSStoreMBean)null);
         if (var6 instanceof JMSFileStoreMBean) {
            JMSLogger.logReplacingJMSPagingStore(var2.getName());
            var5 = (JMSFileStoreMBean)var6;
            var2.setPagingDirectory(var5.getDirectory());
            var1.destroyJMSFileStore(var5);
         } else {
            if (!(var6 instanceof JMSJDBCStoreMBean)) {
               throw new AssertionError("Unknown JMSStoreMBean type");
            }

            JMSLogger.logReplacingJMSJDBCPagingStore(var2.getName());
         }
      }

      JMSTemplateMBean var8 = var2.getTemporaryTemplate();
      if (var8 != null) {
         var2.setHostingTemporaryDestinations(true);
         var2.setTemporaryTemplateResource("interop-jms");
         var2.setTemporaryTemplateName(var8.getName());
      } else if (var2.getTemporaryTemplateResource() == null && var2.getTemporaryTemplateName() == null) {
         var2.setHostingTemporaryDestinations(false);
      }

      if (!var2.isSet("AllowsPersistentDowngrade")) {
         var2.setAllowsPersistentDowngrade(true);
      }

   }

   private FileStoreMBean updateFileStore(DomainMBean var1, JMSFileStoreMBean var2) throws InvalidAttributeValueException, ManagementException {
      JMSLogger.logReplacingJMSFileStoreMBean(var2.getName());
      FileStoreMBean var3 = var1.createFileStore(var2.getName());
      if (var2.getDirectory() != null) {
         var3.setDirectory(var2.getDirectory());
      }

      if (var2.getSynchronousWritePolicy() != null) {
         var3.setSynchronousWritePolicy(var2.getSynchronousWritePolicy());
      } else {
         var3.setSynchronousWritePolicy("Direct-Write");
      }

      var1.destroyJMSFileStore(var2);
      return var3;
   }

   private JDBCStoreMBean updateJDBCStore(DomainMBean var1, JMSJDBCStoreMBean var2) throws InvalidAttributeValueException {
      JMSLogger.logReplacingJMSJDBCStoreMBean(var2.getName());
      JDBCStoreMBean var3 = var1.createJDBCStore(var2.getName());
      var3.setConnectionPool(var2.getConnectionPool());
      if (var2.getPrefixName() != null) {
         var3.setPrefixName(var2.getPrefixName());
      }

      var1.destroyJMSJDBCStore(var2);
      return var3;
   }
}
