package weblogic.xml.process;

import weblogic.utils.NestedException;

public class ProcessorFactoryException extends NestedException {
   public ProcessorFactoryException() {
   }

   public ProcessorFactoryException(String var1) {
      super(var1);
   }

   public ProcessorFactoryException(Throwable var1) {
      super(var1);
   }

   public ProcessorFactoryException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
