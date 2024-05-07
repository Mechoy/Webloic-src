package weblogic.application.internal.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.Attributes;
import weblogic.application.Type;
import weblogic.application.internal.library.util.DeweyDecimal;
import weblogic.application.library.IllegalSpecVersionTypeException;
import weblogic.application.library.LibraryConstants;
import weblogic.application.utils.LibraryUtils;
import weblogic.management.configuration.LibraryMBean;

public class BasicLibraryData implements Serializable {
   private static final String NOT_SET = "<not-set>";
   private final String name;
   private final DeweyDecimal specVersion;
   private final String implVersion;
   private Type type;

   public BasicLibraryData(Attributes var1) throws IllegalSpecVersionTypeException {
      this(getName(var1), getSpecVersion(var1), getImplVersion(var1));
   }

   public BasicLibraryData(LibraryMBean var1) throws IllegalSpecVersionTypeException {
      this(getName(var1), getSpecVersion(var1), getImplVersion(var1));
   }

   public BasicLibraryData(String var1, String var2, String var3) throws IllegalSpecVersionTypeException {
      this(var1, parseSpec(var2), var3);
   }

   public BasicLibraryData(String var1, String var2, String var3, Type var4) throws IllegalSpecVersionTypeException {
      this(var1, parseSpec(var2), var3, var4);
   }

   protected BasicLibraryData(String var1, DeweyDecimal var2, String var3) {
      this(var1, (DeweyDecimal)var2, var3, (Type)null);
   }

   public BasicLibraryData(String var1, DeweyDecimal var2, String var3, Type var4) {
      this.name = var1;
      this.specVersion = var2;
      this.implVersion = var3;
      this.type = var4;
   }

   public String getName() {
      return this.name;
   }

   public DeweyDecimal getSpecificationVersion() {
      return this.specVersion;
   }

   public String getImplementationVersion() {
      return this.implVersion;
   }

   public void setType(Type var1) {
      this.type = var1;
   }

   public Type getType() {
      return this.type;
   }

   public String toString() {
      return LibraryUtils.toString(this);
   }

   public BasicLibraryData importData(BasicLibraryData var1) {
      String var2 = this.getName() != null ? this.getName() : var1.getName();
      DeweyDecimal var3 = this.getSpecificationVersion() != null ? this.getSpecificationVersion() : var1.getSpecificationVersion();
      String var4 = this.getImplementationVersion() != null ? this.getImplementationVersion() : var1.getImplementationVersion();
      Type var5 = this.getType() != null ? this.getType() : var1.getType();
      return new BasicLibraryData(var2, var3, var4, var5);
   }

   public Collection verifyDataConsistency(BasicLibraryData var1) {
      ArrayList var2 = new ArrayList(3);
      this.verifyDataConsistency(var1, var2);
      return var2;
   }

   private void verifyDataConsistency(BasicLibraryData var1, Collection var2) {
      this.checkForMismatch(LibraryConstants.LIBRARY_NAME, this.getName(), var1.getName(), var2);
      this.checkForMismatch(LibraryConstants.SPEC_VERSION_NAME, LibraryUtils.nullOrString(this.getSpecificationVersion()), LibraryUtils.nullOrString(var1.getSpecificationVersion()), var2);
      this.checkForMismatch(LibraryConstants.IMPL_VERSION_NAME, this.getImplementationVersion(), var1.getImplementationVersion(), var2);
   }

   private void checkForMismatch(String var1, String var2, String var3, Collection var4) {
      String var5 = var2 == null ? "<not-set>" : var2;
      String var6 = var3 == null ? "<not-set>" : var3;
      if (!var5.equals(var6)) {
         StringBuffer var7 = new StringBuffer();
         var7.append(var1).append(": ").append(var5).append(" vs. ").append(var6);
         var4.add(var7.toString());
      }

   }

   private static String getName(Attributes var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getValue(LibraryConstants.LIBRARY_NAME);
         return var1 == null ? null : var1.trim();
      }
   }

   private static String getName(LibraryMBean var0) {
      return LibraryUtils.getName(var0);
   }

   private static DeweyDecimal getSpecVersion(Attributes var0) throws IllegalSpecVersionTypeException {
      return var0 == null ? null : parseSpec(var0.getValue(LibraryConstants.SPEC_VERSION_NAME));
   }

   private static DeweyDecimal getSpecVersion(LibraryMBean var0) throws IllegalSpecVersionTypeException {
      return parseSpec(LibraryUtils.getSpecVersion(var0));
   }

   private static String getImplVersion(Attributes var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getValue(LibraryConstants.IMPL_VERSION_NAME);
         return var1 == null ? null : var1.trim();
      }
   }

   private static String getImplVersion(LibraryMBean var0) {
      return LibraryUtils.getImplVersion(var0);
   }

   private static DeweyDecimal parseSpec(String var0) throws IllegalSpecVersionTypeException {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.trim();
         DeweyDecimal var2 = null;

         try {
            var2 = new DeweyDecimal(var1);
            return var2;
         } catch (NumberFormatException var4) {
            throw new IllegalSpecVersionTypeException(var1);
         }
      }
   }
}
