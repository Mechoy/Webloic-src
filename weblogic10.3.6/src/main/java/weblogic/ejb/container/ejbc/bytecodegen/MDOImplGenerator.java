package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.internal.MDOMethodInvoker;
import weblogic.ejb.container.internal.MessageDrivenLocalObject;
import weblogic.ejb.container.internal.MessageEndpointRemote;

class MDOImplGenerator implements Generator {
   private static final String MDO_INVOKER = BCUtil.binName(MDOMethodInvoker.class);
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/MessageDrivenLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;)Ljava/lang/Object;";
   private final MessageDrivenBeanInfo mdbi;
   private final String clsName;
   private final String superClsName;

   MDOImplGenerator(NamingConvention var1, MessageDrivenBeanInfo var2) {
      this.mdbi = var2;
      this.clsName = BCUtil.binName(var1.getMdLocalObjectClassName(var2.getIsWeblogicJMS()));
      this.superClsName = BCUtil.binName(MessageDrivenLocalObject.class);
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      BCUtil.addDefInit(var1, this.superClsName);
      Method[] var2 = EJBMethodsUtil.getMethods(this.mdbi.getMessagingTypeInterfaceClass(), false);
      Map var3 = EJBMethodsUtil.getMethodSigs(var2);
      String var4 = EJBMethodsUtil.methodDescriptorPrefix((short)0);
      BCUtil.addDistinctMDFields(var1, var4, var3.values(), true);

      for(int var5 = 0; var5 < var2.length; ++var5) {
         Method var6 = var2[var5];
         String var7 = var4 + (String)var3.get(var6);
         String var8 = BCUtil.methodDesc(var6);
         String[] var9 = BCUtil.exceptionsDesc(var6.getExceptionTypes());
         MethodVisitor var10 = var1.visitMethod(1, var6.getName(), var8, (String)null, var9);
         var10.visitCode();
         var10.visitVarInsn(25, 0);
         var10.visitFieldInsn(178, this.clsName, var7, BCUtil.WL_MD_FIELD_DESCRIPTOR);
         BCUtil.boxArgs(var10, var6);
         BCUtil.pushInsn(var10, var5);
         var10.visitLdcInsn(this.mdbi.getBeanClass().getName() + "." + var6.getName());
         var10.visitMethodInsn(184, MDO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/MessageDrivenLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;)Ljava/lang/Object;");
         BCUtil.unboxReturn(var10, var6.getReturnType());
         var10.visitMaxs(0, 0);
         var10.visitEnd();
      }

      String var11 = BCUtil.binName(this.mdbi.getMessagingTypeInterfaceClass());
      BCUtil.addInvoke(var1, var2, var11, this.clsName);
      Set var12 = this.mdbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addHandleException(var1, var2, this.clsName, var12);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      Class var1 = this.mdbi.getMessagingTypeInterfaceClass();
      return Remote.class.isAssignableFrom(var1) ? new String[]{BCUtil.binName(MessageEndpointRemote.class), BCUtil.binName(var1), BCUtil.WL_INVOKABLE_CLS} : new String[]{BCUtil.binName(var1), BCUtil.WL_INVOKABLE_CLS};
   }
}
