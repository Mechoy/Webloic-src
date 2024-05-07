package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import weblogic.cacheprovider.coherence.CoherenceClusterDescriptorHelper;
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.internal.PendingDirectoryManager;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.internal.DescriptorInfoUtils;
import weblogic.utils.FileUtils;
import weblogic.utils.io.StreamUtils;

public class CoherenceClusterSystemResource extends ConfigurationExtension {
   private String _DescriptorFileName;
   private String _customConfigFile;
   private static final String DEFAULT_APPENDIX = "-coherence.xml";
   private static final String DEFAULT_CUSTOM_CONFIG_FILE_NAME = "tangosol-coherence-override.xml";

   public CoherenceClusterSystemResource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public static String constructDefaultCoherenceSystemFilename(String var0) {
      var0 = var0.trim();
      if (var0.endsWith("-coherence")) {
         var0 = var0.substring(0, var0.length() - 10);
      }

      return "coherence/" + var0 + "/" + FileUtils.mapNameToFileName(var0) + "-coherence.xml";
   }

   public String getDescriptorFileName() {
      return this._DescriptorFileName;
   }

   public void setDescriptorFileName(String var1) {
      String var2 = "coherence/";
      if (var1 != null && this.isEdit() && !(new File(var1)).getPath().startsWith((new File(var2)).getPath())) {
         this._DescriptorFileName = var2 + var1;
      } else {
         this._DescriptorFileName = var1;
      }

   }

   public DescriptorBean getResource() {
      return (DescriptorBean)this.getCoherenceClusterResource();
   }

   public WeblogicCoherenceBean getCoherenceClusterResource() {
      WeblogicCoherenceBean var1 = (WeblogicCoherenceBean)this.getExtensionRoot(WeblogicCoherenceBean.class, "CoherenceClusterResource", "coherence");
      if (var1 != null && this.getMbean() != null && var1.getName() == null) {
         var1.setName(this.getMbean().getName());
      }

      return var1;
   }

   public String getCustomClusterConfigurationFileName() {
      WeblogicCoherenceBean var1 = this.getCoherenceClusterResource();
      return var1 != null ? var1.getCustomClusterConfigurationFileName() : null;
   }

   public boolean isUsingCustomClusterConfigurationFile() {
      return this.getCustomClusterConfigurationFileName() != null;
   }

   public void importCustomClusterConfigurationFile(String var1) throws ManagementException {
      try {
         File var2 = new File(var1);
         if (var2.exists() && var2.isFile() && var2.canRead()) {
            String var3 = var2.getCanonicalPath();
            String var4 = this.getCustomClusterConfigurationFileName();
            if (var4 != null && !var4.equals(var3)) {
               throw new ManagementException("Cannot associate a new custom cluster configuration file for " + this.getName() + "." + this.getName() + " already using " + var4);
            } else {
               WeblogicCoherenceBean var5 = this.getCoherenceClusterResource();
               String var6 = "coherence/" + this.getName() + "/" + var2.getName();
               PendingDirectoryManager var7 = PendingDirectoryManager.getInstance();
               FileInputStream var8 = new FileInputStream(var2);
               OutputStream var9 = null;

               try {
                  var8 = new FileInputStream(var2);
                  var9 = var7.getFileOutputStream(var6);
                  StreamUtils.writeTo(var8, var9);
               } finally {
                  if (var8 != null) {
                     var8.close();
                  }

                  if (var9 != null) {
                     var9.close();
                  }

               }

               this.updateCustomConfigurationFileDetails(var3);
               this._customConfigFile = var3;
            }
         } else {
            throw new ManagementException("Unable to read from " + var2);
         }
      } catch (IOException var14) {
         throw new ManagementException("Failed to import custom cluster configuration file for " + this.getName(), var14);
      }
   }

   public void setUsingCustomClusterConfigurationFile(boolean var1) throws ManagementException {
      if (var1) {
         if (this._customConfigFile == null && !this.isUsingCustomClusterConfigurationFile()) {
            throw new ManagementException("Must import new custom cluster configuration file for " + this.getName() + ".");
         }
      } else if (this._customConfigFile != null) {
         this.updateCustomConfigurationFileDetails((String)null);
         this._customConfigFile = null;
      }

   }

   private void updateCustomConfigurationFileDetails(String var1) {
      WeblogicCoherenceBean var2 = this.getCoherenceClusterResource();
      if (var1 != null) {
         var2.setCustomClusterConfigurationFileName(var1);
         var2.setCustomClusterConfigurationFileLastUpdatedTimestamp(System.currentTimeMillis());
      } else {
         var2.unSet("CustomClusterConfigurationFileName");
         var2.unSet("CustomClusterConfigurationFileLastUpdatedTimestamp");
      }

   }

   public void _postCreate() {
      this.getCoherenceClusterResource();
      this._customConfigFile = this.getCustomClusterConfigurationFileName();
   }

   public void _preDestroy() {
      ConfigurationMBean var1 = this.getMbean();
      WeblogicCoherenceBean var2 = this.getCoherenceClusterResource();
      DescriptorInfoUtils.removeDescriptorInfo((DescriptorBean)var2, var1.getDescriptor());
      File var3;
      File var4;
      if (this.isUsingCustomClusterConfigurationFile()) {
         var3 = new File(var2.getCustomClusterConfigurationFileName());
         var4 = new File(DomainDir.getConfigDir() + "/" + "coherence" + "/" + var2.getName() + "/" + var3.getName());
         if (var4.exists()) {
            var4.delete();
         }
      }

      var3 = new File(DomainDir.getConfigDir() + "/" + this.getDescriptorFileName());
      var4 = var3.getParentFile();
      String[] var5 = var4.list();
      if (var5 == null || var5.length == 0) {
         var4.delete();
      }

   }

   protected Descriptor loadDescriptor(DescriptorManager var1, InputStream var2, List var3) throws Exception {
      return ((DescriptorBean)CoherenceClusterDescriptorHelper.createCoherenceDescriptor(var2, var1, var3, true)).getDescriptor();
   }
}
