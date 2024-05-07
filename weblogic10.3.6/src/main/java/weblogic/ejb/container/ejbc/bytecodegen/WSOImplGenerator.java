package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.BaseWSLocalObject;
import weblogic.ejb.container.internal.EJBContextHandler;
import weblogic.ejb.container.internal.WSOMethodInvoker;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.ejb.spi.BaseWSObjectIntf;
import weblogic.utils.Debug;

class WSOImplGenerator implements Generator {
   private static final String WSO_INVOKER = BCUtil.binName(WSOMethodInvoker.class);
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/BaseWSLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;I)Ljava/lang/Object;";
   private static final String WL_WEBLOGIC_EJB_CONTEXT_HANDLER_CLS = BCUtil.binName(EJBContextHandler.class);
   private static final String WL_UTILS_DEBUG_CLS = BCUtil.binName(Debug.class);
   private static final String SUPER_PRE_INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;Lweblogic/security/service/ContextHandler;Lweblogic/security/acl/internal/AuthenticatedSubject;)Lweblogic/ejb/container/internal/InvocationWrapper;";
   private static final String EJB_CTX_HANDLER_METHOD_DESC = "(Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;)V";
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;

   WSOImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      this.nc = var1;
      this.sbi = var2;
      this.clsName = BCUtil.binName(var1.getWsObjectClassName());
      this.superClsName = BCUtil.binName(BaseWSLocalObject.class);
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      BCUtil.addDefInit(var1, this.superClsName);
      Method[] var2 = EJBMethodsUtil.getWebserviceMethods(this.sbi);
      Map var3 = EJBMethodsUtil.getMethodSigs(var2);
      String var4 = EJBMethodsUtil.methodDescriptorPrefix((short)0);
      BCUtil.addDistinctMDFields(var1, var4, var3.values(), true);

      for(int var5 = 0; var5 < var2.length; ++var5) {
         Method var6 = var2[var5];
         String var7 = var4 + (String)var3.get(var6);
         String var8 = BCUtil.methodDesc(var6);
         this.addPreInvokeMethod(var1, var6, var7);
         this.addBusMethod(var1, var6, var7, var8, var5);
         this.addDummyMethod(var1, var6, var8);
      }

      this.addPostInvoke(var1);
      BCUtil.addInvoke(var1, var2, BCUtil.binName(this.nc.getGeneratedBeanInterfaceName()), this.clsName);
      BCUtil.addHandleExceptionAssertMethod(var1);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      return new String[]{BCUtil.binName(BaseWSObjectIntf.class), "java/io/Serializable", BCUtil.WL_INVOKABLE_CLS};
   }

   private void addPostInvoke(ClassWriter var1) {
      MethodVisitor var2 = var1.visitMethod(1, "__WL__WS_postInvoke", "()V", (String)null, new String[]{"java/lang/Exception"});
      var2.visitCode();
      var2.visitVarInsn(25, 0);
      var2.visitMethodInsn(183, this.superClsName, "__WL_wsPostInvoke", "()V");
      var2.visitInsn(177);
      var2.visitMaxs(1, 1);
      var2.visitEnd();
   }

   private void addPreInvokeMethod(ClassWriter var1, Method var2, String var3) {
      String var4 = MethodUtils.getWSOPreInvokeMethodName(var2);
      StringBuilder var5 = new StringBuilder("(Lweblogic/security/acl/internal/AuthenticatedSubject;Lweblogic/security/service/ContextHandler;");
      Class[] var6 = var2.getParameterTypes();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Class var9 = var6[var8];
         var5.append(BCUtil.fieldDesc(var9));
      }

      var5.append(")V");
      MethodVisitor var10 = var1.visitMethod(1, var4, var5.toString(), (String)null, (String[])null);
      var10.visitCode();
      var10.visitFieldInsn(178, this.clsName, var3, BCUtil.WL_MD_FIELD_DESCRIPTOR);
      var7 = BCUtil.sizeOf(var2.getParameterTypes()) + 3;
      var10.visitVarInsn(58, var7);
      var10.visitVarInsn(25, 0);
      var10.visitVarInsn(25, var7);
      var10.visitTypeInsn(187, WL_WEBLOGIC_EJB_CONTEXT_HANDLER_CLS);
      var10.visitInsn(89);
      var10.visitVarInsn(25, var7);
      if (var2.getParameterTypes().length == 0) {
         var10.visitInsn(3);
         var10.visitTypeInsn(189, "java/lang/Object");
      } else {
         BCUtil.boxArgs(var10, var2, 3);
      }

      var10.visitMethodInsn(183, WL_WEBLOGIC_EJB_CONTEXT_HANDLER_CLS, "<init>", "(Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;)V");
      var10.visitVarInsn(25, 2);
      var10.visitVarInsn(25, 1);
      var10.visitMethodInsn(183, this.superClsName, "__WL_preInvoke", "(Lweblogic/ejb/container/internal/MethodDescriptor;Lweblogic/security/service/ContextHandler;Lweblogic/security/service/ContextHandler;Lweblogic/security/acl/internal/AuthenticatedSubject;)Lweblogic/ejb/container/internal/InvocationWrapper;");
      var10.visitInsn(87);
      var10.visitInsn(177);
      var10.visitMaxs(0, 0);
      var10.visitEnd();
   }

   private void addBusMethod(ClassWriter var1, Method var2, String var3, String var4, int var5) {
      String var6 = MethodUtils.getWSOBusinessMethodName(var2);
      MethodVisitor var7 = var1.visitMethod(1, var6, var4, (String)null, new String[]{"java/lang/Throwable"});
      var7.visitCode();
      var7.visitVarInsn(25, 0);
      var7.visitFieldInsn(178, this.clsName, var3, BCUtil.WL_MD_FIELD_DESCRIPTOR);
      BCUtil.boxArgs(var7, var2);
      BCUtil.pushInsn(var7, var5);
      var7.visitMethodInsn(184, WSO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/BaseWSLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;I)Ljava/lang/Object;");
      BCUtil.unboxReturn(var7, var2.getReturnType());
      var7.visitMaxs(0, 0);
      var7.visitEnd();
   }

   private void addDummyMethod(ClassWriter var1, Method var2, String var3) {
      String[] var4 = BCUtil.exceptionsDesc(var2.getExceptionTypes());
      MethodVisitor var5 = var1.visitMethod(1, var2.getName(), var3, (String)null, var4);
      var5.visitCode();
      var5.visitInsn(3);
      var5.visitLdcInsn(" invalid call on " + var2.getName() + ".");
      var5.visitMethodInsn(184, WL_UTILS_DEBUG_CLS, "assertion", "(ZLjava/lang/String;)V");
      BCUtil.addReturnDefaultValue(var5, var2.getReturnType());
      var5.visitMaxs(0, 0);
      var5.visitEnd();
   }
}
