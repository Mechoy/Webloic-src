package weblogic.management.mbeans.custom;

import java.io.File;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.jars.VirtualJarFile;

public class LibraryComponent extends Component {
   public LibraryComponent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   protected boolean isPhysicalModule() {
      return true;
   }

   protected TopLevelDescriptorMBean readDescriptor(VirtualJarFile var1, File var2, String var3) throws Exception {
      return null;
   }

   public void refreshDDsIfNeeded(String[] var1) {
   }
}
