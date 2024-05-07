package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.utils.annotation.BeaSynthetic.Helper;

class BeanIntfGenerator implements Generator {
   private final SessionBeanInfo sbi;
   private final String clsName;

   BeanIntfGenerator(NamingConvention var1, SessionBeanInfo var2) {
      this.sbi = var2;
      this.clsName = BCUtil.binName(var1.getGeneratedBeanInterfaceName());
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(0);
      var1.visit(49, 1537, this.clsName, (String)null, "java/lang/Object", new String[]{BCUtil.binName(WLEnterpriseBean.class)});
      short var2 = 1025;

      Method var4;
      int var5;
      for(Iterator var3 = this.getMethods(this.sbi.getBeanClass()).iterator(); var3.hasNext(); var1.visitMethod(var5, var4.getName(), BCUtil.methodDesc(var4), (String)null, BCUtil.exceptionsDesc(var4.getExceptionTypes())).visitEnd()) {
         var4 = (Method)var3.next();
         var5 = var2;
         if (var4.isVarArgs()) {
            var5 = var2 + 128;
         }
      }

      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private Set<Method> getMethods(Class<?> var1) {
      HashSet var2 = new HashSet();
      Method[] var3 = var1.getMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (!Modifier.isAbstract(var6.getModifiers()) && !Modifier.isStatic(var6.getModifiers()) && !Helper.isBeaSyntheticMethod(var6) && var6.getDeclaringClass() != Object.class) {
            var2.add(var6);
         }
      }

      return var2;
   }
}
