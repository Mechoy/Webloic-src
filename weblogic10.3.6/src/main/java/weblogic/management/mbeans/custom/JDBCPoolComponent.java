package weblogic.management.mbeans.custom;

import java.io.File;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.AssertionError;
import weblogic.utils.jars.VirtualJarFile;

public class JDBCPoolComponent extends Component {
   public JDBCPoolComponent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   protected TopLevelDescriptorMBean readDescriptor(VirtualJarFile var1, File var2, String var3) {
      throw new AssertionError("This should never be invoked.");
   }

   protected boolean isPhysicalModule() {
      return false;
   }

   public void refreshDDsIfNeeded(String[] var1) {
   }
}
