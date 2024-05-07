package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.internal.ApplicationContextImpl;
import weblogic.application.io.Ear;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.utils.classloaders.ClassFinder;

public class BuildtimeApplicationContext extends ApplicationContextImpl implements ApplicationContextInternal {
   private final CompilerCtx ctx;
   private final Map uriLinks = new HashMap();

   BuildtimeApplicationContext(CompilerCtx var1) {
      super(var1.getSourceName(), var1.getSourceFile(), Thread.currentThread().getContextClassLoader());
      this.ctx = var1;
   }

   public ApplicationBean getApplicationDD() {
      return this.ctx.getApplicationDD();
   }

   public void registerLink(File var1) throws IOException {
      this.registerLink(var1.getName(), var1);
   }

   public void registerLink(String var1, File var2) throws IOException {
      EarUtils.linkURI(this.ctx.getEar(), this.getApplicationFileManager(), var1, var2);
      this.uriLinks.put(var1, var2);
   }

   public ApplicationDescriptor getApplicationDescriptor() {
      return this.ctx.getApplicationDescriptor();
   }

   public File getURILink(String var1) {
      return (File)this.uriLinks.get(var1);
   }

   public boolean isLibraryURI(String var1) {
      return this.uriLinks.containsKey(var1);
   }

   public ClassFinder getClassFinder() {
      return this.getAppClassLoader().getClassFinder();
   }

   public void notifyDescriptorUpdate() throws LoggableLibraryProcessingException {
      LibraryUtils.resetAppDDs(this.ctx.getApplicationDescriptor(), this.ctx);
   }

   public Ear getEar() {
      return this.ctx.getEar();
   }

   public SubDeploymentMBean[] getLibrarySubDeployments() {
      return null;
   }
}
