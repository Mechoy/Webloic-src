package weblogic.management.mbeans.custom;

import weblogic.descriptor.DescriptorBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public class SystemResource extends ConfigurationExtension {
   public SystemResource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public DescriptorBean getResource() {
      throw new AssertionError("Derived System Resource should implement getResource");
   }
}
