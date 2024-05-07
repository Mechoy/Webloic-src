package weblogic.xml.schema.binding;

import weblogic.utils.NestedException;

public class DeserializationException extends NestedException {
   public DeserializationException() {
   }

   public DeserializationException(String var1) {
      super(var1);
   }

   public DeserializationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public DeserializationException(Throwable var1) {
      super(var1);
   }
}
