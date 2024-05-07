package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.StringUtil;

class JwsCompileList {
   private static final String JAVA_PATTERN = "**/*.java";
   private final JwsModule module;
   private final List<File> sourcepaths;

   JwsCompileList(JwsModule var1) {
      assert var1 != null;

      this.module = var1;
      this.sourcepaths = getSourcepaths(var1);
   }

   File[] getFiles() throws IOException {
      HashSet var1 = new HashSet();
      this.addJwsSources(var1);
      this.addDependentSources(var1);
      this.addGeneratedSources(var1);
      this.addFileSetSources(var1);
      return (File[])var1.toArray(new File[var1.size()]);
   }

   private void addJwsSources(Set<File> var1) {
      Iterator var2 = this.module.getJwsFiles().iterator();

      while(var2.hasNext()) {
         Jws var3 = (Jws)var2.next();
         File var4 = var3.getAbsoluteFile();
         var1.add(var4);
      }

   }

   private void addDependentSources(Set<File> var1) {
      Iterator var2 = this.module.getWebServices().iterator();

      while(var2.hasNext()) {
         WebServiceDecl var3 = (WebServiceDecl)var2.next();
         this.addDependentSources(var3, var1);
      }

   }

   private void addDependentSources(WebServiceDecl var1, Set<File> var2) {
      Path var3 = this.module.getTask().getCompleteClasspath();
      if (WebServiceType.JAXWS.equals(var1.getType()) && var1.getCowReader() != null) {
         var3.createPathElement().setLocation(var1.getCowReader().getCowFile());
      }

      AntClassLoader var4 = new AntClassLoader(this.module.getTask().getProject(), var3);

      try {
         Set var5 = getDependentClasses(var1);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            this.log(EventLevel.VERBOSE, "Checking dependent class " + var7 + ".");

            try {
               var4.loadClass(var7);
               this.log(EventLevel.VERBOSE, "Dependent class " + var7 + " was found in classpath.");
            } catch (ClassNotFoundException var13) {
               File var9 = this.getSourceFile(var7);
               if (var9 == null) {
                  this.log(EventLevel.INFO, "Dependent class " + var7 + " was not found in the classpath or sourcpath.");
               } else {
                  this.log(EventLevel.VERBOSE, "Dependent class " + var7 + " was found in sourcepath at " + var9.getAbsolutePath() + ".'");
                  var2.add(var9);
               }
            }
         }
      } finally {
         var4.cleanup();
      }

   }

   private static Set<String> getDependentClasses(WebServiceDecl var0) {
      HashSet var1 = new HashSet();
      addEndpointInterface(var0, var1);
      addHandlers(var0, var1);
      if (var0 instanceof WebServiceSEIDecl) {
         addDeploymentListeners((WebServiceSEIDecl)var0, var1);
         addAlternativeTypes((WebServiceSEIDecl)((WebServiceSEIDecl)var0), var1);
      }

      return var1;
   }

   private static void addEndpointInterface(WebServiceDecl var0, Set<String> var1) {
      if (!var0.getJClass().getQualifiedName().equals(var0.getEIClass().getQualifiedName())) {
         var1.add(var0.getEIClass().getQualifiedName());
      }

   }

   private static void addAlternativeTypes(WebServiceSEIDecl var0, Set<String> var1) {
      Iterator var2 = var0.getWebMethods();

      while(var2.hasNext()) {
         WebMethodDecl var3 = (WebMethodDecl)var2.next();
         addAlternativeTypes((WebTypeDecl)var3.getWebResult(), var1);
         Iterator var4 = var3.getWebParams();

         while(var4.hasNext()) {
            WebParamDecl var5 = (WebParamDecl)var4.next();
            addAlternativeTypes((WebTypeDecl)var5, var1);
         }
      }

   }

   private static void addAlternativeTypes(WebTypeDecl var0, Set<String> var1) {
      if (var0.getTypeClassNames() != null) {
         for(int var2 = 0; var2 < var0.getTypeClassNames().length; ++var2) {
            var1.add(var0.getTypeClassNames()[var2]);
         }
      }

   }

   private static void addDeploymentListeners(WebServiceSEIDecl var0, Set<String> var1) {
      Iterator var2 = var0.getDeploymentListeners();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

   }

   private static void addHandlers(WebServiceDecl var0, Set<String> var1) {
      String[] var2 = var0.getHandlerChainDecl().getHandlerClassNames();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.add(var5);
      }

   }

   private File getSourceFile(String var1) {
      String var2 = StringUtil.getRelativeSourcePath(var1);
      Iterator var3 = this.sourcepaths.iterator();

      while(var3.hasNext()) {
         File var4 = (File)var3.next();
         File var5 = new File(var4, var2);
         this.log(EventLevel.VERBOSE, "Checking for dependent class " + var1 + " at " + var5.getAbsolutePath() + ".");

         try {
            if (var5.equals(var5.getCanonicalFile()) && var5.exists()) {
               return var5;
            }
         } catch (IOException var7) {
            this.log(EventLevel.WARNING, "Unable to determine canonical file for " + var5 + ": " + var7.getMessage());
         }
      }

      return null;
   }

   private static List<File> getSourcepaths(JwsModule var0) {
      ArrayList var1 = new ArrayList();
      var1.add(var0.getOutputDir());
      String[] var2 = var0.getSourcepath().list();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.add(new File(var5));
      }

      return var1;
   }

   private void addGeneratedSources(Set<File> var1) throws IOException {
      FileSet var2 = new FileSet();
      var2.setProject(this.module.getTask().getProject());
      var2.setDir(this.module.getOutputDir());
      var2.setIncludes("**/*.java");
      this.addFileSet(var2, var1);
   }

   private void addFileSetSources(Set<File> var1) throws IOException {
      Iterator var2 = this.module.getFileSets().iterator();

      while(var2.hasNext()) {
         FileSet var3 = (FileSet)var2.next();
         DirectoryScanner var4 = var3.getDirectoryScanner(this.module.getTask().getProject());
         String[] var5 = var4.getIncludedFiles();
         String[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (var9.endsWith(".java")) {
               var1.add((new File(var3.getDir(this.module.getTask().getProject()), var9)).getCanonicalFile());
            }
         }
      }

   }

   private void addFileSet(FileSet var1, Set<File> var2) throws IOException {
      DirectoryScanner var3 = var1.getDirectoryScanner(this.module.getTask().getProject());
      String[] var4 = var3.getIncludedFiles();
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var2.add((new File(var1.getDir(this.module.getTask().getProject()), var8)).getCanonicalFile());
      }

   }

   private void log(EventLevel var1, String var2) {
      this.module.getBuildContext().getLogger().log(var1, var2);
   }
}
