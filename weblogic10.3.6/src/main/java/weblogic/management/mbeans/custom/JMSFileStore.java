package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.JMSFileStoreMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JMSFileStore extends ConfigurationMBeanCustomizer {
   private transient FileStoreMBean newBean;
   private transient JMSServerMBean serverBean;
   private String directory;
   private String synchronousWritePolicy;

   public JMSFileStore(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setDelegatedBean(FileStoreMBean var1) {
      this.newBean = var1;
   }

   public FileStoreMBean getDelegatedBean() {
      return this.newBean;
   }

   public void setDelegatedJMSServer(JMSServerMBean var1) {
      this.serverBean = var1;
   }

   public JMSServerMBean getDelegatedJMSServer() {
      return this.serverBean;
   }

   public static void copy(JMSFileStoreMBean var0, FileStoreMBean var1) {
      try {
         if (var0.getDirectory() != null) {
            var1.setDirectory(var0.getDirectory());
         }

         if (var0.getSynchronousWritePolicy() != null) {
            var1.setSynchronousWritePolicy(var0.getSynchronousWritePolicy());
         }
      } catch (InvalidAttributeValueException var3) {
      } catch (DistributedManagementException var4) {
      }

   }

   public void setDirectory(String var1) throws InvalidAttributeValueException {
      if (this.newBean != null) {
         this.newBean.setDirectory(var1);
      } else if (this.serverBean != null) {
         this.serverBean.setPagingDirectory(var1);
      } else {
         this.directory = var1;
      }

   }

   public String getDirectory() {
      if (this.newBean != null) {
         return this.newBean.getDirectory();
      } else {
         return this.serverBean != null ? this.serverBean.getPagingDirectory() : this.directory;
      }
   }

   public void setSynchronousWritePolicy(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.newBean != null) {
         this.newBean.setSynchronousWritePolicy(var1);
      } else {
         this.synchronousWritePolicy = var1;
      }

   }

   public String getSynchronousWritePolicy() {
      return this.newBean != null ? this.newBean.getSynchronousWritePolicy() : this.synchronousWritePolicy;
   }
}
