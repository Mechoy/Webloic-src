package weblogic.wsee.tools.jws.validation.annotation;

import com.bea.util.jam.JAnnotation;
import java.util.HashSet;
import java.util.Set;
import weblogic.wsee.util.StringUtil;

public class PackageMatchingRule implements MatchingRule {
   private final String packageName;
   private final boolean recursive;
   private final Set<String> excludeAnnotations;

   public PackageMatchingRule(String var1) {
      this(var1, false);
   }

   public PackageMatchingRule(String var1, boolean var2) {
      this(var1, (Class[])null, var2);
   }

   public PackageMatchingRule(String var1, Class[] var2) {
      this(var1, var2, false);
   }

   public PackageMatchingRule(String var1, Class[] var2, boolean var3) {
      this.excludeAnnotations = new HashSet();

      assert var1 != null : "Package name not specified";

      this.packageName = var1;
      this.recursive = var3;
      if (var2 != null) {
         Class[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            this.excludeAnnotations.add(var7.getName());
         }
      }

   }

   public boolean isMatch(JAnnotation var1) {
      if (this.excludeAnnotations.contains(var1.getQualifiedName())) {
         return false;
      } else {
         String var2 = StringUtil.getPackage(var1.getQualifiedName());
         boolean var3 = var2.equals(this.packageName);
         if (!var3 && this.recursive) {
            var3 = var2.startsWith(this.packageName + '.');
         }

         return var3;
      }
   }
}
