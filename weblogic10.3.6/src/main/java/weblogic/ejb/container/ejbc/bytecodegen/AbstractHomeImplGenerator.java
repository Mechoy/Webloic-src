package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.Label;
import com.bea.objectweb.asm.MethodVisitor;
import com.bea.objectweb.asm.Type;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;

abstract class AbstractHomeImplGenerator implements Generator {
   private static final String METHOD_FIELD_DESC = BCUtil.fieldDesc(Method.class);
   private static final String CLINIT_ASEERTION_ERROR_MSG = "Unable to find expected methods.  Please check your classpath for stale versions of your ejb classes and re-run weblogic.ejbc.\n If this is a java.io.FilePermission exception and you are running under JACC security, then check your security policy file.\n  Exception: ";
   final NamingConvention nc;
   final SessionBeanInfo sbi;
   final String clsName;
   final String superClsName;
   private static final String STATEFUL_BASE_CREATE_SIG = "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/reflect/Method;[Ljava/lang/Object;)";
   private static final String STATELESS_BASE_CREATE_SIG = "(Lweblogic/ejb/container/internal/MethodDescriptor;)";

   AbstractHomeImplGenerator(NamingConvention var1, SessionBeanInfo var2, String var3, String var4) {
      this.nc = var1;
      this.sbi = var2;
      this.clsName = var3;
      this.superClsName = var4;
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      Map var2 = EJBMethodsUtil.getHomeMethodSigs(this.getCreateMethods());
      if (this.needsClInit(var2.keySet())) {
         byte var3 = 26;
         Iterator var4 = var2.values().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var1.visitField(var3, "mth_" + var5, METHOD_FIELD_DESC, (String)null, (Object)null).visitEnd();
         }

         this.addClInit(var1, var2);
      }

      this.addInit(var1);
      String var9 = EJBMethodsUtil.methodDescriptorPrefix((short)1);
      Set var10 = this.sbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addDistinctMDFields(var1, var9, var2.values(), false);
      Iterator var11 = var2.entrySet().iterator();

      while(var11.hasNext()) {
         Map.Entry var6 = (Map.Entry)var11.next();
         String var7 = var9 + (String)var6.getValue();
         String var8 = "mth_" + (String)var6.getValue();
         this.addCreateMethod(var1, (Method)var6.getKey(), var7, var8, var10);
      }

      this.addCustomMembers(var1);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   abstract String[] getInterfaces();

   abstract Class<?> getComponentInterface();

   abstract String getComponentImplName();

   abstract Method[] getCreateMethods();

   abstract void addCustomMembers(ClassWriter var1);

   abstract Class<? extends Exception> getDefExceptionForCreate();

   abstract String getSupersCreateReturnType();

   private void addInit(ClassWriter var1) {
      MethodVisitor var2 = var1.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
      var2.visitCode();
      var2.visitVarInsn(25, 0);
      var2.visitLdcInsn(Type.getType("L" + this.getComponentImplName() + ";"));
      var2.visitMethodInsn(183, this.superClsName, "<init>", "(Ljava/lang/Class;)V");
      var2.visitInsn(177);
      var2.visitMaxs(2, 1);
      var2.visitEnd();
   }

   private boolean needsClInit(Set<Method> var1) {
      boolean var2 = true;
      if (!this.sbi.isStateful() && this.sbi.isEJB30()) {
         try {
            if (!var1.isEmpty()) {
               Method var3 = (Method)var1.iterator().next();
               String var4 = ((Ejb3SessionBeanInfo)this.sbi).getEjbCreateInitMethodName(var3);
               this.sbi.getBeanClass().getMethod(var4, var3.getParameterTypes());
            } else {
               var2 = false;
            }
         } catch (NoSuchMethodException var5) {
            var2 = false;
         }
      }

      return var2;
   }

   private void addClInit(ClassWriter var1, Map<Method, String> var2) {
      String var3 = BCUtil.binName(this.nc.getGeneratedBeanInterfaceName());
      Type var4 = Type.getType("L" + var3 + ";");
      MethodVisitor var5 = var1.visitMethod(8, "<clinit>", "()V", (String)null, (String[])null);
      var5.visitCode();
      Label var6 = new Label();
      Label var7 = new Label();
      Label var8 = new Label();
      var5.visitTryCatchBlock(var6, var7, var8, "java/lang/Exception");
      var5.visitLabel(var6);
      Iterator var9 = var2.entrySet().iterator();

      while(var9.hasNext()) {
         Map.Entry var10 = (Map.Entry)var9.next();
         Method var11 = (Method)var10.getKey();
         String var12 = (String)var10.getValue();
         var5.visitLdcInsn(var4);
         String var13 = null;
         if (this.sbi instanceof Ejb3SessionBeanInfo) {
            var13 = ((Ejb3SessionBeanInfo)this.sbi).getEjbCreateInitMethodName(var11);
         } else {
            var13 = "ejbC" + var11.getName().substring(1);
         }

         var5.visitLdcInsn(var13);
         Class[] var14 = var11.getParameterTypes();
         BCUtil.pushInsn(var5, var14.length);
         var5.visitTypeInsn(189, "java/lang/Class");

         for(int var15 = 0; var15 < var14.length; ++var15) {
            var5.visitInsn(89);
            BCUtil.pushInsn(var5, var15);
            if (var14[var15].isPrimitive()) {
               var5.visitFieldInsn(178, BCUtil.getBoxedClassBinName(var14[var15]), "TYPE", "Ljava/lang/Class;");
            } else {
               var5.visitLdcInsn(Type.getType(BCUtil.fieldDesc(var14[var15])));
            }

            var5.visitInsn(83);
         }

         var5.visitMethodInsn(182, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
         var5.visitFieldInsn(179, this.clsName, "mth_" + var12, METHOD_FIELD_DESC);
      }

      var5.visitLabel(var7);
      Label var16 = new Label();
      var5.visitJumpInsn(167, var16);
      var5.visitLabel(var8);
      var5.visitVarInsn(58, 0);
      var5.visitTypeInsn(187, "java/lang/AssertionError");
      var5.visitInsn(89);
      var5.visitTypeInsn(187, "java/lang/StringBuilder");
      var5.visitInsn(89);
      var5.visitLdcInsn("Unable to find expected methods.  Please check your classpath for stale versions of your ejb classes and re-run weblogic.ejbc.\n If this is a java.io.FilePermission exception and you are running under JACC security, then check your security policy file.\n  Exception: '");
      var5.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
      var5.visitVarInsn(25, 0);
      var5.visitMethodInsn(182, "java/lang/Exception", "getMessage", "()Ljava/lang/String;");
      var5.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
      var5.visitLdcInsn("'");
      var5.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
      var5.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
      var5.visitMethodInsn(183, "java/lang/AssertionError", "<init>", "(Ljava/lang/Object;)V");
      var5.visitInsn(191);
      var5.visitLabel(var16);
      var5.visitInsn(177);
      var5.visitMaxs(6, 1);
      var5.visitEnd();
   }

   private void addCreateMethod(ClassWriter var1, Method var2, String var3, String var4, Set<Class<?>> var5) {
      Class[] var6 = var2.getParameterTypes();
      String var7 = BCUtil.methodDesc(this.getComponentInterface(), var6);
      String[] var8 = BCUtil.exceptionsDesc(var2.getExceptionTypes());
      MethodVisitor var9 = var1.visitMethod(1, var2.getName(), var7, (String)null, var8);
      var9.visitCode();
      Label var10 = new Label();
      Label var11 = new Label();
      Label var12 = new Label();
      var9.visitTryCatchBlock(var10, var11, var12, "java/lang/Exception");
      var9.visitLabel(var10);
      var9.visitVarInsn(25, 0);
      var9.visitVarInsn(25, 0);
      var9.visitFieldInsn(180, this.clsName, var3, BCUtil.WL_MD_FIELD_DESCRIPTOR);
      String var13;
      if (this.sbi.isStateful()) {
         var9.visitFieldInsn(178, this.clsName, var4, METHOD_FIELD_DESC);
         if (var6.length == 0) {
            var9.visitInsn(3);
            var9.visitTypeInsn(189, "java/lang/Object");
         } else {
            BCUtil.boxArgs(var9, var2);
         }

         var13 = "(Lweblogic/ejb/container/internal/MethodDescriptor;Ljava/lang/reflect/Method;[Ljava/lang/Object;)" + this.getSupersCreateReturnType();
         var9.visitMethodInsn(183, this.superClsName, "create", var13);
      } else {
         var13 = "(Lweblogic/ejb/container/internal/MethodDescriptor;)" + this.getSupersCreateReturnType();
         var9.visitMethodInsn(183, this.superClsName, "create", var13);
      }

      var9.visitTypeInsn(192, BCUtil.binName(var2.getReturnType()));
      var9.visitLabel(var11);
      var9.visitInsn(176);
      var9.visitLabel(var12);
      int var18 = var6 == null ? 1 : BCUtil.sizeOf(var6) + 1;
      var9.visitVarInsn(58, var18);
      List var14 = BCUtil.combineExs(var5, var2);
      var14.add(0, this.getDefExceptionForCreate());
      Label var15 = null;
      Iterator var16 = var14.iterator();

      while(var16.hasNext()) {
         Class var17 = (Class)var16.next();
         if (var15 != null) {
            var9.visitLabel(var15);
         }

         var9.visitVarInsn(25, var18);
         var9.visitTypeInsn(193, BCUtil.binName(var17));
         var15 = new Label();
         var9.visitJumpInsn(153, var15);
         var9.visitVarInsn(25, var18);
         var9.visitTypeInsn(192, BCUtil.binName(var17));
         var9.visitInsn(191);
      }

      var9.visitLabel(var15);
      var9.visitTypeInsn(187, "javax/ejb/CreateException");
      var9.visitInsn(89);
      var9.visitTypeInsn(187, "java/lang/StringBuilder");
      var9.visitInsn(89);
      var9.visitLdcInsn("Error while creating bean: ");
      var9.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
      var9.visitVarInsn(25, var18);
      var9.visitMethodInsn(182, "java/lang/Exception", "toString", "()Ljava/lang/String;");
      var9.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
      var9.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
      var9.visitMethodInsn(183, "javax/ejb/CreateException", "<init>", "(Ljava/lang/String;)V");
      var9.visitInsn(191);
      var9.visitMaxs(7, 4);
      var9.visitEnd();
   }
}
