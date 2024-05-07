package weblogic.application.library;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import weblogic.application.internal.library.BasicLibraryData;
import weblogic.application.internal.library.util.DeweyDecimal;
import weblogic.management.configuration.LibraryMBean;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class LibraryData extends BasicLibraryData implements Serializable {
   private static final long serialVersionUID = 1648716277382804679L;
   private File location = null;
   private final LibraryConstants.AutoReferrer[] autoRef;
   private final Attributes attributes;

   public static LibraryData newEmptyInstance(File var0) {
      Attributes var1 = null;
      LibraryConstants.AutoReferrer[] var2 = null;

      try {
         var1 = getManifestAttributes(var0);
         var2 = checkAutoRefLib(var1);
      } catch (IOException var4) {
      }

      return new LibraryData((String)null, (DeweyDecimal)null, (String)null, var0, var2, var1);
   }

   public static LibraryData newInstance(String var0, String var1, String var2, File var3) throws IOException, IllegalSpecVersionTypeException {
      return new LibraryData(var0, var1, var2, var3);
   }

   public static LibraryData initFromManifest(File var0) throws IOException, IllegalSpecVersionTypeException {
      return new LibraryData(var0, getManifestAttributes(var0));
   }

   public static LibraryData initFromManifest(File var0, Attributes var1) throws IllegalSpecVersionTypeException {
      return new LibraryData(var0, var1);
   }

   public static LibraryData initFromMBean(LibraryMBean var0, File var1) throws IOException, IllegalSpecVersionTypeException {
      return new LibraryData(var0, var1);
   }

   public static LibraryData cloneWithNewName(String var0, LibraryData var1) {
      return new LibraryData(var0, var1.getSpecificationVersion(), var1.getImplementationVersion(), var1.getLocation(), var1.getAutoRef(), var1.getAttributes());
   }

   private LibraryData(String var1, String var2, String var3, File var4) throws IOException, IllegalSpecVersionTypeException {
      super(var1, var2, var3);
      this.location = var4;
      this.attributes = getManifestAttributes(var4);
      this.autoRef = checkAutoRefLib(this.attributes);
   }

   private LibraryData(String var1, DeweyDecimal var2, String var3, File var4, LibraryConstants.AutoReferrer[] var5, Attributes var6) {
      super(var1, var2, var3);
      this.location = var4;
      this.attributes = var6;
      this.autoRef = var5;
   }

   private LibraryData(LibraryMBean var1, File var2) throws IOException, IllegalSpecVersionTypeException {
      super(var1);
      this.location = var2;
      this.attributes = getManifestAttributes(var2);
      this.autoRef = checkAutoRefLib(this.attributes);
   }

   private LibraryData(File var1, Attributes var2) throws IllegalSpecVersionTypeException {
      super(var2);
      this.location = var1;
      this.attributes = var2;
      this.autoRef = checkAutoRefLib(var2);
   }

   private LibraryData(BasicLibraryData var1, File var2, LibraryConstants.AutoReferrer[] var3, Attributes var4) {
      super(var1.getName(), var1.getSpecificationVersion(), var1.getImplementationVersion());
      this.location = var2;
      this.autoRef = var3;
      this.attributes = var4;
   }

   public File getLocation() {
      return this.location;
   }

   public LibraryConstants.AutoReferrer[] getAutoRef() {
      return this.autoRef;
   }

   public Attributes getAttributes() {
      return this.attributes;
   }

   void setLocation(File var1) {
      this.location = var1;
   }

   public LibraryData importData(LibraryData var1) {
      BasicLibraryData var2 = super.importData(var1);
      File var3 = this.getLocation() == null ? var1.getLocation() : this.getLocation();
      LibraryConstants.AutoReferrer[] var4 = this.getAutoRef().length == 0 ? var1.getAutoRef() : this.getAutoRef();
      Attributes var5 = this.getAttributes() == null ? var1.getAttributes() : this.getAttributes();
      return new LibraryData(var2, var3, var4, var5);
   }

   private static Attributes getManifestAttributes(File var0) throws IOException {
      Attributes var1 = null;
      VirtualJarFile var2 = null;

      try {
         var2 = VirtualJarFactory.createVirtualJar(var0);
      } catch (IOException var8) {
         closeJar(var2);
         throw var8;
      }

      try {
         Manifest var3 = var2.getManifest();
         if (var3 != null) {
            var1 = var3.getMainAttributes();
         }
      } finally {
         closeJar(var2);
      }

      return var1;
   }

   private static void closeJar(VirtualJarFile var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
      }

   }

   private static LibraryConstants.AutoReferrer[] checkAutoRefLib(Attributes var0) {
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getValue("Auto-Ref-By");
      }

      if (var1 == null) {
         return new LibraryConstants.AutoReferrer[0];
      } else {
         HashSet var2 = new HashSet();
         String[] var3 = var1.split(",");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var2.add(LibraryConstants.AutoReferrer.valueOf(var6.trim()));
         }

         return (LibraryConstants.AutoReferrer[])var2.toArray(new LibraryConstants.AutoReferrer[0]);
      }
   }
}
