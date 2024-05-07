package weblogic.auddi.xml;

import weblogic.auddi.NestedException;

public class SchemaException extends NestedException {
   public SchemaException(String var1) {
      super((Throwable)null, var1);
   }

   public SchemaException(String var1, Throwable var2) {
      super(var2, var1);
   }

   public SchemaException(Exception var1) {
      super((Throwable)var1);
   }
}
