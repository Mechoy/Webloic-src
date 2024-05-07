package weblogic.servlet.utils.annotation;

import com.bea.objectweb.asm.Type;
import com.bea.objectweb.asm.commons.EmptyVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnotationDetectContext {
   private boolean isAnnotationPresent;
   private boolean isClassLevelOnly;
   private List<String> includes;
   private static final String[] excludes = new String[]{Type.getDescriptor(Deprecated.class).intern(), Type.getDescriptor(Override.class).intern(), Type.getDescriptor(SuppressWarnings.class).intern()};
   static final EmptyVisitor EMPTY_VISITOR = new EmptyVisitor();

   public AnnotationDetectContext(String... var1) {
      this(false, var1);
   }

   public AnnotationDetectContext(boolean var1, String... var2) {
      this.isClassLevelOnly = var1;
      this.isAnnotationPresent = false;
      this.includes = Collections.EMPTY_LIST;
      if (var2.length != 0) {
         this.includes = new ArrayList();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            this.includes.add(var6.intern());
         }
      }

   }

   public void setClassLevelOnly(boolean var1) {
      this.isClassLevelOnly = var1;
   }

   public boolean isClassLevelOnly() {
      return this.isClassLevelOnly;
   }

   public boolean isAnnotationPresent() {
      return this.isAnnotationPresent;
   }

   public boolean checkIfAnnotationPresent(String var1, boolean var2) {
      if (!var2) {
         return false;
      } else {
         String[] var3 = excludes;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var1.intern() == var6) {
               return false;
            }
         }

         if (!this.includes.isEmpty() && !this.includes.contains(var1.intern())) {
            return false;
         } else {
            this.isAnnotationPresent = true;
            return true;
         }
      }
   }
}
