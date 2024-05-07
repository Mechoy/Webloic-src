package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.module.WLDFDescriptorLoader;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.internal.DescriptorInfoUtils;

public class WLDFSystemResource extends ConfigurationExtension {
   private static final String DEFAULT_CONFIG = "schema/diagnostics-binding.jar";
   private String _DescriptorFileName;

   public WLDFSystemResource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getDescriptorFileName() {
      return this._DescriptorFileName;
   }

   public void setDescriptorFileName(String var1) {
      String var2 = "diagnostics/";
      if (var1 != null && this.isEdit() && !(new File(var1)).getPath().startsWith((new File(var2)).getPath())) {
         this._DescriptorFileName = var2 + var1;
      } else {
         this._DescriptorFileName = var1;
      }

   }

   public DescriptorBean getResource() {
      return (DescriptorBean)this.getWLDFResource();
   }

   public WLDFResourceBean getWLDFResource() {
      WLDFResourceBean var1 = (WLDFResourceBean)this.getExtensionRoot(WLDFResourceBean.class, "WLDFResource", "diagnostics");
      if (var1 == null) {
         return null;
      } else {
         if (var1.getName() == null) {
            var1.setName(this.getMbean().getName());
         }

         return var1;
      }
   }

   protected Descriptor loadDescriptor(DescriptorManager var1, InputStream var2, List var3) throws Exception {
      WLDFDescriptorLoader var4 = new WLDFDescriptorLoader(var2, var1, var3);
      return var4.loadDescriptorBean().getDescriptor();
   }

   public void _preDestroy() {
      ConfigurationMBean var1 = this.getMbean();
      DescriptorInfoUtils.removeDescriptorInfo((DescriptorBean)this.getWLDFResource(), var1.getDescriptor());
   }
}
