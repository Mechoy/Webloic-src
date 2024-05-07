package weblogic.ejb.spi;

import java.util.Collection;
import weblogic.utils.jars.VirtualJarFile;

public interface DeploymentInfo {
   String getModuleURI();

   String getSecurityRealmName();

   VirtualJarFile getVirtualJarFile();

   Collection getBeanInfos();

   ClassLoader getModuleClassLoader();

   boolean isWarningDisabled(String var1);

   String getApplicationId();
}
