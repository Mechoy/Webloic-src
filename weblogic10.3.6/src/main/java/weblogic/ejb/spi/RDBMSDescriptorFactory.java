package weblogic.ejb.spi;

import java.io.File;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;

public final class RDBMSDescriptorFactory {
   public static RDBMSDescriptor createRDBMSDescriptor(DescriptorManager var0, String var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      return new weblogic.ejb.container.cmp.rdbms.RDBMSDescriptor(var0, var1, var2, var3, var4, var5);
   }
}
