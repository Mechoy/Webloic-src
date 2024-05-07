package weblogic.ejb.container.ejbc.bytecodegen;

import com.bea.objectweb.asm.ClassWriter;
import com.bea.objectweb.asm.Label;
import com.bea.objectweb.asm.MethodVisitor;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.ejb.EJBContext;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.transaction.Transaction;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.ejbc.EJBMethodsUtil;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.ExtendedPersistenceContextWrapper;

class BeanImplGenerator implements Generator {
   private final NamingConvention nc;
   private final SessionBeanInfo sbi;
   private final String clsName;
   private final String superClsName;
   private static final String WL_ALLOWED_METHODS_HELPER_CLS = BCUtil.binName(AllowedMethodsHelper.class);

   BeanImplGenerator(NamingConvention var1, SessionBeanInfo var2) {
      this.nc = var1;
      this.sbi = var2;
      this.clsName = BCUtil.binName(var1.getGeneratedBeanClassName());
      this.superClsName = BCUtil.binName(var1.getBeanClassName());
   }

   public Generator.Output generate() {
      ClassWriter var1 = new ClassWriter(1);
      var1.visit(49, 49, this.clsName, (String)null, this.superClsName, this.getInterfaces());
      this.addMembers(var1, "__WL_method_state", Integer.TYPE, "__WL_getMethodState", "__WL_setMethodState");
      this.addMembers(var1, "__WL_busy", Boolean.TYPE, "__WL_isBusy", "__WL_setBusy");
      this.addMembers(var1, "__WL_isLocal", Boolean.TYPE, "__WL_getIsLocal", "__WL_setIsLocal");
      this.addMembers(var1, "__WL_needsRemove", Boolean.TYPE, "__WL_needsRemove", "__WL_setNeedsRemove");
      this.addMembers(var1, "__WL_creatorOfTx", Boolean.TYPE, "__WL_isCreatorOfTx", "__WL_setCreatorOfTx");
      this.addMembers(var1, "__WL_EJBContext", EJBContext.class, "__WL_getEJBContext", "__WL_setEJBContext");
      this.addMembers(var1, "__WL_bmtx", Transaction.class, "__WL_getBeanManagedTransaction", "__WL_setBeanManagedTransaction");
      this.addMembers(var1, "__WL_loadUser", Object.class, "__WL_getLoadUser", "__WL_setLoadUser");
      boolean var2 = this.sbi.isStateful();
      if (var2) {
         this.addMembers(var1, "__WL_needsSessionSynchronization", Boolean.TYPE, "__WL_needsSessionSynchronization", "__WL_setNeedsSessionSynchronization");
      }

      boolean var3 = this.sbi.isStateful() && this.sbi.isEJB30() && ((Ejb3SessionBeanInfo)this.sbi).containsExtendedPersistenceContextRefs();
      if (var3) {
         var1.visitField(2, "__WL_persistenceContexts", "Ljava/util/Set;", (String)null, (Object)null).visitEnd();
      }

      this.addInit(var1, var3, var2);
      if (SessionBean.class.isAssignableFrom(this.sbi.getBeanClass())) {
         this.addSessionBeanCallbacks(var1);
      }

      if (this.sbi.isStateful() && SessionSynchronization.class.isAssignableFrom(this.sbi.getBeanClass())) {
         this.addSessionSyncCallbacks(var1);
      }

      Method[] var4 = EJBMethodsUtil.getBeanHomeClassMethods(this.sbi.getBeanClass());
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         this.addXMethod(var1, var7, 4);
      }

      if (this.sbi.isStateful()) {
         this.addGetExtendedPersistenceContexts(var1, var3);
         this.addCloseExtendedPersistenceContexts(var1);
      }

      if (this.sbi.isEJB30() && this.sbi.isStateful() && !Serializable.class.isAssignableFrom(this.sbi.getBeanClass())) {
         BCUtil.addSerializationMethods(var1);
      }

      var1.visitEnd();
      return new ClassFileOutput(this.clsName, var1.toByteArray());
   }

   private String[] getInterfaces() {
      String var1 = BCUtil.binName(this.nc.getGeneratedBeanInterfaceName());
      if (this.sbi.isStateful() && this.sbi.isEJB30() && !Serializable.class.isAssignableFrom(this.sbi.getBeanClass())) {
         return new String[]{var1, BCUtil.binName(WLSessionBean.class), "java/io/Serializable"};
      } else {
         return this.sbi.isStateful() ? new String[]{var1, BCUtil.binName(WLSessionBean.class)} : new String[]{var1, BCUtil.binName(WLEnterpriseBean.class)};
      }
   }

   private void addInit(ClassWriter var1, boolean var2, boolean var3) {
      MethodVisitor var4 = var1.visitMethod(1, "<init>", "()V", (String)null, BCUtil.exceptionsDesc(this.beanInitExs()));
      var4.visitCode();
      var4.visitVarInsn(25, 0);
      var4.visitMethodInsn(183, this.superClsName, "<init>", "()V");
      if (var2) {
         var4.visitVarInsn(25, 0);
         var4.visitTypeInsn(187, "java/util/HashSet");
         var4.visitInsn(89);
         var4.visitMethodInsn(183, "java/util/HashSet", "<init>", "()V");
         var4.visitFieldInsn(181, this.clsName, "__WL_persistenceContexts", "Ljava/util/Set;");
      }

      if (var3) {
         var4.visitVarInsn(25, 0);
         var4.visitInsn(4);
         var4.visitFieldInsn(181, this.clsName, "__WL_needsSessionSynchronization", "Z");
      }

      var4.visitInsn(177);
      var4.visitMaxs(0, 0);
      var4.visitEnd();
   }

   private void addSessionBeanCallbacks(ClassWriter var1) {
      Method var2 = null;

      try {
         var2 = this.sbi.getBeanClass().getMethod("ejbActivate");
         this.addXMethod(var1, var2, 32);
         var2 = this.sbi.getBeanClass().getMethod("ejbPassivate");
         this.addXMethod(var1, var2, 64);
         var2 = this.sbi.getBeanClass().getMethod("ejbRemove");
         this.addXMethod(var1, var2, 16);
         var2 = this.sbi.getBeanClass().getMethod("setSessionContext", SessionContext.class);
         this.addXMethod(var1, var2, 1);
      } catch (NoSuchMethodException var4) {
         throw new AssertionError("Method missing from " + this.sbi.getBeanClass() + ". " + var4);
      }
   }

   private void addSessionSyncCallbacks(ClassWriter var1) {
      Method var2 = null;

      try {
         var2 = this.sbi.getBeanClass().getMethod("afterBegin");
         this.addXMethod(var1, var2, 256);
         var2 = this.sbi.getBeanClass().getMethod("beforeCompletion");
         this.addXMethod(var1, var2, 512);
         var2 = this.sbi.getBeanClass().getMethod("afterCompletion", Boolean.TYPE);
         this.addXMethod(var1, var2, 1024);
      } catch (NoSuchMethodException var4) {
         throw new AssertionError("Method missing from " + this.sbi.getBeanClass() + ". " + var4);
      }
   }

   private void addGetExtendedPersistenceContexts(ClassWriter var1, boolean var2) {
      MethodVisitor var3 = var1.visitMethod(1, "getExtendedPersistenceContexts", "()Ljava/util/Set;", (String)null, (String[])null);
      var3.visitCode();
      if (var2) {
         var3.visitVarInsn(25, 0);
         var3.visitFieldInsn(180, this.clsName, "__WL_persistenceContexts", "Ljava/util/Set;");
      } else {
         var3.visitInsn(1);
      }

      var3.visitInsn(176);
      var3.visitMaxs(1, 1);
      var3.visitEnd();
   }

   private void addXMethod(ClassWriter var1, Method var2, int var3) {
      Class[] var4 = var2.getParameterTypes();
      Class var5 = var2.getReturnType();
      Class[] var6 = var2.getExceptionTypes();
      String var7 = BCUtil.methodDesc(var5, var4);
      boolean var8 = var5 == Void.TYPE;
      int var9 = var4 == null ? 0 : BCUtil.sizeOf(var4);
      int var10 = var9 + 1;
      int var11 = var9 + 2;
      int var12 = var9 + 2 + (var8 ? 0 : BCUtil.sizeOf(var5));
      MethodVisitor var13 = var1.visitMethod(1, var2.getName(), var7, (String)null, BCUtil.exceptionsDesc(var6));
      var13.visitCode();
      Label var14 = new Label();
      Label var15 = new Label();
      Label var16 = new Label();
      var13.visitTryCatchBlock(var14, var15, var16, (String)null);
      Label var17 = new Label();
      var13.visitTryCatchBlock(var16, var17, var16, (String)null);
      var13.visitVarInsn(25, 0);
      var13.visitFieldInsn(180, this.clsName, "__WL_method_state", "I");
      var13.visitVarInsn(54, var10);
      var13.visitLabel(var14);
      var13.visitVarInsn(25, 0);
      var13.visitMethodInsn(184, WL_ALLOWED_METHODS_HELPER_CLS, "pushBean", "(Lweblogic/ejb/container/interfaces/WLEnterpriseBean;)V");
      var13.visitVarInsn(25, 0);
      BCUtil.pushInsn(var13, var3);
      var13.visitFieldInsn(181, this.clsName, "__WL_method_state", "I");
      var13.visitVarInsn(25, 0);
      if (var4 != null) {
         int var18 = 1;
         Class[] var19 = var4;
         int var20 = var4.length;

         for(int var21 = 0; var21 < var20; ++var21) {
            Class var22 = var19[var21];
            var13.visitVarInsn(BCUtil.loadOpcode(var22), var18);
            var18 += BCUtil.sizeOf(var22);
         }
      }

      var13.visitMethodInsn(183, this.superClsName, var2.getName(), var7);
      if (!var8) {
         var13.visitVarInsn(BCUtil.storeOpcode(var5), var11);
      }

      var13.visitLabel(var15);
      var13.visitVarInsn(25, 0);
      var13.visitVarInsn(21, var10);
      var13.visitFieldInsn(181, this.clsName, "__WL_method_state", "I");
      var13.visitMethodInsn(184, WL_ALLOWED_METHODS_HELPER_CLS, "popBean", "()V");
      Label var23 = new Label();
      if (var8) {
         var13.visitJumpInsn(167, var23);
      } else {
         var13.visitVarInsn(BCUtil.loadOpcode(var5), var11);
         var13.visitInsn(BCUtil.returnOpcode(var5));
      }

      var13.visitLabel(var16);
      var13.visitVarInsn(58, var12);
      var13.visitLabel(var17);
      var13.visitVarInsn(25, 0);
      var13.visitVarInsn(21, var10);
      var13.visitFieldInsn(181, this.clsName, "__WL_method_state", "I");
      var13.visitMethodInsn(184, WL_ALLOWED_METHODS_HELPER_CLS, "popBean", "()V");
      var13.visitVarInsn(25, var12);
      var13.visitInsn(191);
      if (var8) {
         var13.visitLabel(var23);
         var13.visitInsn(177);
      }

      var13.visitMaxs(0, 0);
      var13.visitEnd();
   }

   private void addMembers(ClassWriter var1, String var2, Class<?> var3, String var4, String var5) {
      var1.visitField(2, var2, BCUtil.fieldDesc(var3), (String)null, (Object)null).visitEnd();
      BCUtil.addGetter(var1, this.clsName, var4, var2, var3);
      BCUtil.addSetter(var1, this.clsName, var5, var2, var3);
   }

   private Class<?>[] beanInitExs() {
      try {
         return this.sbi.getBeanClass().getConstructor().getExceptionTypes();
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("Class: " + this.sbi.getBeanClass().getName() + " does not have a no-arg constructor. Exception: " + var2);
      }
   }

   private void addCloseExtendedPersistenceContexts(ClassWriter var1) {
      String var2 = BCUtil.binName(ExtendedPersistenceContextWrapper.class);
      MethodVisitor var3 = var1.visitMethod(1, "closeExtendedPersistenceContexts", "()V", (String)null, (String[])null);
      var3.visitCode();
      var3.visitVarInsn(25, 0);
      var3.visitMethodInsn(182, this.clsName, "getExtendedPersistenceContexts", "()Ljava/util/Set;");
      var3.visitVarInsn(58, 1);
      var3.visitVarInsn(25, 1);
      Label var4 = new Label();
      var3.visitJumpInsn(198, var4);
      var3.visitVarInsn(25, 1);
      var3.visitMethodInsn(185, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
      var3.visitVarInsn(58, 2);
      Label var5 = new Label();
      var3.visitLabel(var5);
      var3.visitVarInsn(25, 2);
      var3.visitMethodInsn(185, "java/util/Iterator", "hasNext", "()Z");
      Label var6 = new Label();
      var3.visitJumpInsn(153, var6);
      var3.visitVarInsn(25, 2);
      var3.visitMethodInsn(185, "java/util/Iterator", "next", "()Ljava/lang/Object;");
      var3.visitTypeInsn(192, var2);
      var3.visitVarInsn(58, 3);
      var3.visitVarInsn(25, 3);
      var3.visitMethodInsn(182, var2, "decrementReferenceCount", "()V");
      var3.visitJumpInsn(167, var5);
      var3.visitLabel(var6);
      var3.visitVarInsn(25, 1);
      var3.visitMethodInsn(185, "java/util/Set", "clear", "()V");
      var3.visitLabel(var4);
      var3.visitInsn(177);
      var3.visitMaxs(1, 4);
      var3.visitEnd();
   }
}
