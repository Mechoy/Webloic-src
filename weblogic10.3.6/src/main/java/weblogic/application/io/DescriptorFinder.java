package weblogic.application.io;

import java.util.Enumeration;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;

public class DescriptorFinder implements ClassFinder {
   private final String prefix;
   private ClassFinder delegate;

   public DescriptorFinder(String var1, ClassFinder var2) {
      this.prefix = var1 + "#";
      this.delegate = var2;
   }

   private String removePrefix(String var1) {
      return var1.substring(this.prefix.length(), var1.length());
   }

   public Source getSource(String var1) {
      return var1 != null && var1.startsWith(this.prefix) ? this.delegate.getSource(this.removePrefix(var1)) : null;
   }

   public Enumeration getSources(String var1) {
      return (Enumeration)(var1 != null && var1.startsWith(this.prefix) ? this.delegate.getSources(this.removePrefix(var1)) : new EmptyEnumerator());
   }

   public Source getClassSource(String var1) {
      return null;
   }

   public String getClassPath() {
      return "";
   }

   public ClassFinder getManifestFinder() {
      return new ClasspathClassFinder2("");
   }

   public Enumeration entries() {
      return EmptyEnumerator.EMPTY;
   }

   public void close() {
      this.delegate.close();
   }
}
