package weblogic.application.ddconvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class WarConverter implements Converter {
   private static final String STANDARD_DD;
   private static final String WEBLOGIC_DD;

   public void printStartMessage(String var1) {
      ConvertCtx.debug("START Converting WAR " + var1);
   }

   public void printEndMessage(String var1) {
      ConvertCtx.debug("END Converting WAR " + var1);
   }

   public void convertDDs(ConvertCtx var1, VirtualJarFile var2, File var3) throws DDConvertException {
      FileOutputStream var4 = null;
      FileOutputStream var5 = null;
      GenericClassLoader var6 = null;

      try {
         (new File(var3, "WEB-INF")).mkdirs();
         if (var1.isVerbose()) {
            ConvertCtx.debug("Converting " + STANDARD_DD);
         }

         var4 = new FileOutputStream(new File(var3, STANDARD_DD));
         var6 = var1.newClassLoader(var2);
         WebAppDescriptor var7 = new WebAppDescriptor(var1.getDescriptorManager(), var6);
         WebAppBean var8 = var7.getWebAppBean();
         var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var8, var4);
         WeblogicWebAppBean var9 = var7.getWeblogicWebAppBean();
         if (var9 != null) {
            if (var1.isVerbose()) {
               ConvertCtx.debug("Converting " + WEBLOGIC_DD);
            }

            var5 = new FileOutputStream(new File(var3, WEBLOGIC_DD));
            var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var9, var5);
         }
      } catch (Exception var20) {
         throw new DDConvertException(var20);
      } finally {
         if (var6 != null) {
            var6.close();
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var19) {
            }
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var18) {
            }
         }

      }

   }

   static {
      STANDARD_DD = "WEB-INF" + File.separator + "web.xml";
      WEBLOGIC_DD = "WEB-INF" + File.separator + "weblogic.xml";
   }
}
