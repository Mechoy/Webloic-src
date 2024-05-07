package weblogic.application.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import weblogic.utils.PlatformConstants;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.SequencingEnumerator;

public final class CompositeWebAppFinder implements ClassFinder {
   private MultiClassFinder webappFinder = null;
   private MultiClassFinder librariesFinder = null;

   public CompositeWebAppFinder() {
      this.webappFinder = new MultiClassFinder();
   }

   public void addFinder(ClassFinder var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot add null finder");
      } else {
         this.webappFinder.addFinder(var1);
      }
   }

   public void addFinderFirst(ClassFinder var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot add null finder");
      } else {
         this.webappFinder.addFinderFirst(var1);
      }
   }

   public void addLibraryFinder(ClassFinder var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot add null finder");
      } else {
         this.initLibraryFinder();
         this.librariesFinder.addFinder(var1);
      }
   }

   public ClassFinder getWebappFinder() {
      return this.webappFinder;
   }

   public ClassFinder getLibraryFinder() {
      return this.librariesFinder;
   }

   public Source getSource(String var1) {
      Source var2 = this.webappFinder.getSource(var1);
      if (var2 == null && this.librariesFinder != null) {
         var2 = this.librariesFinder.getSource(var1);
      }

      return var2;
   }

   public Enumeration getSources(String var1) {
      SequencingEnumerator var2 = new SequencingEnumerator();
      var2.addEnumeration(this.webappFinder.getSources(var1));
      if (this.librariesFinder != null) {
         var2.addEnumeration(this.librariesFinder.getSources(var1));
      }

      return var2;
   }

   public Source getClassSource(String var1) {
      Source var2 = this.webappFinder.getClassSource(var1);
      if (var2 == null && this.librariesFinder != null) {
         var2 = this.librariesFinder.getClassSource(var1);
      }

      return var2;
   }

   public String getClassPath() {
      StringBuffer var1 = new StringBuffer();
      String var2 = this.webappFinder.getClassPath();
      if (var2 != null && var2.trim().length() > 0) {
         var1.append(var2);
      }

      if (this.librariesFinder != null) {
         String var3 = this.librariesFinder.getClassPath();
         if (var3 != null && var3.trim().length() > 0) {
            var1.append(PlatformConstants.PATH_SEP).append(var3);
         }
      }

      return var1.toString();
   }

   public ClassFinder getManifestFinder() {
      MultiClassFinder var1 = new MultiClassFinder();
      ClassFinder var2 = this.webappFinder.getManifestFinder();
      if (var2 != null) {
         var1.addFinder(var2);
      }

      if (this.librariesFinder != null) {
         ClassFinder var3 = this.librariesFinder.getManifestFinder();
         if (var3 != null) {
            var1.addFinder(var3);
         }
      }

      return var1;
   }

   public Enumeration entries() {
      ArrayList var1 = new ArrayList();
      Enumeration var2 = this.webappFinder.entries();
      if (var2 != null && var2 != EmptyEnumerator.EMPTY) {
         var1.add(var2);
      }

      if (this.librariesFinder != null) {
         var2 = this.librariesFinder.entries();
         if (var2 != null && var2 != EmptyEnumerator.EMPTY) {
            var1.add(var2);
         }
      }

      return new SequencingEnumerator((Enumeration[])((Enumeration[])var1.toArray(new Enumeration[var1.size()])));
   }

   public void close() {
      this.webappFinder.close();
      if (this.librariesFinder != null) {
         this.librariesFinder.close();
         this.librariesFinder = null;
      }

   }

   private void initLibraryFinder() {
      if (this.librariesFinder == null) {
         this.librariesFinder = new MultiClassFinder();
      }

   }
}
