package weblogic.wsee.util.cow;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;

class CowReaderImpl implements CowReader {
   private ClassLoader cl = null;
   private File cowFile;
   private Map<String, WsdlDefinitions> definitions = new HashMap();

   CowReaderImpl(File var1) {
      this.cowFile = var1;
      this.cl = getCowClassLoader(var1);
   }

   private static URLClassLoader getCowClassLoader(File var0) {
      URL var1 = null;

      try {
         var1 = var0.toURI().toURL();
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("compileWsdl file " + var0.getAbsolutePath() + "is invalid.", var3);
      }

      URLClassLoader var2 = new URLClassLoader(new URL[]{var1}, (ClassLoader)null);
      return var2;
   }

   public WsdlDefinitions getWsdl(String var1) throws WsdlException {
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      if (this.definitions.containsKey(var1)) {
         return (WsdlDefinitions)this.definitions.get(var1);
      } else {
         URL var2 = this.cl.getResource(var1);
         if (var2 == null) {
            throw new WsdlException("wsdl was not found in compiledWsdl file at: " + var1);
         } else {
            WsdlDefinitions var3 = WsdlFactory.getInstance().parse(var2.toExternalForm());
            this.definitions.put(var1, var3);
            return var3;
         }
      }
   }

   public ClassLoader getClassLoader() {
      return this.cl;
   }

   public File getCowFile() {
      return this.cowFile;
   }

   public void cleanup() {
      this.cl = null;
   }
}
