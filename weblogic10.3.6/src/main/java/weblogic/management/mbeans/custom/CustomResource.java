package weblogic.management.mbeans.custom;

import java.io.File;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.CustomResourceMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.internal.DescriptorInfoUtils;

public class CustomResource extends ConfigurationExtension {
   private String _DescriptorFileName;

   public CustomResource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getDescriptorFileName() {
      return this._DescriptorFileName;
   }

   public void setDescriptorFileName(String var1) {
      File var2 = null;
      if (var1 != null) {
         var2 = new File(var1);
         if (var2.isAbsolute()) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getFileCannotBeAbsolute(var1));
         }
      }

      String var3 = "custom/";
      if (var1 != null && this.isEdit() && var2.getParent() == null) {
         this._DescriptorFileName = var3 + var1;
      } else {
         this._DescriptorFileName = var1;
      }

   }

   public DescriptorBean getResource() {
      ClassLoader var1 = DescriptorClassLoader.getClassLoader();
      CustomResourceMBean var2 = (CustomResourceMBean)this.getMbean();
      String var3 = var2.getDescriptorBeanClass();

      Class var4;
      try {
         var4 = var1.loadClass(var3);
      } catch (ClassNotFoundException var6) {
         throw new AssertionError(var6);
      }

      return this.getExtensionRoot(var4, "Resource", "custom");
   }

   public DescriptorBean getCustomResource() {
      return this.getResource();
   }

   public void _postCreate() {
      this.getResource();
   }

   public void _preDestroy() {
      ConfigurationMBean var1 = this.getMbean();
      DescriptorInfoUtils.removeDescriptorInfo(this.getResource(), var1.getDescriptor());
   }
}
