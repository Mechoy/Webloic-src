package weblogic.connector.configuration;

import java.io.File;
import java.util.zip.ZipEntry;
import weblogic.connector.common.Debug;
import weblogic.connector.exception.RAConfigurationException;
import weblogic.j2ee.descriptor.wl.LinkRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorExtensionBean;
import weblogic.utils.jars.VirtualJarFile;

public class DDValidator {
   static void validateRARAndAltDD(VirtualJarFile var0, File var1) throws RAConfigurationException {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      String var5 = "";
      if (var1 != null) {
         var2 = true;
      }

      if (!var2) {
         if (var0 == null) {
            var5 = Debug.getExceptionNoDescriptorOrAltDD();
         } else {
            ZipEntry var6 = var0.getEntry("META-INF/weblogic-ra.xml");
            ZipEntry var7 = var0.getEntry("META-INF/ra.xml");
            var4 = var6 != null;
            var3 = var7 != null;
            if (!var3 && !var4) {
               var5 = Debug.getExceptionNoDescriptor();
            }
         }
      }

      if (var5.length() > 0) {
         throw new RAConfigurationException(var5);
      }
   }

   public static boolean isLinkRef(WeblogicConnectorExtensionBean var0) {
      if (var0 == null) {
         return false;
      } else {
         LinkRefBean var1 = var0.getLinkRef();
         if (var1 == null) {
            return false;
         } else {
            String var2 = var1.getRaLinkRef();
            return var2 != null && var2.trim().length() > 0;
         }
      }
   }
}
