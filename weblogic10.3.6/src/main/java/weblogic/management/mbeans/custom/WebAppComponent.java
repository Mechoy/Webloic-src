package weblogic.management.mbeans.custom;

import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.Debug;

public class WebAppComponent extends Component {
   private static final long serialVersionUID = 5422191349969975414L;

   public WebAppComponent(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void refreshDDsIfNeeded(String[] var1) {
      if (!this.isConfig()) {
         if ((var1 == null || this.containsDD(var1, "weblogic.xml") || this.containsDD(var1, "web.xml")) && DEBUG) {
            Debug.say("Resetting Editor and Descriptor Tree");
         }

      }
   }

   protected boolean isPhysicalModule() {
      return true;
   }
}
