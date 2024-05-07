package weblogic.management.provider.internal;

import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.management.configuration.ConfigurationExtensionMBean;

public class DescriptorInfo {
   public static final String DESCRIPTOR_INFO = "DescriptorInfo";
   public static final String DELETED_DESCRIPTOR_INFO = "DeletedDescriptorInfo";
   public static final String NOT_FOUND_DESCRIPTOR_INFO = "NotFoundDescriptorInfo";
   public static final String DESCRIPTOR_EXTENSION_LOAD = "DescriptorExtensionLoad";
   public static final String DESCRIPTOR_CONFIG_EXTENSION = "DescriptorConfigExtension";
   public static final String DESCRIPTOR_CONFIG_EXTENSION_ATTRIBUTE = "DescriptorConfigExtensionAttribute";
   private Descriptor descriptor = null;
   private Class descriptorClass = null;
   private DescriptorBean descriptorBean = null;
   private DescriptorManager descriptorManager = null;
   private ConfigurationExtensionMBean configurationExtension = null;

   public DescriptorInfo(Descriptor var1, Class var2, DescriptorBean var3, DescriptorManager var4, ConfigurationExtensionMBean var5) {
      this.descriptor = var1;
      this.descriptorClass = var2;
      this.descriptorBean = var3;
      this.descriptorManager = var4;
      this.configurationExtension = var5;
   }

   public Descriptor getDescriptor() {
      return this.descriptor;
   }

   public Class getDescriptorClass() {
      return this.descriptorClass;
   }

   public DescriptorBean getDescriptorBean() {
      return this.descriptorBean;
   }

   public DescriptorManager getDescriptorManager() {
      return this.descriptorManager;
   }

   public ConfigurationExtensionMBean getConfigurationExtension() {
      return this.configurationExtension;
   }
}
