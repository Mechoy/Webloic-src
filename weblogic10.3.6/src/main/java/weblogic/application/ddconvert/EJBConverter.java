package weblogic.application.ddconvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.ejb.spi.EjbDescriptorReader;
import weblogic.ejb.spi.EjbJarDescriptor;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;

public final class EJBConverter implements Converter {
   private static final String STANDARD_DD;
   private static final String WEBLOGIC_DD;

   public void printStartMessage(String var1) {
      ConvertCtx.debug("START Converting EJB-JAR " + var1);
   }

   public void printEndMessage(String var1) {
      ConvertCtx.debug("END Converting EJB-JAR " + var1);
   }

   public void convertDDs(ConvertCtx var1, VirtualJarFile var2, File var3) throws DDConvertException {
      FileOutputStream var4 = null;
      FileOutputStream var5 = null;
      GenericClassLoader var6 = null;
      EjbDescriptorBean var7 = new EjbDescriptorBean(false);

      try {
         (new File(var3, "META-INF")).mkdirs();
         if (var1.isVerbose()) {
            ConvertCtx.debug("Converting " + STANDARD_DD);
         }

         var6 = var1.newClassLoader(var2);
         var4 = new FileOutputStream(new File(var3, STANDARD_DD));
         EjbJarDescriptor var8 = new EjbJarDescriptor(var1.getDescriptorManager(), var6);
         EjbJarBean var9 = var8.getEditableEjbJarBean();
         WeblogicEjbJarBean var10 = var8.getEditableWeblogicEjbJarBean();
         var7.setEjbJarBean(var9);
         var7.setWeblogicEjbJarBean(var10);
         if (var10 != null) {
            if (var1.isVerbose()) {
               ConvertCtx.debug("Converting " + WEBLOGIC_DD);
            }

            var5 = new FileOutputStream(new File(var3, WEBLOGIC_DD));
            EjbDescriptorReader var11 = EjbDescriptorFactory.getEjbDescriptorReader();
            var11.loadWeblogicRDBMSJarMBeans(var7, var2, new ProcessorFactory(), false);
         }

         var7.usePersistenceDestination(var3.getAbsolutePath());
         var7.persist();
      } catch (Exception var22) {
         throw new DDConvertException(var22);
      } finally {
         if (var6 != null) {
            var6.close();
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var21) {
            }
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var20) {
            }
         }

      }

   }

   static {
      STANDARD_DD = "META-INF" + File.separator + "ejb-jar.xml";
      WEBLOGIC_DD = "META-INF" + File.separator + "weblogic-ejb-jar.xml";
   }
}
