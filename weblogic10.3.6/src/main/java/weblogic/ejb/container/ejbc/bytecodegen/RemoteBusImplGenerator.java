package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.MethodVisitor;
import com.bea.objectweb.asm.Type;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.internal.SessionRemoteMethodInvoker;
import weblogic.ejb.container.internal.StatefulRemoteObject;
import weblogic.ejb.container.internal.StatelessRemoteObject;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.ejb.spi.BusinessObject;
import weblogic.rmi.extensions.activation.Activatable;

class RemoteBusImplGenerator implements Generator {
   private static final MethInfo GET_BUSINESS_OBJECT_HANDLE_MI = MethInfo.of("_WL_getBusinessObjectHandle", "md_eo__WL_getBusinessObjectHandle").returns(BusinessHandle.class).exceps(RemoteException.class).create();
   private static final String SRO_INVOKER = BCUtil.binName(SessionRemoteMethodInvoker.class);
   private static final String INVOKE_METHOD_DESC = "(Lweblogic/ejb/container/internal/BaseRemoteObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;";
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;
   private final Class<?> busIntf;

   RemoteBusImplGenerator(NamingConvention var1, SessionBeanInfo var2, Class<?> var3) {
      this.nc = var1;
      this.sbi = var2;
      this.busIntf = var3;
      this.clsName = BCUtil.binName(var1.getRemoteBusinessImplClassName(this.busIntf));
      if (var2.isStateful()) {
         this.superClsName = BCUtil.binName(StatefulRemoteObject.class);
      } else {
         this.superClsName = BCUtil.binName(StatelessRemoteObject.class);
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
         var11.visitMethodInsn(184, SRO_INVOKER, "invoke", "(Lweblogic/ejb/container/internal/BaseRemoteObject;Lweblogic/ejb/container/internal/MethodDescriptor;[Ljava/lang/Object;ILjava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
         BCUtil.unboxReturn(var11, var7.getReturnType());
         var11.visitMaxs(0, 0);
         var11.visitEnd();
      }

      BCUtil.addInvoke(var1, var3, BCUtil.binName(this.nc.getGeneratedBeanInterfaceName()), this.clsName);
      Set var12 = this.sbi.getDeploymentInfo().getUncheckedAppExceptionClasses();
      BCUtil.addHandleException(var1, var3, this.clsName, var12);
      BCUtil.addEOMembers(var1, this.clsName, this.superClsName, GET_BUSINESS_OBJECT_HANDLE_MI);
      this.addGetImplementedBusinessInterfaceName(var1);
      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      String var1;
      if (Remote.class.isAssignableFrom(this.busIntf)) {
         var1 = BCUtil.binName(this.busIntf);
      } else {
         var1 = BCUtil.binName(this.nc.getRemoteBusinessIntfClassName(this.busIntf));
      }

      return this.sbi.isStateful() && this.sbi.getReplicationType() != 2 ? new String[]{var1, BCUtil.binName(Activatable.class), BCUtil.binName(BusinessObject.class), BCUtil.WL_INVOKABLE_CLS} : new String[]{var1, BCUtil.binName(BusinessObject.class), BCUtil.WL_INVOKABLE_CLS};
   }

   private void addGetImplementedBusinessInterfaceName(ClassWriter var1) {
      MethodVisitor var2 = var1.visitMethod(4, "getImplementedBusinessInterfaceName", "()Ljava/lang/String;", (String)null, (String[])null);
      var2.visitCode();
      var2.visitFieldInsn(178, this.clsName, "bIClass", "Ljava/lang/Class;");
      var2.visitMethodInsn(182, "java/lang/Class", "getName", "()Ljava/lang/String;");
      var2.visitInsn(176);
      var2.visitMaxs(1, 1);
      var2.visitEnd();
   }
}
