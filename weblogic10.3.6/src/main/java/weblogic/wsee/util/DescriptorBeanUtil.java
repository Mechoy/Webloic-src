package weblogic.wsee.util;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorException;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.servlet.internal.WebAppDescriptor;

public class DescriptorBeanUtil {
   private DescriptorBeanUtil() {
   }

   public static WeblogicApplicationBean loadWeblogicAppBean(File var0) throws IOException {
      if (!var0.exists()) {
         return null;
      } else {
         try {
            return (WeblogicApplicationBean)(new ApplicationDescriptor.MyWlsApplicationDescriptor(var0)).loadDescriptorBean();
         } catch (Exception var2) {
            throw new IOException("The existing descriptor at " + var0.getAbsolutePath() + " is corrupted.\n" + var2.getMessage());
         }
      }
   }

   public static List validateBean(File var0) throws IOException {
      try {
         loadWebDescriptor(var0, true);
      } catch (DescriptorException var2) {
         return var2.getErrorList();
      }

      return Collections.EMPTY_LIST;
   }

   public static DescriptorBean loadWebDescriptor(File var0) throws IOException {
      return loadWebDescriptor(var0, false);
   }

   private static DescriptorBean loadWebDescriptor(File var0, boolean var1) throws IOException {
      if (var0.getName().endsWith("web.xml")) {
         return loadWebBean(var0, var1);
      } else if (var0.getName().endsWith("weblogic.xml")) {
         return loadWebLogicBean(var0, var1);
      } else {
         throw new AssertionError(" loadWebDescriptor called with a non web descriptor: " + var0.getAbsolutePath());
      }
   }

   private static DescriptorBean loadWebBean(File var0, boolean var1) throws IOException {
      WebAppDescriptor var2 = new WebAppDescriptor(var0, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
      var2.setValidateSchema(var1);

      try {
         return (DescriptorBean)var2.getWebAppBean();
      } catch (XMLStreamException var4) {
         throw new IOException("Exception parsing web.xml: " + var4);
      }
   }

   private static DescriptorBean loadWebLogicBean(File var0, boolean var1) throws IOException {
      WebAppDescriptor var2 = new WebAppDescriptor((File)null, var0, (File)null, (DeploymentPlanBean)null, (String)null);
      var2.setValidateSchema(var1);

      try {
         return (DescriptorBean)var2.getWeblogicWebAppBean();
      } catch (XMLStreamException var4) {
         throw new IOException("Exception parsing weblogic.xml: " + var4);
      }
   }
}
