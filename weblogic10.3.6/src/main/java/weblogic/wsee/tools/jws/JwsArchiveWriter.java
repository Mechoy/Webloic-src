package weblogic.wsee.tools.jws;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.util.IOUtil;

public class JwsArchiveWriter {
   public void process(ModuleInfo var1) throws WsBuildException {
      String var2 = var1.getJwsBuildContext().getDestEncoding();

      try {
         if (!var1.isWsdlOnly()) {
            if (var1.getWebServicesBean() != null) {
               writeBean(var1.getWebServicesBean(), new File(var1.getOutputDir(), "webservices.xml"), var2);
            }

            if (var1.getWeblogicWebservicesBean() != null) {
               writeBean(var1.getWeblogicWebservicesBean(), new File(var1.getOutputDir(), "weblogic-webservices.xml"), var2);
            }

            if (var1.getWebAppBean() != null) {
               writeBean(var1.getWebAppBean(), new File(var1.getOutputDir(), "web.xml"), var2);
               if (var1.getWeblogicWebAppBean() != null) {
                  writeBean(var1.getWeblogicWebAppBean(), new File(var1.getOutputDir(), "weblogic.xml"), var2);
               }
            }

            if (var1.getWebservicePolicyRefBean() != null) {
               writeBean(var1.getWebservicePolicyRefBean(), new File(var1.getOutputDir(), "weblogic-webservices-policy.xml"), var2);
            }
         }

      } catch (Exception var4) {
         throw new WsBuildException("Failed to write wsdl", var4);
      }
   }

   private static void writeBean(Object var0, File var1, String var2) throws IOException {
      assert var1 != null;

      assert var0 != null;

      DescriptorBean var3 = (DescriptorBean)var0;
      OutputStream var4 = IOUtil.createEncodedFileOutputStream(var1, var2);

      try {
         (new EditableDescriptorManager()).writeDescriptorAsXML(var3.getDescriptor(), var4, var2);
         var4.flush();
      } finally {
         var4.close();
      }

   }
}
