package weblogic.application.ddconvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.connector.configuration.ConnectorDescriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public final class RarConverter implements Converter {
   private static final String STANDARD_DD;
   private static final String WEBLOGIC_DD;

   public void printStartMessage(String var1) {
      ConvertCtx.debug("START Converting RAR " + var1);
   }

   public void printEndMessage(String var1) {
      ConvertCtx.debug("END Converting RAR " + var1);
   }

   public void convertDDs(ConvertCtx var1, VirtualJarFile var2, File var3) throws DDConvertException {
      FileOutputStream var4 = null;
      GenericClassLoader var5 = null;

      try {
         var5 = var1.newClassLoader(var2);
         (new File(var3, "META-INF")).mkdirs();
         ConnectorDescriptor var6 = new ConnectorDescriptor(var1.getDescriptorManager(), var5, false);
         WeblogicConnectorBean var7 = var6.getWeblogicConnectorBean();
         if (var7 != null) {
            if (var1.isVerbose()) {
               ConvertCtx.debug("Converting " + WEBLOGIC_DD);
            }

            this.convertWLRADD(var1, var7, var3);
         }

         ConnectorBean var8 = var6.getConnectorBean();
         if (var8 != null) {
            if (var1.isVerbose()) {
               ConvertCtx.debug("Converting " + STANDARD_DD);
            }

            var4 = new FileOutputStream(new File(var3, STANDARD_DD));
            var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var8, var4);
         } else {
            ConvertCtx.debug("No ra.xml exists. Adapter may be a link-ref. Note that the adapter may still be deployed successfully as a 1.0 adapter without conversion.");
         }
      } catch (Exception var16) {
         throw new DDConvertException(var16);
      } finally {
         if (var5 != null) {
            var5.close();
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var15) {
            }
         }

      }

   }

   private void convertWLRADD(ConvertCtx var1, WeblogicConnectorBean var2, File var3) throws DDConvertException {
      FileOutputStream var4 = null;

      try {
         if (var1.isVerbose()) {
            ConvertCtx.debug("Converting " + WEBLOGIC_DD);
         }

         var4 = new FileOutputStream(new File(var3, WEBLOGIC_DD));
         var1.getDescriptorManager().writeDescriptorBeanAsXML((DescriptorBean)var2, var4);
      } catch (Exception var13) {
         throw new DDConvertException(var13);
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var12) {
            }
         }

      }

   }

   static {
      STANDARD_DD = "META-INF" + File.separator + "ra.xml";
      WEBLOGIC_DD = "META-INF" + File.separator + "weblogic-ra.xml";
   }
}
