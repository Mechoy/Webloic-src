package weblogic.ejb.container.dd.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.spi.EjbDescriptorBean;

public class AnnotationDebugger {
   public static final DebugLogger compilationLogger = DebugLogger.getDebugLogger("DebugEjbAnnotationProcessor");

   public static void log(Class var0, Annotation var1) {
      String var2 = "Processing " + var1 + " on " + var0;
      info(var2);
   }

   public static void log(Class var0, Method var1, Annotation var2) {
      String var3 = "Processing " + var2 + " on '" + var1.getName() + "' method of " + var0;
      info(var3);
   }

   public static void log(Class var0, Method var1, String var2) {
      String var3 = "Processing " + var2 + " on '" + var1 + "' method of " + var0;
      info(var3);
   }

   public static void log(Class var0, Field var1, Annotation var2) {
      String var3 = "Processing " + var2 + " on '" + var1.getName() + "' field of " + var0;
      info(var3);
   }

   public static void log(Class var0, String var1) {
      String var2 = "Processing " + var1 + " on " + var0;
      info(var2);
   }

   public static void logEjbJar(EjbDescriptorBean var0) {
      if (isDebugEnabled()) {
         DescriptorBean var1 = (DescriptorBean)var0.getEjbJarBean();
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();

         try {
            var1.getDescriptor().toXML(var2);
            info(var2.toString());
         } catch (IOException var4) {
         }
      }

   }

   public static void logWlsEjbJar(EjbDescriptorBean var0) {
      if (isDebugEnabled()) {
         DescriptorBean var1 = (DescriptorBean)var0.getWeblogicEjbJarBean();
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();

         try {
            var1.getDescriptor().toXML(var2);
            info(var2.toString());
         } catch (IOException var4) {
         }
      }

   }

   public static void info(String var0) {
      if (isDebugEnabled()) {
         compilationLogger.debug(var0 + "\n");
      }

   }

   public static boolean isDebugEnabled() {
      return compilationLogger.isDebugEnabled();
   }
}
