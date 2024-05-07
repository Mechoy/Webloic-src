package weblogic.application.ddconvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.utils.EarUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class EarConverter implements Converter {
   private static final String STANDARD_DD;
   private static final String WEBLOGIC_DD;

   public void printStartMessage(String var1) {
      ConvertCtx.debug("START Converting EAR " + var1);
   }

   public void printEndMessage(String var1) {
      ConvertCtx.debug("END Converting EAR " + var1);
   }

   public void convertDDs(ConvertCtx var1, VirtualJarFile var2, File var3) throws DDConvertException {
      FileOutputStream var4 = null;
      FileOutputStream var5 = null;
      GenericClassLoader var6 = null;

      try {
         (new File(var3, "META-INF")).mkdirs();
         var4 = new FileOutputStream(new File(var3, STANDARD_DD));
         if (var1.isVerbose()) {
            ConvertCtx.debug("Converting " + STANDARD_DD);
         }

         var6 = var1.newClassLoader(var2);
         ApplicationDescriptor var7 = new ApplicationDescriptor(var1.getDescriptorManager(), var6);
         ApplicationBean var8 = var7.getApplicationDescriptor();
         var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var8, var4);
         WeblogicApplicationBean var9 = var7.getWeblogicApplicationDescriptor();
         if (var9 != null) {
            if (var1.isVerbose()) {
               ConvertCtx.debug("Converting " + WEBLOGIC_DD);
            }

            var5 = new FileOutputStream(new File(var3, WEBLOGIC_DD));
            var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var9, var5);
         }

         this.convertModules(var8.getModules(), var1, var3);
      } catch (Exception var20) {
         var20.printStackTrace();
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

   private void convertModules(ModuleBean[] var1, ConvertCtx var2, File var3) throws DDConvertException, IOException {
      if (var1 != null && var1.length != 0) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var5 = EarUtils.reallyGetModuleURI(var1[var4]);
            VirtualJarFile var6 = null;

            try {
               var6 = var2.getModuleVJF(var5);
               if (var2.isVerbose()) {
                  ConvertCtx.debug("Looking for converter for module-uri " + var5);
               }

               Converter var7 = ConverterFactory.findConverter(var2, var6);
               if (var7 != null) {
                  File var8 = new File(var3, var5);
                  var8.mkdirs();
                  if (!var2.isQuiet()) {
                     var7.printStartMessage(var5);
                  }

                  var7.convertDDs(var2, var6, var8);
                  if (!var2.isQuiet()) {
                     var7.printEndMessage(var5);
                  }
               }
            } finally {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (IOException var14) {
                  }
               }

            }
         }

      }
   }

   static {
      STANDARD_DD = "META-INF" + File.separator + "application.xml";
      WEBLOGIC_DD = "META-INF" + File.separator + "weblogic-application.xml";
   }
}
