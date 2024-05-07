package weblogic.xml.security.utils;

import weblogic.utils.NestedException;

public class XMLReaderException extends NestedException {
   public XMLReaderException() {
   }

   public XMLReaderException(Throwable var1) {
      super(var1);
   }

   public XMLReaderException(String var1) {
      super(var1);
   }

   public XMLReaderException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
