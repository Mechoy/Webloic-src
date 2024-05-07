package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.MessageDrivenBean;
import javax.jms.Message;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.ErrorCollectionException;

public class MessageDrivenBeanClassChecker extends BaseComplianceChecker {
   private Class m_beanClass;
   private String m_ejbName;
   private MessageDrivenBeanInfo m_beanInfo;
   private Class messagingTypeClass;

   public MessageDrivenBeanClassChecker(BeanInfo var1) {
      this.m_beanInfo = (MessageDrivenBeanInfo)var1;
      this.m_beanClass = var1.getBeanClass();
      this.m_ejbName = var1.getEJBName();
      this.messagingTypeClass = ((MessageDrivenBeanInfo)var1).getMessagingTypeInterfaceClass();
   }

   public void checkMessageDrivenImplementsMDB() throws ComplianceException {
      if (!this.m_beanInfo.isEJB30() && !MessageDrivenBean.class.isAssignableFrom(this.m_beanClass)) {
         throw new ComplianceException(this.fmt.BEAN_CLASS_IMPLEMENTS_MESSAGE_DRIVEN(this.m_ejbName));
      }
   }

   public void checkMessageDrivenImplementsMessageListener() throws ComplianceException {
      if (!this.m_beanInfo.isEJB30()) {
         if (!this.messagingTypeClass.isAssignableFrom(this.m_beanClass)) {
            throw new ComplianceException(this.fmt.BEAN_CLASS_IMPLEMENTS_MESSAGE_LISTENER(this.m_ejbName, this.m_beanInfo.getMessagingTypeInterfaceName()));
         }
      }
   }

   public void checkMessageDrivenBeanClassIsPublic() throws ComplianceException {
      int var1 = this.m_beanClass.getModifiers();
      if (!Modifier.isPublic(var1)) {
         throw new ComplianceException(this.fmt.PUBLIC_BEAN_CLASS(this.m_ejbName));
      }
   }

   public void checkMessageDrivenBeanClassIsNotFinal() throws ComplianceException {
      int var1 = this.m_beanClass.getModifiers();
      if (Modifier.isFinal(var1)) {
         throw new ComplianceException(this.fmt.FINAL_BEAN_CLASS(this.m_ejbName));
      }
   }

   public void checkMessageDrivenBeanClassIsNotAbstract() throws ComplianceException {
      int var1 = this.m_beanClass.getModifiers();
      if (Modifier.isAbstract(var1)) {
         throw new ComplianceException(this.fmt.ABSTRACT_BEAN_CLASS(this.m_ejbName));
      }
   }

   public void checkBeanClassDoesNotDefineFinalize() throws ComplianceException {
      try {
         Method var1 = this.m_beanClass.getMethod("finalize", (Class[])null);
         throw new ComplianceException(this.fmt.NO_FINALIZE_IN_BEAN(this.m_ejbName));
      } catch (NoSuchMethodException var2) {
      }
   }

   public void checkBeanClassHasPublicNoArgCtor() throws ComplianceException {
      if (!ComplianceUtils.classHasPublicNoArgCtor(this.m_beanClass)) {
         throw new ComplianceException(this.fmt.PUBLIC_NOARG_BEAN_CTOR(this.m_ejbName));
      }
   }

   private void validateOnMessageMethod(Method var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      int var3 = var1.getModifiers();
      if (!Modifier.isPublic(var3)) {
         var2.add(new ComplianceException(this.fmt.PUBLIC_ONMESSAGE(this.m_ejbName)));
      }

      if (Modifier.isFinal(var3)) {
         var2.add(new ComplianceException(this.fmt.FINAL_ONMESSAGE(this.m_ejbName)));
      }

      if (Modifier.isStatic(var3)) {
         var2.add(new ComplianceException(this.fmt.STATIC_ONMESSAGE(this.m_ejbName)));
      }

      Class[] var4 = var1.getParameterTypes();
      if (var4 != null && var4.length == 1) {
         if (!Message.class.isAssignableFrom(var4[0])) {
            var2.add(new ComplianceException(this.fmt.ONMESSAGE_TAKES_MESSAGE(this.m_ejbName)));
         }
      } else {
         var2.add(new ComplianceException(this.fmt.SINGLE_ONMESSAGE_REQUIRED(this.m_ejbName)));
      }

      if (!Void.TYPE.equals(var1.getReturnType())) {
         var2.add(new ComplianceException(this.fmt.ONMESSAGE_RETURNS_VOID(this.m_ejbName)));
      }

      Class[] var5 = var1.getExceptionTypes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (ComplianceUtils.isApplicationException(var5[var6])) {
            var2.add(new ComplianceException(this.fmt.ONMESSAGE_THROWS_APP_EXCEPTION(this.m_ejbName)));
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   private void validateMessagingTypeMethod(Method var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      String var3 = var1.getName();
      int var4 = var1.getModifiers();
      if (!Modifier.isPublic(var4)) {
         var2.add(new ComplianceException(this.fmt.BUS_METHOD_NOT_PUBLIC(this.m_ejbName, var3)));
      }

      if (Modifier.isFinal(var4)) {
         var2.add(new ComplianceException(this.fmt.BUS_METHOD_MUST_NOT_FINAL(this.m_ejbName, var3)));
      }

      if (Modifier.isStatic(var4)) {
         var2.add(new ComplianceException(this.fmt.BUS_METHOD_MUST_NOT_STATIC(this.m_ejbName, var3)));
      }

      Class[] var5 = var1.getExceptionTypes();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (ComplianceUtils.isApplicationException(var5[var6])) {
            var2.add(new ComplianceException(this.fmt.ONMESSAGE_THROWS_APP_EXCEPTION(this.m_ejbName)));
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   protected List getMessagingTypeMethods() {
      ArrayList var1 = new ArrayList();
      if (this.messagingTypeClass != null) {
         Method[] var2 = this.messagingTypeClass.getMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.add(var2[var3]);
         }
      }

      return var1;
   }

   public void checkMessagingTypeMethods() throws ErrorCollectionException, ComplianceException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      if (this.m_beanInfo.getIsWeblogicJMS()) {
         try {
            Method var2 = this.m_beanClass.getMethod("onMessage", Message.class);
            this.validateOnMessageMethod(var2);
         } catch (ErrorCollectionException var8) {
            var1.add(var8);
         } catch (NoSuchMethodException var9) {
            var1.add(new ComplianceException(this.fmt.BEAN_MUST_HAVE_ONMESSAGE(this.m_ejbName)));
         }
      } else {
         Iterator var10 = this.getMessagingTypeMethods().iterator();

         while(var10.hasNext()) {
            Method var3 = (Method)var10.next();
            Method var4 = null;

            try {
               var4 = ClassUtils.getDeclaredMethod(this.m_beanClass, var3.getName(), var3.getParameterTypes());
               this.validateMessagingTypeMethod(var4);
            } catch (ErrorCollectionException var6) {
               var1.add(var6);
            } catch (NoSuchMethodException var7) {
               var1.add(new ComplianceException(this.fmt.MT_METHOD_DOESNT_EXIST_IN_BEAN(this.m_ejbName, DDUtils.getMethodSignature(var4))));
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void validateCreateMethod(Method var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      int var3 = var1.getModifiers();
      if (!Modifier.isPublic(var3)) {
         var2.add(new ComplianceException(this.fmt.PUBLIC_EJBCREATE(this.m_ejbName)));
      }

      if (Modifier.isFinal(var3)) {
         var2.add(new ComplianceException(this.fmt.FINAL_EJBCREATE(this.m_ejbName)));
      }

      if (Modifier.isStatic(var3)) {
         var2.add(new ComplianceException(this.fmt.STATIC_EJBCREATE(this.m_ejbName)));
      }

      if (!Void.TYPE.equals(var1.getReturnType())) {
         var2.add(new ComplianceException(this.fmt.EJBCREATE_RETURNS_VOID(this.m_ejbName)));
      }

      if (!ComplianceUtils.methodTakesNoArgs(var1)) {
         var2.add(new ComplianceException(this.fmt.MESSAGE_NOARG_EJBCREATE(this.m_ejbName)));
      }

      Class[] var4 = var1.getExceptionTypes();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (ComplianceUtils.isApplicationException(var4[var5])) {
            var2.add(new ComplianceException(this.fmt.MESSAGE_EJBCREATE_THROWS_APP_EXCEPTION(this.m_ejbName)));
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkEjbCreateMethod() throws ErrorCollectionException, ComplianceException {
      if (!this.m_beanInfo.isEJB30()) {
         try {
            Method var1 = this.m_beanClass.getMethod("ejbCreate", (Class[])null);
            this.validateCreateMethod(var1);
         } catch (NoSuchMethodException var2) {
            throw new ComplianceException(this.fmt.MESSAGE_DEFINES_EJBCREATE(this.m_ejbName));
         }
      }
   }

   private void validateRemoveMethod(Method var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException();
      int var3 = var1.getModifiers();
      if (!Modifier.isPublic(var3)) {
         var2.add(new ComplianceException(this.fmt.PUBLIC_EJBREMOVE(this.m_ejbName)));
      }

      if (Modifier.isFinal(var3)) {
         var2.add(new ComplianceException(this.fmt.FINAL_EJBREMOVE(this.m_ejbName)));
      }

      if (Modifier.isStatic(var3)) {
         var2.add(new ComplianceException(this.fmt.STATIC_EJBREMOVE(this.m_ejbName)));
      }

      if (!Void.TYPE.equals(var1.getReturnType())) {
         var2.add(new ComplianceException(this.fmt.EJBREMOVE_RETURNS_VOID(this.m_ejbName)));
      }

      if (!ComplianceUtils.methodTakesNoArgs(var1)) {
         var2.add(new ComplianceException(this.fmt.MESSAGE_NOARG_EJBREMOVE(this.m_ejbName)));
      }

      Class[] var4 = var1.getExceptionTypes();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (ComplianceUtils.isApplicationException(var4[var5])) {
            var2.add(new ComplianceException(this.fmt.MESSAGE_EJBREMOVE_THROWS_APP_EXCEPTION(this.m_ejbName)));
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   public void checkEjbRemoveMethod() throws ErrorCollectionException, ComplianceException {
      if (!this.m_beanInfo.isEJB30()) {
         try {
            Method var1 = this.m_beanClass.getMethod("ejbRemove", (Class[])null);
            this.validateRemoveMethod(var1);
         } catch (NoSuchMethodException var2) {
            throw new ComplianceException(this.fmt.MESSAGE_DEFINES_EJBREMOVE(this.m_ejbName));
         }
      }
   }

   public void checkTimeoutMethods() throws ErrorCollectionException, ComplianceException {
      TimeoutCheckHelper.validateTimeoutMethod(this.m_beanInfo);
   }

   public void checkTransactionAttribute() throws ComplianceException {
      Iterator var1 = this.m_beanInfo.getAllMessagingTypeMethodInfos().iterator();

      short var3;
      do {
         MethodInfo var2;
         if (!var1.hasNext()) {
            if (this.m_beanInfo.isTimerDriven()) {
               var2 = this.m_beanInfo.getBeanMethodInfo(DDUtils.getMethodSignature(this.m_beanInfo.getTimeoutMethod().getName(), new String[]{"javax.ejb.Timer"}));
               var3 = var2.getTransactionAttribute();
               if (1 != var3 && 3 != var3 && 0 != var3) {
                  throw new ComplianceException(this.fmt.EJB_TIMEOUT_BAD_TX_ATTRIBUTE(this.m_beanInfo.getDisplayName()));
               }
            }

            return;
         }

         var2 = (MethodInfo)var1.next();
         var3 = var2.getTransactionAttribute();
      } while(1 == var3 || 0 == var3);

      throw new ComplianceException(this.fmt.MESSAGE_BAD_TX_ATTRIBUTE(this.m_ejbName));
   }

   public void checkMaxBeansInFreePoolGreaterThanZero() throws ComplianceException {
      int var1 = this.m_beanInfo.getCachingDescriptor().getMaxBeansInFreePool();
      if (var1 <= 0) {
         throw new ComplianceException(this.fmt.MESSAGE_ILLEGAL_MAX_BEANS_IN_FREE_POOL(this.m_ejbName, var1), new DescriptorErrorInfo("<max-beans-in-free-pool>", this.m_ejbName, (Object)null));
      }
   }
}
