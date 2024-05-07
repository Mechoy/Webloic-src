package weblogic.servlet.jsp;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import weblogic.jsp.compiler.IJavelin;
import weblogic.jsp.compiler.client.ClientUtils;
import weblogic.jsp.wlw.util.filesystem.FS;
import weblogic.jsp.wlw.util.filesystem.mds.MDSFileSystem;
import weblogic.servlet.internal.RequestCallback;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.StringUtils;

public class ResourceProviderJavelinxJspStub extends JavelinxJSPStub {
   private static final String DELIMITER = "!";
   private String requestURI;

   public ResourceProviderJavelinxJspStub(String var1, String var2, Map var3, WebAppServletContext var4, JspConfig var5, String var6) {
      super(var1, var2, var3, var4, var5);
      this.requestURI = var6;
   }

   protected URI getFileToCompileURI(String var1) throws Exception {
      URI var2 = null;

      try {
         if (this.resourceProvider != null) {
            StringBuilder var3 = new StringBuilder("mds");
            var3.append(":");
            var3.append(this.requestURI);
            var3.append("!");
            var3.append(var1);
            var2 = new URI(var3.toString());
         }
      } catch (Exception var4) {
      }

      if (var2 == null) {
         throw new ServletException("Can't get resource URI because: 1.) JspResourceProvider is not initialized properly or 2.) there's URI syntax error in providerURI. ReqeustURI: " + this.requestURI + "providerURI: " + var1 + "JspResourceProvider: " + this.resourceProvider);
      } else {
         return var2;
      }
   }

   protected URI[] prepareDocRoots(String var1) throws IOException {
      ArrayList var2 = new ArrayList();
      var2.add(MDSFileSystem.DEFAULT_ROOT_URI);
      var2.addAll(Arrays.asList(super.prepareDocRoots(var1)));
      return (URI[])var2.toArray(new URI[var2.size()]);
   }

   protected String[] getSourcePaths(String var1) {
      String var2 = this.getContext().getResourceFinder("/").getClassPath();
      return StringUtils.splitCompletely(var2, File.pathSeparator);
   }

   protected void compilePage(RequestCallback var1) throws Exception {
      try {
         ((MDSFileSystem)FS.getFSFromScheme("mds")).storeJspResourceProvider(this.resourceProvider);
         super.compilePage(var1);
      } finally {
         ((MDSFileSystem)FS.getFSFromScheme("mds")).removeJspResourceProvider();
      }

   }

   protected IJavelin createJaveLin(boolean var1, boolean var2) {
      return ClientUtils.createCommandLineJavelin(var1, false, true);
   }
}
