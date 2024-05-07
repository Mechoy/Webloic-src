package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.internal.WlsExtensionReader;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.utils.jars.VirtualJarFile;

public class ExtensionDescriptorParser extends AbstractDescriptorLoader2 {
   private WeblogicExtensionBean wlExtBean = null;
   private boolean parsed = false;
   private final String extDescritorUri;
   private final String appName;

   public ExtensionDescriptorParser(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.extDescritorUri = var5;
      this.appName = var4;
   }

   public ExtensionDescriptorParser(InputStream var1) {
      super(var1);
      this.extDescritorUri = "";
      this.appName = "";
   }

   public WeblogicExtensionBean getWlExtensionDescriptor() throws IOException, XMLStreamException {
      if (!this.parsed) {
         this.wlExtBean = (WeblogicExtensionBean)this.loadDescriptorBean();
         this.parsed = true;
      }

      return this.wlExtBean;
   }

   protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
      return new WlsExtensionReader(var1, this);
   }

   public void mergeWlExtensionDescriptorsFromLibraries(ApplicationContextInternal var1) throws IOException, XMLStreamException {
      VirtualJarFile[] var2 = null;

      try {
         var2 = LibraryUtils.getLibraryVjarsWithDescriptor(var1, this.appName, this.extDescritorUri);
         WeblogicExtensionBean var3 = this.getWlExtensionDescriptor();
         if (var2.length > 0) {
            ExtensionDescriptorParser var4 = this;
            if (var3 == null) {
               var4 = new ExtensionDescriptorParser(var2[0], this.getConfigDir(), this.getDeploymentPlan(), this.appName, this.extDescritorUri);
               var3 = (WeblogicExtensionBean)var4.loadDescriptorBean();
               var2[0].close();
               if (var2.length > 1) {
                  VirtualJarFile[] var5 = new VirtualJarFile[var2.length - 1];
                  System.arraycopy(var2, 1, var5, 0, var2.length - 1);
                  var2 = var5;
               } else {
                  var2 = new VirtualJarFile[0];
               }
            }

            if (var2.length > 0) {
               var3 = (WeblogicExtensionBean)var4.mergeDescriptors(var2);
            }
         }

         this.wlExtBean = var3;
      } finally {
         IOUtils.forceClose(var2);
      }

   }
}
