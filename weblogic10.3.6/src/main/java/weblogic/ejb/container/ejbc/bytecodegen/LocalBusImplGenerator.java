package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import com.bea.objectweb.asm.Type;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.SessionLocalMethodInvoker;
import weblogic.ejb.container.internal.StatefulLocalObject;
import weblogic.ejb.container.internal.StatelessLocalObject;

class LocalBusImplGenerator implements Generator {
   private static final String SLO_INVOKER = BCUtil.binName(SessionLocalMethodInvoker.class);
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/BaseLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;";
   private static final String SERIALIZATION_ERROR_MSG = "Attempt to pass a reference to an EJBLocalObject to a remote client. A local EJB component may only be accessed by clients co-located in the same ear or standalone jar file.";
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;
   private final Class<?> busIntf;

   LocalBusImplGenerator(NamingConvention var1, SessionBeanInfo var2, Class<?> var3) {
      this.nc = var1;
      this.sbi = var2;
      this.busIntf = var3;
      this.clsName = BCUtil.binName(var1.getLocalBusinessImplClassName(this.busIntf));
      if (var2.isStateful()) {
         this.superClsName = BCUtil.binName(StatefulLocalObject.class);
      } else {
         this.superClsName = BCUtil.binName(StatelessLocalObject.class);
      }

   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      var1.visitField(26, "bIClass", "Ljava/lang/Class;", (String)null, (Object)null).visitEnd();
      MethodVisitor var2 = var1.visitMethod(8, "<clinit>", "()V", (String)null, (String[])null);
      var2.visitCode();
      var2.visitLdcInsn(Type.getType(this.busIntf));
      var2.visitFieldInsn(179, this.clsName, "bIClass", "Ljava/lang/Class;");
      var2.visitInsn(177);
      var2.visitMaxs(1, 0);
      var2.visitEnd();
      BCUtil.addDefInit(var1, this.superClsName);
      Method[] var3 = EJBMethodsUtil.getMethods(this.busIntf, false);
      Map var4 = EJBMethodsUtil.getMethodSigs(var3);
      String var5 = EJBMethodsUtil.methodDescriptorPrefix((short)0);
      BCUtil.addDistinctMDFields(var1, var5, var4.values(), true);

      for(int var6 = 0; var6 < var3.length; ++var6) {
         Method var7 = var3[var6];
         String var8 = var5 + (String)var4.get(var7);
         String var9 = BCUtil.methodDesc(var7);
         String[] var10 = BCUtil.exceptionsDesc(var7.getExceptionTypes());
         MethodVisitor var11 = var1.visitMethod(1, var7.getName(), var9, (String)null, var10);
         var11.visitCode();
         var11.visitVarInsn(25, 0);
         var11.visitFieldInsn(178, this.clsName, var8, BCUtil.WL_MD_FIELD_DESCRIPTOR);
         BCUtil.boxArgs(var11, var7);
         BCUtil.pushInsn(var11, var6);
         var11.visitLdcInsn(this.sbi.getBeanClass().getName() + "." + var7.getName());
         var11.visitFieldInsn(178, this.clsName, "bIClass", "Ljava/lang/Class;");
         var11.visitMethodInsn(184, SLO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/BaseLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
         BCUtil.unboxReturn(var11, var7.getReturnType());
         var11.visitMaxs(0, 0);
         var11.visitEnd();
      }

      BCUtil.addInvoke(var1, var3, BCUtil.binName(this.nc.getGeneratedBeanInterfaceName()), this.clsName);
      Set var12 = this.sbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addHandleException(var1, var3, this.clsName, var12);
      BCUtil.addSerializationAssertMethods(var1, "javax/ejb/EJBException", "Attempt to pass a reference to an EJBLocalObject to a remote client. A local EJB component may only be accessed by clients co-located in the same ear or standalone jar file.");
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      return new String[]{BCUtil.binName(this.busIntf), "java/io/Serializable", BCUtil.WL_INVOKABLE_CLS};
   }
}
