package weblogic.xml.schema.binding;

import weblogic.utils.NestedException;

public class SerializationException extends NestedException {
   public SerializationException() {
   }

   public SerializationException(String var1) {
      super(var1);
   }

   public SerializationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SerializationException(Throwable var1) {
      super(var1);
   }
}
