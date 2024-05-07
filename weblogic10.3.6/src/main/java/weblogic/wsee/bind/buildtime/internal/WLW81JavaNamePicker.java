package weblogic.wsee.bind.buildtime.internal;

import com.bea.xbean.common.NameUtil;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;

public class WLW81JavaNamePicker {
   private boolean _uppercase;
   private Set _usedNames = new HashSet();
   private Set _nonUniqueNames = new HashSet();

   public WLW81JavaNamePicker(boolean var1) {
      this._uppercase = var1;
   }

   public static String[] pickNames(String[] var0, boolean var1) {
      WLW81JavaNamePicker var2 = new WLW81JavaNamePicker(var1);
      String[] var3 = new String[var0.length];

      for(int var4 = 0; var4 < var0.length; ++var4) {
         var3[var4] = var2.pick(var0[var4]);
      }

      return var3;
   }

   public static String[] pickNames(QName[] var0, boolean var1) {
      WLW81JavaNamePicker var2 = new WLW81JavaNamePicker(var1);
      String[] var3 = new String[var0.length];

      for(int var4 = 0; var4 < var0.length; ++var4) {
         var3[var4] = var2.pick(var0[var4]);
      }

      return var3;
   }

   public void exclude(String var1) {
      this._usedNames.add(var1);
   }

   public void exclude(Set var1) {
      this._usedNames.addAll(var1);
   }

   public boolean isNonUnique(String var1) {
      return this._nonUniqueNames.contains(var1);
   }

   public String pick(QName var1) {
      return this.pick(var1, false);
   }

   public String pick(QName var1, boolean var2) {
      return var1 == null ? this.pick((String)null, var2) : this.pick(var1.getLocalPart(), var2);
   }

   public String pick(String var1) {
      return this.pick(var1, false);
   }

   public String pick(String var1, boolean var2) {
      if (var1 == null || var1.length() == 0) {
         var1 = this._uppercase ? "T" : "t";
      }

      String var3;
      if (NameUtil.isValidJavaIdentifier(var1)) {
         var3 = var1;
      } else if (this._uppercase) {
         var3 = NameUtil.nonJavaCommonClassName(NameUtil.upperCamelCase(var1));
      } else {
         var3 = NameUtil.nonJavaKeyword(NameUtil.lowerCamelCase(var1));
      }

      if (var2) {
         String var4 = var3;

         for(int var5 = 1; this._usedNames.contains(var3); ++var5) {
            var3 = var4 + var5;
         }
      }

      if (this._usedNames.contains(var3)) {
         this._nonUniqueNames.add(var3);
      } else {
         this._usedNames.add(var3);
      }

      return var3;
   }
}
