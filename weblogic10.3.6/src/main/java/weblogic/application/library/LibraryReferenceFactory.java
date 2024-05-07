package weblogic.application.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import weblogic.application.Type;
import weblogic.application.internal.library.BasicLibraryData;
import weblogic.application.internal.library.LibraryRegistry;
import weblogic.application.internal.library.OptionalPackageReference;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.utils.StringUtils;

public final class LibraryReferenceFactory {
   private LibraryReferenceFactory() {
   }

   public static J2EELibraryReference[] getAppLibReference() {
      return getAppLibReference(LibraryRegistry.getRegistry());
   }

   public static J2EELibraryReference[] getAppLibReference(LibraryRegistry var0) {
      return getMatchingJ2EELibRefs(var0, ApplicationLibrary.class, (Type)null);
   }

   public static J2EELibraryReference[] getWebAppLibReference() {
      return getMatchingJ2EELibRefs(LibraryRegistry.getRegistry(), Type.WAR);
   }

   public static J2EELibraryReference[] getWebAppLibReference(LibraryRegistry var0) {
      return getMatchingJ2EELibRefs(var0, Type.WAR);
   }

   private static J2EELibraryReference[] getMatchingJ2EELibRefs(LibraryRegistry var0, Type var1) {
      return getMatchingJ2EELibRefs(var0, Library.class, var1);
   }

   private static J2EELibraryReference[] getMatchingJ2EELibRefs(LibraryRegistry var0, Class var1, Type var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("libCategory cannot be null");
      } else {
         ArrayList var3 = new ArrayList();
         Collection var4 = var0.getAll();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Library var6 = (Library)var5.next();
            if (var1.isAssignableFrom(var6.getClass())) {
               try {
                  BasicLibraryData var7 = new BasicLibraryData(var6.getName(), var6.getSpecificationVersion(), var6.getImplementationVersion(), var2);
                  var3.add(new J2EELibraryReference(var7, true, (String)null));
               } catch (IllegalSpecVersionTypeException var8) {
                  throw new AssertionError(var8);
               }
            }
         }

         return (J2EELibraryReference[])((J2EELibraryReference[])var3.toArray(new J2EELibraryReference[var3.size()]));
      }
   }

   public static J2EELibraryReference[] getAppLibReference(LibraryRefBean[] var0) throws IllegalSpecVersionTypeException {
      return getReference(var0, (Type)null);
   }

   public static J2EELibraryReference[] getWebLibReference(LibraryRefBean[] var0) throws IllegalSpecVersionTypeException {
      return getReference(var0, Type.WAR);
   }

   public static LibraryReference[] getOptPackReference(String var0, Attributes var1) {
      String var2 = var1.getValue(Name.EXTENSION_LIST);
      if (var2 == null) {
         return null;
      } else {
         String[] var3 = StringUtils.splitCompletely(var2, " ");
         ArrayList var4 = new ArrayList(var3.length);

         for(int var5 = 0; var5 < var3.length; ++var5) {
            String var6 = var3[var5].trim();
            if (var6.length() != 0) {
               String var7 = var1.getValue(var6 + "-" + Name.EXTENSION_NAME);
               if (var7 == null) {
                  LibraryLoggingUtils.warnMissingExtensionName(var6, var0);
               } else {
                  String var8 = var1.getValue(var3[var5] + "-" + Name.SPECIFICATION_VERSION);
                  String var9 = var1.getValue(var3[var5] + "-" + Name.IMPLEMENTATION_VERSION);
                  BasicLibraryData var10 = LibraryLoggingUtils.initOptionalPackageRefLibData(var7, var8, var9, var0);
                  if (var10 != null) {
                     var4.add(new OptionalPackageReference(var10, var0));
                  }
               }
            }
         }

         if (var4.isEmpty()) {
            return null;
         } else {
            return (LibraryReference[])((LibraryReference[])var4.toArray(new OptionalPackageReference[var4.size()]));
         }
      }
   }

   private static J2EELibraryReference[] getReference(LibraryRefBean[] var0, Type var1) throws IllegalSpecVersionTypeException {
      if (var0 == null) {
         return null;
      } else {
         J2EELibraryReference[] var2 = new J2EELibraryReference[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            BasicLibraryData var4 = new BasicLibraryData(var0[var3].getLibraryName(), var0[var3].getSpecificationVersion(), var0[var3].getImplementationVersion(), var1);
            var2[var3] = new J2EELibraryReference(var4, var0[var3].getExactMatch(), var0[var3].getContextRoot());
         }

         return var2;
      }
   }
}
