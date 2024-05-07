package weblogic.jms.extensions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.ClientSAFBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class ClientSAFParser {
   public static ClientSAFBean createClientSAFDescriptor(String var0) throws IOException, XMLStreamException {
      if (var0 == null) {
         throw new IOException("Null URI specified");
      } else {
         AbstractDescriptorLoader2 var1 = new AbstractDescriptorLoader2(new File(var0), (File)null, (DeploymentPlanBean)null, (String)null, var0) {
            protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
               String var2 = "weblogic.j2ee.descriptor.wl.ClientSAFBeanImpl$SchemaHelper2";
               return new VersionMunger(var1, this, var2, "http://xmlns.oracle.com/weblogic/weblogic-jms");
            }
         };
         return (ClientSAFBean)var1.loadDescriptorBean();
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         usage();
      } else {
         String var1 = var0[0];
         ClientSAFBean var2 = null;

         try {
            System.out.println("\n\n... getting JMSBean from JMSMD:\n\n");
            var2 = createClientSAFDescriptor(var1);
            if (var2 != null) {
               DescriptorUtils.writeAsXML((DescriptorBean)var2);
            }
         } catch (Throwable var6) {
            int var4 = 0;

            for(Throwable var5 = var6; var5 != null; var5 = var5.getCause()) {
               System.out.println("\nERROR: run threw an exception: level " + var4);
               ++var4;
               var5.printStackTrace();
            }
         }

      }
   }

   private static void usage() {
      System.err.println("usage: java weblogic.jms.extensions.ClientSAFParser file-path");
   }
}
