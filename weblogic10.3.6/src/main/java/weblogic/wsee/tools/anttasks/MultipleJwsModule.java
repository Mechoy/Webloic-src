package weblogic.wsee.tools.anttasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.context.JwsBuildContext;

public class MultipleJwsModule extends JwsModule {
   private List<Jws> jwses = new ArrayList();
   private List<JwsFileSet> jwsFileSets = new ArrayList();
   private List<Jws> allJwses = null;

   MultipleJwsModule(JwscTask var1, JwsBuildContext var2) {
      super(var1, var2);
      this.setName("jws");
   }

   public Jws createJws() {
      Jws var1 = new Jws();
      this.jwses.add(var1);
      return var1;
   }

   public JwsFileSet createJwsFileSet() {
      JwsFileSet var1 = new JwsFileSet();
      var1.setProject(this.getTask().getProject());
      this.jwsFileSets.add(var1);
      return var1;
   }

   Collection<Jws> getJwsFiles() {
      if (this.allJwses == null) {
         this.allJwses = new ArrayList(this.jwses);
         Iterator var1 = this.jwsFileSets.iterator();

         while(var1.hasNext()) {
            JwsFileSet var2 = (JwsFileSet)var1.next();
            this.allJwses.addAll(var2.getFiles(this.getTask().getSrcdir()));
         }
      }

      return this.allJwses;
   }

   Path getSourcepath() {
      Path var1 = super.getSourcepath();
      Iterator var2 = this.jwsFileSets.iterator();

      while(var2.hasNext()) {
         JwsFileSet var3 = (JwsFileSet)var2.next();
         var1.add(var3.getSrcdir());
      }

      return var1;
   }

   void reset() {
      if (this.allJwses != null) {
         this.allJwses.clear();
         this.allJwses = null;
      }

      this.jwses.clear();
      this.jwses = null;
      this.jwsFileSets.clear();
      this.jwsFileSets = null;
   }

   void validateImpl() {
   }
}
