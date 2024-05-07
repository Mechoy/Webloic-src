package weblogic.servlet.ejb2jsp.dd;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.servlet.ejb2jsp.Utils;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class FilesystemInfoDescriptor implements ToXML {
   private String javacPath;
   private String javacFlags;
   private String pkgname;
   private String ejbJarFile;
   private String saveAs;
   private String saveJarTmpdir;
   private String saveJarFile;
   private String saveDirClassDir;
   private String saveDirTldFile;
   private String[] compileClasspath;
   private String[] sourcePath;
   private boolean keepgenerated;
   private boolean compile = true;

   public FilesystemInfoDescriptor() {
      this.javacPath = this.javacFlags = this.pkgname = this.ejbJarFile = this.saveAs = this.saveJarTmpdir = this.saveJarFile = this.saveDirClassDir = this.saveDirTldFile = "";
      this.sourcePath = new String[0];
      this.compileClasspath = new String[0];
   }

   public FilesystemInfoDescriptor(Element var1) throws DOMProcessingException {
      Element var2 = null;
      this.javacPath = DOMUtils.getValueByTagName(var1, "javac-path");
      this.javacFlags = DOMUtils.getValueByTagName(var1, "javac-flags");
      this.compileClasspath = this.getPathElements(var1, "compile-classpath");
      String var3 = DOMUtils.getValueByTagName(var1, "keepgenerated");
      this.keepgenerated = "true".equalsIgnoreCase(var3);
      this.sourcePath = this.getPathElements(var1, "source-path");
      this.pkgname = DOMUtils.getValueByTagName(var1, "package-name");
      this.ejbJarFile = DOMUtils.getValueByTagName(var1, "ejb-jar-file");
      this.saveAs = DOMUtils.getValueByTagName(var1, "save-as");
      var2 = DOMUtils.getElementByTagName(var1, "save-taglib-jar");
      this.saveJarTmpdir = DOMUtils.getValueByTagName(var2, "tmpdir");
      this.saveJarFile = DOMUtils.getValueByTagName(var2, "taglib-jar-file");
      var2 = DOMUtils.getElementByTagName(var1, "save-taglib-directory");
      this.saveDirClassDir = DOMUtils.getValueByTagName(var2, "classes-directory");
      this.saveDirTldFile = DOMUtils.getValueByTagName(var2, "tld-file");
   }

   private String[] getPathElements(Element var1, String var2) throws DOMProcessingException {
      Element var3 = DOMUtils.getElementByTagName(var1, var2);
      List var4 = DOMUtils.getOptionalElementsByTagName(var3, "path-element");
      var4 = DOMUtils.getTextDataValues(var4);
      Iterator var5 = var4.iterator();
      ArrayList var6 = new ArrayList();

      while(var5.hasNext()) {
         var6.add(var5.next());
      }

      String[] var7 = new String[var6.size()];
      var6.toArray(var7);
      return var7;
   }

   public String toString() {
      return "Project Build Options";
   }

   private static String trim(String var0) {
      return var0 == null ? "" : var0.trim();
   }

   private static boolean isNull(String var0) {
      return var0 == null || var0.length() == 0;
   }

   public String[] getErrors() {
      this.javacPath = trim(this.javacPath);
      this.javacFlags = trim(this.javacFlags);
      this.pkgname = trim(this.pkgname);
      this.saveJarTmpdir = trim(this.saveJarTmpdir);
      this.saveJarFile = trim(this.saveJarFile);
      this.saveDirClassDir = trim(this.saveDirClassDir);
      this.saveDirTldFile = trim(this.saveDirTldFile);
      ArrayList var1 = new ArrayList();
      if (isNull(this.javacPath)) {
         var1.add("A java compiler (like \"javac\") must be specified, e.g., \"my.ejb.jsptags\"");
      }

      if (isNull(this.pkgname)) {
         var1.add("A java package for the generated code must be specified.");
      }

      if (this.saveAsDirectory()) {
         if (isNull(this.saveDirTldFile)) {
            var1.add("A file location for the TLD file must be specified, e.g., WEB-INF/myejb.tld");
         }

         if (isNull(this.saveDirClassDir)) {
            var1.add("A directory must be specified where the tag classes will be compiled, e.g., WEB-INF/classes");
         }
      } else {
         if (isNull(this.saveJarFile)) {
            var1.add("The target taglib jar file must be specified, e.g., WEB-INF/lib/myejb-tags.jar");
         }

         if (isNull(this.saveJarTmpdir)) {
            var1.add("A temporary compilation directory must be specified before packaging the jar file, e.g., C:\\TEMP or /tmp");
         }
      }

      String[] var2 = new String[var1.size()];
      var1.toArray(var2);
      return var2;
   }

   public String[] getCompileCommand() {
      ArrayList var1 = new ArrayList();
      if (this.getKeepgenerated()) {
         var1.add("-keepgenerated");
      }

      var1.add("-d");
      if (this.saveAsDirectory()) {
         var1.add(this.getSaveDirClassDir());
      } else {
         var1.add(this.getSaveJarTmpdir());
      }

      String[] var2 = new String[var1.size()];
      var1.toArray(var2);
      return var2;
   }

   public String getPackage() {
      return this.pkgname;
   }

   public void setPackage(String var1) {
      this.pkgname = var1;
      if (this.pkgname.endsWith(".")) {
         this.pkgname = this.pkgname.substring(0, this.pkgname.length() - 2);
      }

   }

   public String getJavacPath() {
      return this.javacPath;
   }

   public void setJavacPath(String var1) {
      this.javacPath = var1;
   }

   public String getJavacFlags() {
      return this.javacFlags;
   }

   public void setJavacFlags(String var1) {
      this.javacFlags = var1;
   }

   public String[] getCompileClasspath() {
      if (this.compileClasspath == null) {
         this.compileClasspath = new String[0];
      }

      return (String[])((String[])this.compileClasspath.clone());
   }

   public String[] getBuiltinClasspath() {
      return Utils.splitPath(System.getProperty("java.class.path") + File.pathSeparator + this.getEJBJarFile());
   }

   public void setCompileClasspath(String[] var1) {
      if (var1 == null) {
         this.compileClasspath = new String[0];
      } else {
         this.compileClasspath = (String[])((String[])var1.clone());
      }

   }

   public boolean getCompile() {
      return this.compile;
   }

   public void setCompile(boolean var1) {
      this.compile = var1;
   }

   public boolean getKeepgenerated() {
      return this.keepgenerated;
   }

   public void setKeepgenerated(boolean var1) {
      this.keepgenerated = var1;
   }

   public String[] getSourcePath() {
      if (this.sourcePath == null) {
         this.sourcePath = new String[0];
      }

      return (String[])((String[])this.sourcePath.clone());
   }

   public void setSourcePath(String[] var1) {
      this.sourcePath = (String[])((String[])var1.clone());
   }

   public String getEJBJarFile() {
      return this.ejbJarFile;
   }

   public void setEJBJarFile(String var1) {
      this.ejbJarFile = var1;
   }

   public String getSaveAs() {
      return this.saveAs;
   }

   public void setSaveAs(String var1) {
      this.saveAs = var1;
   }

   public boolean saveAsDirectory() {
      return !"JAR".equals(this.saveAs);
   }

   public String getSaveJarTmpdir() {
      return this.saveJarTmpdir;
   }

   public void setSaveJarTmpdir(String var1) {
      this.saveJarTmpdir = var1;
   }

   public String getSaveJarFile() {
      return this.saveJarFile;
   }

   public void setSaveJarFile(String var1) {
      this.saveJarFile = var1;
   }

   public String getSaveDirClassDir() {
      return this.saveDirClassDir;
   }

   public void setSaveDirClassDir(String var1) {
      this.saveDirClassDir = var1;
   }

   public String getSaveDirTldFile() {
      return this.saveDirTldFile;
   }

   public void setSaveDirTldFile(String var1) {
      this.saveDirTldFile = var1;
   }

   public void toXML(XMLWriter var1) {
      var1.println("<filesystem-info>");
      var1.incrIndent();
      var1.println("<javac-path>" + this.javacPath + "</javac-path>");
      var1.println("<javac-flags>" + this.javacFlags + "</javac-flags>");
      var1.println("<compile-classpath>");
      String[] var2 = this.getCompileClasspath();
      var1.incrIndent();

      int var3;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var1.println("<path-element>" + var2[var3] + "</path-element>");
      }

      var1.decrIndent();
      var1.println("</compile-classpath>");
      var1.println("<keepgenerated>" + this.keepgenerated + "</keepgenerated>");
      var1.println("<source-path>");
      var2 = this.getSourcePath();
      var1.incrIndent();

      for(var3 = 0; var3 < var2.length; ++var3) {
         var1.println("<path-element>" + var2[var3] + "</path-element>");
      }

      var1.decrIndent();
      var1.println("</source-path>");
      var1.println("<package-name>" + this.pkgname + "</package-name>");
      var1.println("<ejb-jar-file>" + this.ejbJarFile + "</ejb-jar-file>");
      var1.println("<save-as>" + this.saveAs + "</save-as>");
      var1.println("<save-taglib-jar>");
      var1.incrIndent();
      var1.println("<tmpdir>" + this.saveJarTmpdir + "</tmpdir>");
      var1.println("<taglib-jar-file>" + this.saveJarFile + "</taglib-jar-file>");
      var1.decrIndent();
      var1.println("</save-taglib-jar>");
      var1.println("<save-taglib-directory>");
      var1.incrIndent();
      var1.println("<classes-directory>" + this.saveDirClassDir + "</classes-directory>");
      var1.println("<tld-file>" + this.saveDirTldFile + "</tld-file>");
      var1.decrIndent();
      var1.println("</save-taglib-directory>");
      var1.decrIndent();
      var1.println("</filesystem-info>");
   }
}
