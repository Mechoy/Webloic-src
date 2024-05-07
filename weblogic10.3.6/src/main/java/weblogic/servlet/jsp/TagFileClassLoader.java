package weblogic.servlet.jsp;

import java.util.Enumeration;
import weblogic.utils.classloaders.ChangeAwareClassLoader;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.enumerations.EmptyEnumerator;

public class TagFileClassLoader extends ChangeAwareClassLoader {
   private static final boolean debug = false;
   private final long creationTime = this.getLastChecked();

   public TagFileClassLoader(ClassFinder var1, ClassLoader var2) {
      super(var1, false, var2);
   }

   public Class loadClass(String var1) throws ClassNotFoundException {
      return this.findClass(var1);
   }

   public Class findClass(String var1) throws ClassNotFoundException {
      return this.isTagFileClass(var1) ? super.findClass(var1) : this.getParent().loadClass(var1);
   }

   public Enumeration findResources(String var1) {
      return new EmptyEnumerator();
   }

   public final long getCreationTime() {
      return this.creationTime;
   }

   private boolean isTagFileClass(String var1) {
      return var1.indexOf("_tags") > 0 && (var1.endsWith("_tag") || var1.indexOf("_tag$") > 0);
   }
}
