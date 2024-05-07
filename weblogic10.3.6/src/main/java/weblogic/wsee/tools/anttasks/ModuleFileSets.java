package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.ZipFileSet;

class ModuleFileSets {
   private final JwsModule module;
   private final boolean keepGenerated;

   public ModuleFileSets(JwsModule var1, boolean var2) {
      assert var1 != null;

      this.module = var1;
      this.keepGenerated = var2;
   }

   private ZipFileSet buildZipSet(String var1) {
      ZipFileSet var2 = new ZipFileSet();
      var2.setDir(this.module.getOutputDir());
      PatternSet.NameEntry var3 = var2.createInclude();
      var3.setName(var1);
      return var2;
   }

   private ZipFileSet buildClassesSet() {
      ZipFileSet var1 = new ZipFileSet();
      var1.setDir(this.module.getOutputDir());
      PatternSet.NameEntry var2 = var1.createInclude();
      var2.setName("**/*.class");
      var2 = var1.createInclude();
      var2.setName("**/*_handler.xml");
      var2 = var1.createInclude();
      var2.setName("**/callbackclient/*.*");
      var2 = var1.createExclude();
      var2.setName("schemacom_bea_xml/**/*.class");
      return var1;
   }

   private ZipFileSet buildArtifactSet() {
      ZipFileSet var1 = new ZipFileSet();
      var1.setDir(this.module.getOutputDir());
      var1.setIncludes("*.xml, **/policies/*.xml, **/*.wsdl, **/*.xsd");
      var1.setExcludes("web.xml");
      if (this.keepGenerated) {
         var1.setIncludes("schemacom_bea_xml/**, META-INF/**");
      }

      return var1;
   }

   List<ZipFileSet> getZipFileSets() {
      ArrayList var1 = new ArrayList();
      ZipFileSet var2 = this.buildArtifactSet();
      ZipFileSet var3 = this.buildClassesSet();
      if (this.module.isEjb()) {
         var1.add(var3);
         Iterator var4 = this.module.getFileSets().iterator();

         while(var4.hasNext()) {
            FileSet var5 = (FileSet)var4.next();
            if (var5 instanceof ZipFileSet) {
               var1.add((ZipFileSet)var5);
            } else {
               var1.add(new ExposingZipFileSet(var5));
            }
         }

         var2.setPrefix("META-INF/");
         var1.add(var2);
      } else {
         ZipFileSet var8 = new ZipFileSet();
         var8.setFile(new File(this.module.getOutputDir(), "web.xml"));
         var8.setPrefix("WEB-INF/");
         var1.add(var8);
         var2.setPrefix("WEB-INF/");
         var1.add(var2);
         var3.setPrefix("WEB-INF/classes");
         var1.add(var3);
         Iterator var9 = this.module.getFileSets().iterator();

         while(var9.hasNext()) {
            FileSet var6 = (FileSet)var9.next();
            if (var6 instanceof ZipFileSet) {
               var1.add((ZipFileSet)var6);
            } else {
               ExposingZipFileSet var7 = new ExposingZipFileSet(var6);
               var7.setPrefix("WEB-INF/classes");
               var1.add(var7);
            }
         }
      }

      if (this.keepGenerated) {
         var1.add(this.buildZipSet("**/*.java"));
      }

      return var1;
   }

   private static class ExposingZipFileSet extends ZipFileSet {
      ExposingZipFileSet(FileSet var1) {
         super(var1);
      }
   }
}
