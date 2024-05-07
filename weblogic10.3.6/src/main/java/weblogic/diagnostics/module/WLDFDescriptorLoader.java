package weblogic.diagnostics.module;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class WLDFDescriptorLoader extends AbstractDescriptorLoader2 {
   public WLDFDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   public WLDFDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
   }

   public WLDFDescriptorLoader(InputStream var1, DescriptorManager var2, List var3) {
      super(var1, var2, var3, true);
   }

   public WLDFDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
      return new WLDFVersionMunger(var1, this);
   }
}
