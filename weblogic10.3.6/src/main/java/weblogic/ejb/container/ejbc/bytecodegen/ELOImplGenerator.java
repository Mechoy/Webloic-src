package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.RemoveException;
import weblogic.ejb.EJBLocalObject;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.SessionLocalMethodInvoker;
import weblogic.ejb.container.internal.StatefulEJBLocalObject;
import weblogic.ejb.container.internal.StatelessEJBLocalObject;
import weblogic.ejb20.interfaces.LocalHandle;

class ELOImplGenerator implements Generator {
   private static final MethInfo REMOVE_MI = MethInfo.of("remove", "md_eo_remove").exceps(RemoveException.class).create();
   private static final MethInfo GET_EJB_LOCAL_HOME_MI = MethInfo.of("getEJBLocalHome", "md_eo_getEJBLocalHome").returns(EJBLocalHome.class).exceps(EJBException.class).create();
   private static final MethInfo GET_LOCAL_HANDLE_MI = MethInfo.of("getLocalHandle", "md_eo_getLocalHandle").returns(LocalHandle.class).exceps(EJBException.class).create();
   private static final MethInfo GET_PRIMARY_KEY_MI = MethInfo.of("getPrimaryKey", "md_eo_getPrimaryKey").returns(Object.class).exceps(EJBException.class).create();
   private static final MethInfo IS_IDENTICAL_MI;
   private static final String SLO_INVOKER;
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/BaseLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;";
   private static final String SERIALIZATION_ERROR_MSG = "Attempt to pass a reference to an EJBLocalObject to a remote client. A local EJB component may only be accessed by clients co-located in the same ear or standalone jar file.";
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;

   ELOImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      this.nc = var1;
      this.sbi = var2;
      this.clsName = BCUtil.binName(var1.getEJBLocalObjectClassName());
      if (var2.isStateful()) {
         this.superClsName = BCUtil.binName(StatefulEJBLocalObject.class);
      } else {
         this.superClsName = BCUtil.binName(StatelessEJBLocalObject.class);
      }

   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      BCUtil.addDefInit(var1, this.superClsName);
      Method[] var2 = EJBMethodsUtil.getLocalMethods(this.sbi.getLocalInterfaceClass(), false);
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
         var10.visitLdcInsn(this.sbi.getBeanClass().getName() + "." + var6.getName());
         var10.visitInsn(1);
         var10.visitMethodInsn(184, SLO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/BaseLocalObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
         BCUtil.unboxReturn(var10, var6.getReturnType());
         var10.visitMaxs(0, 0);
         var10.visitEnd();
      }

      BCUtil.addInvoke(var1, var2, BCUtil.binName(this.nc.getGeneratedBeanInterfaceName()), this.clsName);
      Set var11 = this.sbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addHandleException(var1, var2, this.clsName, var11);
      BCUtil.addEOMembers(var1, this.clsName, this.superClsName, REMOVE_MI, GET_EJB_LOCAL_HOME_MI, GET_LOCAL_HANDLE_MI, GET_PRIMARY_KEY_MI, IS_IDENTICAL_MI);
      this.addOperationsComplete(var1);
      BCUtil.addSerializationAssertMethods(var1, "javax/ejb/EJBException", "Attempt to pass a reference to an EJBLocalObject to a remote client. A local EJB component may only be accessed by clients co-located in the same ear or standalone jar file.");
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      return new String[]{BCUtil.binName(this.sbi.getLocalInterfaceClass()), "java/io/Serializable", BCUtil.binName(EJBLocalObject.class), BCUtil.WL_INVOKABLE_CLS};
   }

   private void addOperationsComplete(ClassWriter var1) {
      MethodVisitor var2 = var1.visitMethod(1, "operationsComplete", "()V", (String)null, (String[])null);
      var2.visitCode();
      var2.visitVarInsn(25, 0);
      var2.visitMethodInsn(183, this.superClsName, "operationsComplete", "()V");
      var2.visitInsn(177);
      var2.visitMaxs(1, 1);
      var2.visitEnd();
   }

   static {
      IS_IDENTICAL_MI = MethInfo.of("isIdentical", "md_eo_isIdentical_javax_ejb_EJBLocalObject").args(javax.ejb.EJBLocalObject.class).returns(Boolean.TYPE).exceps(EJBException.class).create();
      SLO_INVOKER = BCUtil.binName(SessionLocalMethodInvoker.class);
   }
}
