package weblogic.wsee.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.xml.schema.binding.TypeMapping;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;

public class WebServiceClientJarFile extends WebServiceJarFile {
   public WebServiceClientJarFile(File var1, File var2) throws IOException {
      super(var1, var2);
      if (debug) {
         System.out.println("Creating WebServiceClientJarFile(" + var2.getName() + ")");
      }

   }

   public void writeClientDD(String var1, String var2, TypeMapping var3) throws IOException {
      String var4 = var1.replace('.', '/') + '/' + var2 + ".xml";
      File var5 = new File(this.getExploded(), var4);
      var5.getParentFile().mkdirs();
      FileOutputStream var6 = new FileOutputStream(var5);
      XMLOutputStream var7 = XMLOutputStreamFactory.newInstance().newDebugOutputStream(var6);

      try {
         var3.writeXML(var7);
         var7.flush();
         var6.flush();
      } finally {
         try {
            if (var7 != null) {
               var6.close();
            }
         } catch (IOException var17) {
         }

         try {
            if (var6 != null) {
               var6.close();
            }
         } catch (IOException var16) {
         }

      }

   }

   public String toString() {
      return "WebServiceClientJarFile[" + super.toString() + "]";
   }
}
