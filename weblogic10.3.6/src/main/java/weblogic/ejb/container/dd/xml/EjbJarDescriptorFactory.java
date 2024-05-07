package weblogic.ejb.container.dd.xml;

import java.io.File;
import weblogic.ejb.spi.EjbJarDescriptor;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.jars.VirtualJarFile;

public abstract class EjbJarDescriptorFactory {
   public abstract EjbJarDescriptor getEjbJarDescriptor();

   public static EjbJarDescriptorFactory newInstance(final VirtualJarFile var0, final File var1, final DeploymentPlanBean var2, final String var3, final String var4) {
      return new EjbJarDescriptorFactory() {
         public EjbJarDescriptor getEjbJarDescriptor() {
            return new EjbJarDescriptor(var0, var1, var2, var3, var4);
         }
      };
   }

   public static EjbJarDescriptorFactory newInstance(final VirtualJarFile var0, final String var1, final String var2) {
      return new EjbJarDescriptorFactory() {
         public EjbJarDescriptor getEjbJarDescriptor() {
            return new EjbJarDescriptor(var0, var1, var2);
         }
      };
   }

   public static EjbJarDescriptorFactory newInstance(final EjbJarDescriptor var0) {
      return new EjbJarDescriptorFactory() {
         public EjbJarDescriptor getEjbJarDescriptor() {
            return var0;
         }
      };
   }
}
