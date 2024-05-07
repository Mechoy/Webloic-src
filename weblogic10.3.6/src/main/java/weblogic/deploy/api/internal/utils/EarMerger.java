package weblogic.deploy.api.internal.utils;

import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;

public class EarMerger extends AppMerger {
   public DescriptorSupport getDescriptorSupport() {
      return DescriptorSupportManager.getForTag("application");
   }

   protected LibraryRefBean[] getLibraryRefs(DescriptorBean var1) {
      WeblogicApplicationBean var2 = (WeblogicApplicationBean)var1;
      return var2.getLibraryRefs();
   }
}
