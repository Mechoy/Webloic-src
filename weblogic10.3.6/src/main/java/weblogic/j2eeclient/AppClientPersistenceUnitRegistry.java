package weblogic.j2eeclient;

import java.io.File;
import java.net.MalformedURLException;
import weblogic.application.utils.IOUtils;
import weblogic.deployment.AbstractPersistenceUnitRegistry;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class AppClientPersistenceUnitRegistry extends AbstractPersistenceUnitRegistry {
   public AppClientPersistenceUnitRegistry(File var1, GenericClassLoader var2, String var3, File var4, DeploymentPlanBean var5) throws Exception, MalformedURLException {
      super(var2, var3, var4, var5);
      VirtualJarFile var6 = VirtualJarFactory.createVirtualJar(var1);

      try {
         this.loadPersistenceDescriptor(var6, true, var1);
      } finally {
         IOUtils.forceClose(var6);
      }

   }

   public PersistenceUnitInfoImpl getPersistenceUnit(String var1) {
      return (PersistenceUnitInfoImpl)this.persistenceUnits.get(var1);
   }
}
