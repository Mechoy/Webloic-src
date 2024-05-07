package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.JspDescriptorMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WLJspDescriptor extends BaseServletDescriptor implements ToXML, JspDescriptorMBean {
   private static final String JSP_PARAM = "jsp-param";
   private List jspParams;

   public WLJspDescriptor() {
      this.jspParams = new ArrayList();
   }

   public WLJspDescriptor(List var1) {
      this.jspParams = var1;
   }

   public WLJspDescriptor(JspDescriptorMBean var1) {
      this();
      if (var1 != null) {
         this.setDefaultFileName(var1.getDefaultFileName());
         this.setCompileCommand(var1.getCompileCommand());
         this.setPrecompile(var1.isPrecompile());
         this.setPrecompile(var1.isPrecompileContinue());
         this.setCompilerClass(var1.getCompilerClass());
         this.setCompileFlags(var1.getCompileFlags());
         this.setWorkingDir(var1.getWorkingDir());
         this.setVerbose(var1.isVerbose());
         this.setCompilerSupportsEncoding(var1.getCompilerSupportsEncoding());
         this.setKeepgenerated(var1.isKeepgenerated());
         this.setPageCheckSeconds(var1.getPageCheckSeconds());
         this.setEncoding(var1.getEncoding());
         this.setPackagePrefix(var1.getPackagePrefix());
         this.setSuperclass(var1.getSuperclass());
         this.setNoTryBlocks(var1.isNoTryBlocks());
         this.setDebugEnabled(var1.isDebugEnabled());
         this.setBackwardCompatible(var1.isBackwardCompatible());
         this.setPrintNulls(var1.getPrintNulls());
         this.setJspServlet(var1.getJspServlet());
         this.setJspPrecompiler(var1.getJspPrecompiler());
      }

   }

   public WLJspDescriptor(Element var1) throws DOMProcessingException {
      List var2 = DOMUtils.getOptionalElementsByTagName(var1, "jsp-param");
      Iterator var3 = var2.iterator();
      this.jspParams = new ArrayList(var2.size());

      while(var3.hasNext()) {
         ParameterDescriptor var5 = new ParameterDescriptor((Element)var3.next());
         String var6 = var5.getParamName();
         if (!isValidParam(var6)) {
            if (var6 != null && var6.length() == 0) {
               HTTPLogger.logEmptyJspParamName();
            } else {
               HTTPLogger.logInvalidJspParamName(var6);
            }

            throw new DOMProcessingException();
         }

         this.jspParams.add(var5);
      }

   }

   private static boolean isValidParam(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.equalsIgnoreCase("defaultFileName") || var0.equalsIgnoreCase("compileCommand") || var0.equalsIgnoreCase("compileFlags") || var0.equalsIgnoreCase("compilerclass") || var0.equalsIgnoreCase("workingDir") || var0.equalsIgnoreCase("verbose") || var0.equalsIgnoreCase("keepgenerated") || var0.equalsIgnoreCase("compilerSupportsEncoding") || var0.equalsIgnoreCase("pageCheckSeconds") || var0.equalsIgnoreCase("encoding") || var0.equalsIgnoreCase("packagePrefix") || var0.equalsIgnoreCase("noTryBlocks") || var0.equalsIgnoreCase("precompile") || var0.equalsIgnoreCase("precompileContinue") || var0.equalsIgnoreCase("exactMapping") || var0.equalsIgnoreCase("superclass") || var0.equalsIgnoreCase("debug") || var0.equalsIgnoreCase("backwardCompatible") || var0.equalsIgnoreCase("printNulls") || var0.equalsIgnoreCase("jspServlet") || var0.equalsIgnoreCase("jspPrecompiler");
      }
   }

   private static boolean isDefaultValue(String var0, String var1) {
      if (var0 == null) {
         return false;
      } else {
         return var0.equalsIgnoreCase("verbose") && var1.equals("true") || var0.equalsIgnoreCase("keepgenerated") && var1.equals("false") || var0.equalsIgnoreCase("compilerSupportsEncoding") && var1.equals("true") || var0.equalsIgnoreCase("pageCheckSeconds") && var1.equals("1") || var0.equalsIgnoreCase("packagePrefix") && var1.equals("jsp_servlet") || var0.equalsIgnoreCase("noTryBlocks") && var1.equals("false") || var0.equalsIgnoreCase("precompile") && var1.equals("false") || var0.equalsIgnoreCase("precompileContinue") && var1.equals("false") || var0.equalsIgnoreCase("exactMapping") && var1.equals("true") || var0.equalsIgnoreCase("superclass") && var1.equals("weblogic.servlet.jsp.JspBase") || var0.equalsIgnoreCase("debug") && var1.equals("false") || var0.equalsIgnoreCase("backwardCompatible") && var1.equals("false") || var0.equalsIgnoreCase("printNulls") && var1.equals("true");
      }
   }

   public String getDefaultFileName() {
      return this.getProp("defaultFilename", "index.jsp");
   }

   public void setDefaultFileName(String var1) {
      String var2 = this.getDefaultFileName();
      this.setProp("defaultFilename", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("defaultFilename", var2, var1);
      }

   }

   public boolean isNoTryBlocks() {
      return "true".equalsIgnoreCase(this.getProp("noTryBlocks", "false"));
   }

   public void setNoTryBlocks(boolean var1) {
      boolean var2 = this.isNoTryBlocks();
      this.setProp("noTryBlocks", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("noTryBlocks", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isPrecompile() {
      boolean var1 = "true".equalsIgnoreCase(this.getProp("precompile", "false"));
      return var1;
   }

   public void setPrecompile(boolean var1) {
      boolean var2 = this.isPrecompile();
      this.setProp("precompile", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("precompile", new Boolean(var2), new Boolean(var1));
      }

   }

   public boolean isPrecompileContinue() {
      boolean var1 = "true".equalsIgnoreCase(this.getProp("precompileContinue", "false"));
      return var1;
   }

   public void setPrecompileContinue(boolean var1) {
      boolean var2 = this.isPrecompileContinue();
      this.setProp("precompileContinue", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("precompileContinue", new Boolean(var2), new Boolean(var1));
      }

   }

   public boolean isExactMapping() {
      boolean var1 = "true".equalsIgnoreCase(this.getProp("exactMapping", "true"));
      return var1;
   }

   public void setExactMapping(boolean var1) {
      boolean var2 = this.isExactMapping();
      this.setProp("exactMapping", "" + var1);
      if (var1 != var2) {
         this.firePropertyChange("exactMapping", new Boolean(var2), new Boolean(var1));
      }

   }

   public String getSuperclass() {
      return this.getProp("superclass", "weblogic.servlet.jsp.JspBase");
   }

   public void setSuperclass(String var1) {
      String var2 = this.getSuperclass();
      this.setProp("superclass", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("superclass", var2, var1);
      }

   }

   public String getCompileCommand() {
      return this.getProp("compileCommand", (String)null);
   }

   public void setCompileCommand(String var1) {
      String var2 = this.getCompileCommand();
      this.setProp("compileCommand", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("compileCommand", var2, var1);
      }

   }

   public String getCompileFlags() {
      return this.getProp("compileFlags", (String)null);
   }

   public void setCompileFlags(String var1) {
      String var2 = this.getCompileFlags();
      this.setProp("compileFlags", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("compileFlags", var2, var1);
      }

   }

   public String getCompilerClass() {
      return this.getProp("compilerclass", (String)null);
   }

   public void setCompilerClass(String var1) {
      String var2 = this.getCompilerClass();
      this.setProp("compilerclass", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("compilerclass", var2, var1);
      }

   }

   public String getWorkingDir() {
      return this.getProp("workingDir", (String)null);
   }

   public void setWorkingDir(String var1) {
      if (var1 != null && (var1 = var1.trim()).length() > 0) {
         String var2 = this.getWorkingDir();
         this.setProp("workingDir", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("workingDir", var2, var1);
         }
      }

   }

   public String getEncoding() {
      return this.getProp("encoding", (String)null);
   }

   public void setEncoding(String var1) {
      if (var1 != null && (var1 = var1.trim()).length() > 0) {
         String var2 = this.getEncoding();
         this.setProp("encoding", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("encoding", var2, var1);
         }
      }

   }

   public boolean getCompilerSupportsEncoding() {
      return !"false".equalsIgnoreCase(this.getProp("compilerSupportsEncoding", "true"));
   }

   public void setCompilerSupportsEncoding(boolean var1) {
      boolean var2 = this.getCompilerSupportsEncoding();
      this.setProp("compilerSupportsEncoding", "" + var1);
      if (var1 != var2) {
         this.firePropertyChange("compilerSupportsEncoding", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean getVerbose() {
      return this.isVerbose();
   }

   public boolean isVerbose() {
      return !"false".equalsIgnoreCase(this.getProp("verbose", "true"));
   }

   public void setVerbose(boolean var1) {
      boolean var2 = this.isVerbose();
      this.setProp("verbose", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("verbose", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean getKeepgenerated() {
      return this.isKeepgenerated();
   }

   public boolean isKeepgenerated() {
      return "true".equalsIgnoreCase(this.getProp("keepgenerated", "false"));
   }

   public void setKeepgenerated(boolean var1) {
      boolean var2 = this.isKeepgenerated();
      this.setProp("keepgenerated", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("keepgenerated", new Boolean(!var1), new Boolean(var1));
      }

   }

   public int getPageCheckSeconds() {
      String var1 = this.getProp("pageCheckSeconds", "1");

      try {
         return Integer.parseInt(var1.trim());
      } catch (NumberFormatException var3) {
         return 1;
      }
   }

   public void setPageCheckSeconds(int var1) {
      int var2 = this.getPageCheckSeconds();
      this.setProp("pageCheckSeconds", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("pageCheckSeconds", new Integer(var2), new Integer(var1));
      }

   }

   public String getPackagePrefix() {
      return this.getProp("packagePrefix", "jsp_servlet");
   }

   public void setPackagePrefix(String var1) {
      if (var1 != null && (var1 = var1.trim()).length() > 0) {
         String var2 = this.getPackagePrefix();
         this.setProp("packagePrefix", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("packagePrefix", var2, var1);
         }
      }

   }

   public void setDebugEnabled(boolean var1) {
      if (var1 != this.isDebugEnabled()) {
         this.setProp("debug", "" + var1);
         this.firePropertyChange("debug", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isDebugEnabled() {
      return "true".equalsIgnoreCase(this.getProp("debug", "false"));
   }

   public boolean getDebugEnabled() {
      return this.isDebugEnabled();
   }

   public void setBackwardCompatible(boolean var1) {
      if (var1 != this.isBackwardCompatible()) {
         this.setProp("backwardCompatible", "" + var1);
         this.firePropertyChange("backwardCompatible", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isBackwardCompatible() {
      return "true".equalsIgnoreCase(this.getProp("backwardCompatible", "false"));
   }

   public boolean getBackwardCompatible() {
      return this.isBackwardCompatible();
   }

   public void setPrintNulls(boolean var1) {
      if (var1 != this.getPrintNulls()) {
         this.setProp("printNulls", "" + var1);
         this.firePropertyChange("printNulls", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean getPrintNulls() {
      return !"false".equalsIgnoreCase(this.getProp("printNulls", "true"));
   }

   public String getJspServlet() {
      return this.getProp("jspServlet", "");
   }

   public void setJspServlet(String var1) {
      String var2 = this.getJspServlet();
      this.setProp("jspServlet", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("jspServlet", var2, var1);
      }

   }

   public String getJspPrecompiler() {
      return this.getProp("jspPrecompiler", "");
   }

   public void setJspPrecompiler(String var1) {
      String var2 = this.getJspPrecompiler();
      this.setProp("jspPrecompiler", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("jspPrecompiler", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   private void setProp(String var1, String var2) {
      Iterator var3 = this.jspParams.iterator();

      ParameterDescriptor var4;
      do {
         if (!var3.hasNext()) {
            if (var2 != null) {
               var4 = new ParameterDescriptor();
               var4.setParamName(var1);
               var4.setParamValue(var2);
               this.jspParams.add(var4);
            }

            return;
         }

         var4 = (ParameterDescriptor)var3.next();
      } while(!var1.equalsIgnoreCase(var4.getParamName()));

      if (var2 == null) {
         this.jspParams.remove(var4);
      } else {
         var4.setParamName(var1);
         var4.setParamValue(var2);
      }

   }

   private String getProp(String var1, String var2) {
      Iterator var3 = this.jspParams.iterator();

      ParameterDescriptor var4;
      do {
         if (!var3.hasNext()) {
            if (var2 != null && var2.length() > 0) {
               var4 = new ParameterDescriptor();
               var4.setParamName(var1);
               var4.setParamValue(var2);
               this.jspParams.add(var4);
            }

            return var2;
         }

         var4 = (ParameterDescriptor)var3.next();
      } while(!var1.equalsIgnoreCase(var4.getParamName()));

      var4.setParamName(var1);
      return var4.getParamValue();
   }

   public String toXML(int var1) {
      String var2 = "";
      boolean var3 = false;
      if (this.jspParams != null && this.jspParams.size() > 0) {
         Iterator var4 = this.jspParams.iterator();

         while(var4.hasNext()) {
            ParameterDescriptor var5 = (ParameterDescriptor)var4.next();
            if (!isDefaultValue(var5.getParamName(), var5.getParamValue())) {
               if (!var3) {
                  var2 = var2 + this.indentStr(var1) + "<jsp-descriptor>\n";
                  var1 += 2;
                  var3 = true;
               }

               var2 = var2 + this.indentStr(var1) + "<jsp-param>\n";
               var1 += 2;
               var2 = var2 + this.indentStr(var1) + "<param-name>" + var5.getParamName() + "</param-name>\n";
               var2 = var2 + this.indentStr(var1) + "<param-value>" + var5.getParamValue() + "</param-value>\n";
               var1 -= 2;
               var2 = var2 + this.indentStr(var1) + "</jsp-param>\n";
            }
         }

         if (var3) {
            var1 -= 2;
            var2 = var2 + this.indentStr(var1) + "</jsp-descriptor>\n";
         }
      }

      return var2;
   }
}
