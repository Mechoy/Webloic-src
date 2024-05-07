package weblogic.ejb.spi;

import java.util.Collection;
import java.util.Properties;
import weblogic.utils.jars.VirtualJarFile;

public interface VersionHelper {
   boolean needsRecompile(String var1, ClassLoader var2) throws ClassNotFoundException;

   Collection needsRecompile(VirtualJarFile var1);

   Collection needsRecompile(Properties var1);

   Properties getCurrentJarHash();

   void removeCompilerOptions(Properties var1);
}
