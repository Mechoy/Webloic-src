package weblogic.servlet.jsp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.descriptor.DescriptorBean;
import weblogic.html.EntityEscape;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.jsp.compiler.DiagnosticList;
import weblogic.jsp.compiler.IApplication;
import weblogic.jsp.compiler.ICPL;
import weblogic.jsp.compiler.IJavelin;
import weblogic.jsp.compiler.IJavelinFile;
import weblogic.jsp.compiler.Diagnostic.Severity;
import weblogic.jsp.compiler.client.ClientUtils;
import weblogic.jsp.compiler.jsp.IJspCompilerOptions;
import weblogic.jsp.internal.html.HtmlLanguageX;
import weblogic.jsp.internal.jsp.JspLanguageX;
import weblogic.jsp.internal.jsp.options.JspCompilerOptionsX;
import weblogic.jsp.internal.jsp.utils.JavaTransformUtils;
import weblogic.jsp.languages.java.IJavaLanguage;
import weblogic.jsp.wlw.filesystem.IFile;
import weblogic.jsp.wlw.util.filesystem.FS;
import weblogic.servlet.internal.RequestCallback;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.Source;

public class JavelinxJSPStub extends JspStub {
   private static final boolean verboseload = Boolean.getBoolean("weblogic.jsp.javelin.verboseload");

   public JavelinxJSPStub(String var1, String var2, Map var3, WebAppServletContext var4, JspConfig var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected URI getFileToCompileURI(String var1) throws Exception {
      WarSource var2 = this.getContext().getResourceAsSource(var1);
      if (var2 == null) {
         throw new JspFileNotFoundException("no resource '" + var1 + "' in servlet context root '" + this.getContext().getDocroot() + "'");
      } else {
         return getAbsoluteJspURI(var2, var1);
      }
   }

   protected URI[] prepareDocRoots(String var1) throws IOException {
      ArrayList var2 = new ArrayList();
      String[] var3 = this.getSourcePaths(var1);
      if (var3 != null && var3.length > 0) {
         IFile[] var8 = new IFile[var3.length];

         for(int var7 = 0; var7 < var3.length; ++var7) {
            File var6 = (new File(var3[var7])).getCanonicalFile();
            var8[var7] = FS.getIFile(var6);
            if (var8[var7] != null) {
               var2.add(var8[var7].getURI());
            }
         }
      } else {
         String var4 = this.getContext().getDocroot();
         IFile var5 = FS.getIFile(new File(var4));
         if (var5 != null) {
            var2.add(var5.getURI());
         }
      }

      return (URI[])var2.toArray(new URI[var2.size()]);
   }

   protected String[] getSourcePaths(String var1) {
      WarSource var2 = this.getContext().getResourceAsSource(var1);
      if (var2 != null && "file".equals(var2.getURL().getProtocol())) {
         String var3 = this.getContext().getResourceFinder(var1).getClassPath();
         return StringUtils.splitCompletely(var3, File.pathSeparator);
      } else {
         return null;
      }
   }

   protected void compilePage(RequestCallback var1) throws Exception {
      String var2 = this.getRequestURI(var1);
      URI var3 = this.getFileToCompileURI(var2);
      URI[] var4 = this.prepareDocRoots(var2);
      WebAppBean var5 = this.getContext().getWebAppModule().getWebAppBean();
      String var6 = ((DescriptorBean)var5).getDescriptor().getOriginalVersionInfo();
      IJavelin var7 = this.prepareJavelin(var6);
      JspCLLManager.JspClientContext var8 = new JspCLLManager.JspClientContext();
      IApplication var9 = var7.createApplication("JSPStubx", var8);
      ICPL var10 = var9.createCPL("CPL");

      try {
         IJspCompilerOptions var11 = JspCompilerOptionsX.createFeature(var10);
         if (this.getContext().getConfigManager().useDefaultEncoding() && this.jsps.getEncoding() == null) {
            this.jsps.setEncoding(this.getContext().getConfigManager().getDefaultEncoding());
         }

         JspCLLManager.setUpCompilerOptions(var11, this.jsps);
         JspCLLManager.makeConfigFeature(var5, var10, this.getContext().getJSPManager().getJspExtensions());
         this.prepareCPLClasspath(var10);
         var10.setClassLoader(this.getContext().getTagFileHelper().getTagFileClassLoader());
         JspCLLManager.setWebAppProjectFeature(var10, this.getContext().getContextPath(), var4, var6, JspCLLManager.getVirtualRootsInfo(this.getContext().getWebAppModule().getWlWebAppBean()), this.resourceProvider);
         HashSet var12 = new HashSet();
         IFile var13 = FS.getIFile(var3);
         var12.add(var13);
         var10.addSourceFiles(var12);
         Set var20 = var10.getSourceFiles();
         HashSet var14 = new HashSet();
         Map var15 = this.prepareBuildOptions();
         IFile var16 = FS.getIFile(new File(this.jsps.getWorkingDir()));

         while(var20.size() > 0) {
            var14.addAll(var20);
            ClientUtils.get(var9).build(var10, var20, var16, var15);
            var20.addAll(var10.getSourceFiles());
            var20.removeAll(var14);
         }

         ClientUtils.get(var9).buildPrototypes(var10, var16, var15);
         this.reportCompilationErrorIfNeccessary(var10, var8, var1, var2, var14);
      } finally {
         ClientUtils.get(var9).closeAndWait();
      }
   }

   private void reportCompilationErrorIfNeccessary(ICPL var1, JspCLLManager.JspClientContext var2, RequestCallback var3, String var4, Set var5) throws IOException {
      StringBuilder var6 = this.getInternalErrors(var2);
      StringBuffer var7 = null;
      StringBuffer var8 = null;
      Map var9 = var1.getFilesWithDiagnostics();
      if (var6 != null || var9 != null && var9.size() > 0) {
         var7 = new StringBuffer();
         var8 = new StringBuffer();
         boolean var10 = false;
         if (var6 != null) {
            this.appendErrorMessageHeader(var8, var4);
            var7.append(var6);
            var10 = true;
         }

         if (var9 != null && var9.size() > 0) {
            Iterator var11 = var9.entrySet().iterator();

            while(var11.hasNext()) {
               Map.Entry var12 = (Map.Entry)var11.next();
               IJavelinFile var13 = (IJavelinFile)var12.getKey();
               if (!var13.isBinary() && var12.getValue() != null && var5.contains(var13)) {
                  DiagnosticList var14 = (DiagnosticList)var12.getValue();
                  List var15 = var14.get(true, Severity.ERROR);
                  int var16 = var15.size();
                  if (var16 > 0) {
                     if (!var10) {
                        var10 = true;
                        this.appendErrorMessageHeader(var8, var4);
                     }

                     StringWriter var17 = new StringWriter();
                     PrintWriter var18 = new PrintWriter(var17);
                     var14.print(true, JavaTransformUtils.isDiagnosticWithAbsolutePath ? var13.getIFile().getDisplayPath() : var13.getName(), var18, true, Severity.ERROR);
                     var7.append(var17.toString());
                  }
               }
            }
         }

         if (var10) {
            String var19 = var7.toString();
            EntityEscape.escapeString(var19, var8);
            var8.append("</pre>\n</body></html>\n");
            String var20 = var8.toString();
            this.getContext().log(var19);
            CompilationException var21 = new CompilationException("Failed to compile JSP " + var4, var4, var19, var20, (Throwable)null);
            if (this.jsps.isVerbose() && !this.isErrorPageDefined(500) && !this.isErrorPageDefined(var21)) {
               var3.reportJSPCompilationFailure(var19, var20);
            }

            this.destroy();
            throw var21;
         }
      }

   }

   private Map prepareBuildOptions() {
      HashMap var1 = new HashMap();
      if (this.jsps.isKeepGenerated()) {
         var1.put("-keepGenerated", "true");
         var1.put("-s", (new File(this.jsps.getWorkingDir())).getAbsolutePath());
      }

      return var1;
   }

   private StringBuilder getInternalErrors(JspCLLManager.JspClientContext var1) {
      StringBuilder var2 = null;
      if (var1.hasException()) {
         var2 = new StringBuilder("Exception occurred while processing '");
         var2.append(var1.getPath());
         var2.append("'");
         Throwable var3 = var1.getException();
         if (var3 != null) {
            var2.append(StackTraceUtils.throwable2StackTrace(var3));
         }
      }

      return var2;
   }

   private void prepareCPLClasspath(ICPL var1) {
      ArrayList var2 = new ArrayList();
      this.addPaths(var2, this.getContext().getFullClasspath());
      var1.initializeBinaryPaths(var2);
   }

   private IJavelin prepareJavelin(String var1) {
      IJavelin var2 = this.createJaveLin(verboseload, false);
      var2.addLanguage(new JspLanguageX(var2));
      var2.addLanguage(new HtmlLanguageX(var2));
      float var3 = 0.0F;
      if (var1 != null) {
         try {
            var3 = Float.parseFloat(var1);
         } catch (NumberFormatException var5) {
         }
      }

      IJavaLanguage var4 = (IJavaLanguage)var2.getLanguage("java");
      if (this.jsps.isDebugEnabled()) {
         var4.setEmitDebug(7);
      }

      if ((double)var3 < 2.4 && var4 != null) {
         var4.setJSPBackCompat(true);
      }

      return var2;
   }

   protected IJavelin createJaveLin(boolean var1, boolean var2) {
      return ClientUtils.createCommandLineJavelin(var1, false, false);
   }

   private void appendErrorMessageHeader(StringBuffer var1, String var2) {
      var1.append("<html>\n<head>\n<title>Weblogic JSP compilation error</title>\n</head>\n<body>\n");
      var1.append("<b>Compilation of JSP File '");
      var1.append(var2);
      var1.append("' <font color=#FF0000>failed</font>:</b><HR><pre>\n");
   }

   private boolean isErrorPageDefined(int var1) {
      return this.getContext().getErrorManager().getErrorLocation(String.valueOf(var1)) != null;
   }

   private boolean isErrorPageDefined(Throwable var1) {
      return this.getContext().getErrorManager().getExceptionLocation(var1) != null;
   }

   private void addPaths(List var1, String var2) {
      String[] var3 = StringUtils.splitCompletely(var2, File.pathSeparator);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         File var5 = new File(var3[var4]);
         if (var5.exists()) {
            var1.add(FS.getIFile(var5));
         }
      }

   }

   static URI getAbsoluteJspURI(Source var0, String var1) throws CompilationException {
      URL var2 = var0.getURL();
      String var3 = var2.getProtocol();
      if ("file".equals(var3)) {
         return (new File(var2.getPath())).toURI();
      } else {
         String var4;
         if ("zip".equals(var3)) {
            var4 = var2.toString();
            int var5 = var4.indexOf("!/");
            String var6;
            if (var5 == -1) {
               var6 = "JSP source=" + var4 + " can't be loaded";
               throw new CompilationException(var6, var1, "", "", (Throwable)null);
            } else {
               var6 = var4.substring("zip:".length());
               if (var6.charAt(0) != '/') {
                  var6 = "/" + var6;
               }

               try {
                  return new URI("war:file:" + var6);
               } catch (URISyntaxException var9) {
                  String var8 = "JSP source URI syntax error: URI: " + var6 + ", error: " + var9.getMessage();
                  throw new CompilationException(var8, var1, "", "", (Throwable)null);
               }
            }
         } else {
            var4 = "JSP source in an unsupported form: URL=" + var0.getCodeSourceURL() + " Source class=" + var0.getClass().getName();
            throw new CompilationException(var4, var1, "", "", (Throwable)null);
         }
      }
   }

   static {
      FS.initializeForJSP();
   }
}
