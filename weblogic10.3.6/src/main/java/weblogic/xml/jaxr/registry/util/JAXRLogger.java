package weblogic.xml.jaxr.registry.util;

public interface JAXRLogger {
   void error(String var1);

   void info(String var1);

   void debug(String var1);

   void trace(String var1);

   void error(Throwable var1);

   void info(Throwable var1);

   void debug(Throwable var1);

   void trace(Throwable var1);
}
