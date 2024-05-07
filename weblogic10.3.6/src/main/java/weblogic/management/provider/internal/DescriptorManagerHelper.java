package weblogic.management.provider.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.management.VersionConstants;
import weblogic.management.upgrade.ConfigFileHelper;

public final class DescriptorManagerHelper {
   public static DescriptorManager getDescriptorManager(boolean var0) {
      return (DescriptorManager)(var0 ? DescriptorManagerHelper.EDIT_SINGLETON.instance : DescriptorManagerHelper.READONLY_SINGLETON.instance);
   }

   public static Descriptor loadDescriptor(InputStream var0, DescriptorManagerHelperContext var1) throws IOException, XMLStreamException {
      DescriptorManager var2 = getDescriptorManager(var1.isEditable());
      if (var1.isTransform()) {
         ConfigReaderContext var3 = new ConfigReaderContext();
         ConfigReader var4 = new ConfigReader(var0, var3);
         Descriptor var5 = var2.createDescriptor(var4, var1.getErrors(), var1.isValidate());
         if (var4.isModified()) {
            var1.setTransformed(true);
         }

         return var5;
      } else {
         return var2.createDescriptor(var0, var1.getErrors(), var1.isValidate());
      }
   }

   public static Descriptor loadDescriptor(String var0, boolean var1, boolean var2, List var3) throws IOException, XMLStreamException {
      DescriptorManagerHelperContext var4 = new DescriptorManagerHelperContext();
      var4.setEditable(var1);
      var4.setValidate(var2);
      var4.setTransform(true);
      var4.setErrors(var3);
      if (var1) {
         var4.setEProductionModeEnabled(ConfigFileHelper.getProductionModeEnabled() || Boolean.getBoolean("weblogic.ProductionModeEnabled"));
      } else {
         var4.setRProductionModeEnabled(ConfigFileHelper.getProductionModeEnabled() || Boolean.getBoolean("weblogic.ProductionModeEnabled"));
      }

      File var5 = new File(var0);
      FileInputStream var6 = new FileInputStream(var5);
      return loadDescriptor(var6, var4);
   }

   public static void saveDescriptor(Descriptor var0, OutputStream var1) throws IOException {
      DescriptorManagerHelper.EDIT_SINGLETON.instance.writeDescriptorAsXML(var0, var1);
   }

   private static void setSchemaLocationIfNeeded(EditableDescriptorManager var0) {
      String var1 = System.getProperty("weblogic.configuration.schema.location.replace");
      if (var1 != null) {
         var0.setSchemaLocation(var1);
      }

   }

   private static class EDIT_SINGLETON {
      static EditableDescriptorManager instance = new EditableDescriptorManager();

      static {
         instance.setProductionMode(ConfigFileHelper.getProductionModeEnabled() || Boolean.getBoolean("weblogic.ProductionModeEnabled"));
         DescriptorManagerHelper.setSchemaLocationIfNeeded(instance);
         instance.addInitialNamespace("sec", "http://xmlns.oracle.com/weblogic/security");
         instance.addInitialNamespace("wls", "http://xmlns.oracle.com/weblogic/security/wls");

         for(int var0 = 0; var0 < VersionConstants.NAMESPACE_MAPPING.length; ++var0) {
            instance.addNamespaceMapping(VersionConstants.NAMESPACE_MAPPING[var0][0], VersionConstants.NAMESPACE_MAPPING[var0][1]);
         }

      }
   }

   private static class READONLY_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();

      static {
         instance.setProductionMode(ConfigFileHelper.getProductionModeEnabled() || Boolean.getBoolean("weblogic.ProductionModeEnabled"));

         for(int var0 = 0; var0 < VersionConstants.NAMESPACE_MAPPING.length; ++var0) {
            instance.addNamespaceMapping(VersionConstants.NAMESPACE_MAPPING[var0][0], VersionConstants.NAMESPACE_MAPPING[var0][1]);
         }

      }
   }
}
