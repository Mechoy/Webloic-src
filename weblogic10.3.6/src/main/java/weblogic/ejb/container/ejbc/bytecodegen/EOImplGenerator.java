package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBHome;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import weblogic.ejb.EJBObject;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.SessionRemoteMethodInvoker;
import weblogic.ejb.container.internal.StatefulEJBObject;
import weblogic.ejb.container.internal.StatelessEJBObject;
import weblogic.rmi.extensions.activation.Activatable;

class EOImplGenerator implements Generator {
   private static final MethInfo REMOVE_OBJ_MI = MethInfo.of("remove", "md_eo_remove").exceps(RemoveException.class, RemoteException.class).create();
   private static final MethInfo GET_EJB_HOME_MI = MethInfo.of("getEJBHome", "md_eo_getEJBHome").returns(EJBHome.class).exceps(RemoteException.class).create();
   private static final MethInfo GET_HANDLE_MI = MethInfo.of("getHandle", "md_eo_getHandle").returns(Handle.class).exceps(RemoteException.class).create();
   private static final MethInfo GET_PRIMARY_KEY_MI = MethInfo.of("getPrimaryKey", "md_eo_getPrimaryKey").returns(Object.class).exceps(RemoteException.class).create();
   private static final MethInfo IS_IDENTICAL_MI;
   private static final String SRO_INVOKER;
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/BaseRemoteObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;";
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;

   EOImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      this.nc = var1;
      this.sbi = var2;
      this.clsName = BCUtil.binName(var1.getEJBObjectClassName());
      if (var2.isStateful()) {
         this.superClsName = BCUtil.binName(StatefulEJBObject.class);
      } else {
         this.superClsName = BCUtil.binName(StatelessEJBObject.class);
      }

   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      BCUtil.addDefInit(var1, this.superClsName);
      Method[] var2 = EJBMethodsUtil.getRemoteMethods(this.sbi.getRemoteInterfaceClass(), false);
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
         var10.visitMethodInsn(184, SRO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/BaseRemoteObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
         BCUtil.unboxReturn(var10, var6.getReturnType());
         var10.visitMaxs(0, 0);
         var10.visitEnd();
      }

      BCUtil.addInvoke(var1, var2, BCUtil.binName(this.nc.getGeneratedBeanInterfaceName()), this.clsName);
      Set var11 = this.sbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addHandleException(var1, var2, this.clsName, var11);
      BCUtil.addEOMembers(var1, this.clsName, this.superClsName, REMOVE_OBJ_MI, GET_EJB_HOME_MI, GET_HANDLE_MI, GET_PRIMARY_KEY_MI, IS_IDENTICAL_MI);
      this.addOperationsComplete(var1);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      return this.sbi.isStateful() && this.sbi.getReplicationType() != 2 ? new String[]{BCUtil.binName(this.sbi.getRemoteInterfaceClass()), BCUtil.binName(Activatable.class), BCUtil.binName(EJBObject.class), BCUtil.WL_INVOKABLE_CLS} : new String[]{BCUtil.binName(this.sbi.getRemoteInterfaceClass()), BCUtil.binName(EJBObject.class), BCUtil.WL_INVOKABLE_CLS};
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
      IS_IDENTICAL_MI = MethInfo.of("isIdentical", "md_eo_isIdentical_javax_ejb_EJBObject").args(javax.ejb.EJBObject.class).returns(Boolean.TYPE).exceps(RemoteException.class).create();
      SRO_INVOKER = BCUtil.binName(SessionRemoteMethodInvoker.class);
   }
}
