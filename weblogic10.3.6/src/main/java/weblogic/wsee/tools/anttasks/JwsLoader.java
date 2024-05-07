package weblogic.wsee.tools.anttasks;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.build.GeneratedCallbackJws;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.ClassLoaderUtil;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.cow.CowFile;

class JwsLoader {
   private final JwsBuildContext ctx;
   private final File outputDir;
   private final Path sourcepath;
   private final Path classpath;
   private final boolean autoDetectCows;

   JwsLoader(JwsBuildContext var1, File var2, Path var3, Path var4, boolean var5) {
      assert var1 != null;

      assert var2 != null;

      assert var3 != null;

      assert var4 != null;

      this.ctx = var1;
      this.outputDir = var2;
      this.sourcepath = var3;
      this.classpath = var4;
      this.autoDetectCows = var5;
   }

   void load(Collection<Jws> var1) throws IOException, WsBuildException {
      this.extractCows(var1);
      this.loadJClasses(var1);
      this.removeNonWebServices(var1);
      if (this.autoDetectCows) {
         List var2 = this.autoDectectCOWs(var1);
         if (!var2.isEmpty()) {
            this.load(var2);
         }
      }

   }

   private void extractCows(Collection<Jws> var1) throws IOException {
      this.ctx.getLogger().log(EventLevel.INFO, "Parsing source files");
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Jws var3 = (Jws)var2.next();
         this.extractCow(var3);
      }

   }

   private void extractCow(Jws var1) throws IOException {
      if (var1.getCowReader() != null) {
         this.ctx.getLogger().log(EventLevel.VERBOSE, "Using compiledWsdl " + var1.getCowReader());
         boolean var2 = WebServiceType.JAXWS.equals(var1.getType());
         File var3 = this.outputDir;
         if (var1 instanceof GeneratedCallbackJws) {
            var3 = var1.getCowFile().getParentFile();
         }

         (new CowFile(var1.getCowFile())).extract(var3, this.ctx.getLogger(), var2);
         if (var2) {
            this.classpath.createPathElement().setLocation(var1.getCowFile());
         }
      }

   }

   private void removeNonWebServices(Collection<Jws> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Jws var3 = (Jws)var2.next();
         if (!ClassUtil.isWebService(var3.getJClass())) {
            var2.remove();
         }
      }

   }

   private List<Jws> autoDectectCOWs(Collection<Jws> var1) throws IOException, WsBuildException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Jws var4 = (Jws)var3.next();
         if (var4.getCowFile() == null) {
            File var5 = this.findCow(var4);
            if (var5 != null) {
               var4.setCompiledWsdl(var5);
               this.extractCow(var4);
               var2.add(var4);
            }
         }
      }

      return var2;
   }

   private File findCow(Jws var1) throws IOException, WsBuildException {
      String var2 = getEndpointInterface(var1.getJClass());
      if (var2 != null) {
         String var3 = var2.replace('.', '/') + ".java";
         List var4 = ClassLoaderUtil.getSourceURIs(this.ctx.getClassLoader(), var3);
         if (var4.size() > 1) {
            throw new WsBuildException("Too many source URIs for " + var3);
         }

         if (var4.size() == 1) {
            return new File((URI)var4.get(0));
         }
      }

      return null;
   }

   private static String getEndpointInterface(JClass var0) {
      JAnnotation var1 = var0.getAnnotation(WebService.class);
      return var1 != null ? JamUtil.getAnnotationStringValue(var1, "endpointInterface") : null;
   }

   private void loadJClasses(Collection<Jws> var1) throws WsBuildException {
      this.ctx.getLogger().log(EventLevel.INFO, "Parsing source files");
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Jws var4 = (Jws)var3.next();
         var2.add(var4.getAbsoluteFile());
      }

      JClass[] var8 = JamUtil.parseSource((File[])var2.toArray(new File[var2.size()]), this.sourcepath.toString(), this.classpath.toString(), this.ctx.getSrcEncoding());
      Map var9 = buildClassMap(var8);
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Jws var6 = (Jws)var5.next();
         File var7 = var6.getAbsoluteFile();
         if (!var9.containsKey(var7)) {
            throw new WsBuildException(var7 + " has no corresponding jclass");
         }

         var6.setJClass((JClass)var9.remove(var7));
      }

   }

   private static Map<File, JClass> buildClassMap(JClass[] var0) throws WsBuildException {
      HashMap var1 = new HashMap();
      JClass[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         JClass var5 = var2[var4];
         if (var5.getSourcePosition() == null || var5.getSourcePosition().getSourceURI() == null) {
            throw new WsBuildException("Unable to determine source for " + var5.getQualifiedName());
         }

         var1.put(new File(var5.getSourcePosition().getSourceURI()), var5);
      }

      return var1;
   }
}
