package weblogic.management.mbeans.custom;

import java.io.File;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.Debug;
import weblogic.utils.jars.VirtualJarFile;

public final class ConnectorComponent extends Component {
   private static final long serialVersionUID = -6688751447109089056L;

   public ConnectorComponent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void refreshDDsIfNeeded(String[] var1) {
      if (!this.isConfig()) {
         if ((var1 == null || this.containsDD(var1, "ra.xml") || this.containsDD(var1, "weblogic-ra.xml")) && DEBUG) {
            Debug.say("Resetting Editor and Descriptor Tree");
         }

      }
   }

   protected TopLevelDescriptorMBean readDescriptor(VirtualJarFile var1, File var2, String var3) throws Exception {
      return null;
   }

   protected boolean isPhysicalModule() {
      return true;
   }
}
