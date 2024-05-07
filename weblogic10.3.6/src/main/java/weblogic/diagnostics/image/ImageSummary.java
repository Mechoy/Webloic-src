package weblogic.diagnostics.image;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.version;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.image.descriptor.FailedImageSourceBean;
import weblogic.diagnostics.image.descriptor.ImageSummaryBean;
import weblogic.diagnostics.image.descriptor.SuccessfulImageSourceBean;
import weblogic.diagnostics.image.descriptor.SystemPropertyBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;

class ImageSummary implements ImageSource {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean captureCancelled;
   private String imageName;
   private Date imageCreationDate;
   private Long imageCreationElapsedTime;
   private Map successfulImageSources = new HashMap();
   private Map failedImageSources = new HashMap();
   private ImageRequester imageRequester;
   private ImageSummaryBean root;

   ImageSummary(long var1, ImageRequester var3, String var4) {
      this.imageCreationDate = new Date(var1);
      this.imageRequester = var3;
      this.imageName = var4;
      this.setImageCreationElapsedTime(0L);
   }

   void setImageCreationElapsedTime(long var1) {
      this.imageCreationElapsedTime = new Long(var1);
   }

   void addSuccessfulImageSource(String var1, long var2) {
      this.successfulImageSources.put(var1, new Long(var2));
   }

   void addFailedImageSource(String var1, Exception var2) {
      this.failedImageSources.put(var1, var2);
   }

   void captureCancelled() {
      this.captureCancelled = true;
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      DescriptorManager var2 = new DescriptorManager();
      Descriptor var3 = var2.createDescriptorRoot(ImageSummaryBean.class);
      this.root = (ImageSummaryBean)var3.getRootBean();
      this.writeHeader();
      this.writeImageSourceElapsedTimes();
      this.writeFailedImageSources();
      this.writeImageRequester();

      try {
         var2.writeDescriptorBeanAsXML((DescriptorBean)this.root, var1);
      } catch (IOException var5) {
         throw new ImageSourceCreationException(var5);
      }
   }

   public void timeoutImageCreation() {
   }

   private void writeHeader() {
      this.root.setImageCaptureCancelled(this.captureCancelled);
      this.root.setImageCreationDate(this.imageCreationDate.toString());
      this.root.setImageCreationElapsedTime(this.imageCreationElapsedTime);
      this.root.setImageFileName(this.imageName);
      this.root.setMuxerClass(ManagementService.getRuntimeAccess(kernelId).getServer().getMuxerClass());
      this.root.setServerName(ManagementService.getRuntimeAccess(kernelId).getServer().getName());
      this.root.setServerReleaseInfo(version.getWebServerReleaseInfo());
      Iterator var1 = System.getProperties().entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         SystemPropertyBean var3 = this.root.createSystemProperty();
         var3.setName(var2.getKey().toString());
         var3.setValue(var2.getValue().toString());
      }

   }

   private void writeImageSourceElapsedTimes() {
      Set var1 = this.successfulImageSources.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Long var4 = (Long)this.successfulImageSources.get(var3);
         SuccessfulImageSourceBean var5 = this.root.createSuccessfulImageSource();
         var5.setImageSource(var3);
         var5.setImageCaptureElapsedTime(var4);
      }

   }

   private void writeFailedImageSources() {
      Set var1 = this.failedImageSources.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Throwable var4 = (Throwable)this.failedImageSources.get(var3);
         if (var4 != null) {
            String var5 = StackTraceUtils.throwable2StackTrace(var4);
            FailedImageSourceBean var6 = this.root.createFailedImageSource();
            var6.setImageSource(var3);
            var6.setFailureExceptionStackTrace(var5);
         }
      }

   }

   private void writeImageRequester() {
      this.root.setRequesterThreadName(this.imageRequester.getRequesterThreadName());
      this.root.setRequesterUserId(this.imageRequester.getRequesterUserId());
      this.root.setRequestStackTrace(this.imageRequester.getRequester());
   }
}
