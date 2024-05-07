package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.module.JMSParser;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.internal.DescriptorInfoUtils;

public class JMSSystemResource extends ConfigurationExtension {
   private String _DescriptorFileName;

   public JMSSystemResource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public String getDescriptorFileName() {
      return this._DescriptorFileName;
   }

   public void setDescriptorFileName(String var1) {
      String var2 = "jms/";
      if (var1 != null && this.isEdit() && !(new File(var1)).getPath().startsWith((new File(var2)).getPath())) {
         this._DescriptorFileName = var2 + var1;
      } else {
         this._DescriptorFileName = var1;
      }

   }

   public DescriptorBean getResource() {
      return (DescriptorBean)this.getJMSResource();
   }

   public JMSBean getJMSResource() {
      return (JMSBean)this.getExtensionRoot(JMSBean.class, "JMSResource", "jms");
   }

   public void _postCreate() {
      this.getJMSResource();
   }

   public void _preDestroy() {
      ConfigurationMBean var1 = this.getMbean();
      DescriptorInfoUtils.removeDescriptorInfo((DescriptorBean)this.getJMSResource(), var1.getDescriptor());
   }

   protected Descriptor loadDescriptor(DescriptorManager var1, InputStream var2, List var3) throws Exception {
      return ((DescriptorBean)JMSParser.createJMSDescriptor(var2, var1, var3, true)).getDescriptor();
   }
}
