package weblogic.xml.registry;

import weblogic.utils.NestedException;

public class XMLRegistryException extends NestedException {
   public XMLRegistryException(String var1) {
      super(var1);
   }

   public XMLRegistryException(Throwable var1) {
      super(var1);
   }

   public XMLRegistryException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
