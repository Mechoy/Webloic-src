package weblogic.wsee.deploy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.j2ee.descriptor.wl.WebserviceDescriptionBean;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.internal.WsdlDefinitionsImpl;

public class WsdlFilePublishHelper {
   private static final boolean verbose = Verbose.isVerbose(WsdlFilePublishHelper.class);
   private Set wsdlFiles = new HashSet();
   private Map fileNames = new HashMap();

   public WsdlFilePublishHelper(WeblogicWebservicesBean var1) {
      if (var1 != null) {
         WebserviceDescriptionBean[] var2 = var1.getWebserviceDescriptions();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].getWsdlPublishFile();
            if (var4 != null) {
               this.fileNames.put(var2[var3].getWebserviceDescriptionName(), var4);
            }
         }

      }
   }

   public void publish(String var1, String var2, WsdlDefinitions var3) throws IOException {
      String var4 = (String)this.fileNames.get(var1);
      if (var4 != null) {
         if (var2.startsWith("META-INF/wsdl")) {
            var4 = var4 + var2.substring(13);
         } else {
            if (!var2.startsWith("WEB-INF/wsdl")) {
               throw new IOException("Wsdl file should be placed at META-INF/wsdl, or WEB-INF/wsdl");
            }

            var4 = var4 + var2.substring(12);
         }

         File var5 = new File(var4);
         this.wsdlFiles.add(var5);

         try {
            if (verbose) {
               Verbose.log((Object)("writing wsdl " + var5.getAbsolutePath()));
            }

            if (var3 instanceof WsdlDefinitionsImpl) {
               ((WsdlDefinitionsImpl)var3).writeToFile(var5);
            }

         } catch (WsdlException var7) {
            throw new IOException("Failed to write WSDL: " + var7);
         }
      }
   }

   public void unpublishAll() {
   }
}
