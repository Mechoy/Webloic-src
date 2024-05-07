package weblogic.j2ee.dd.xml.validator;

import weblogic.descriptor.Visitor;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.j2ee.dd.xml.validator.injectiontarget.InjectionTargetValidatorFactory;
import weblogic.j2ee.dd.xml.validator.lifecyclecallback.LifecycleCallbackValidatorFactory;
import weblogic.j2ee.dd.xml.validator.listener.ListenerBeanValidator;
import weblogic.utils.ErrorCollectionException;

public class AnnotationValidatorVisitor implements Visitor {
   private ClassLoader classLoader = null;
   private ErrorCollectionException errors = null;

   public AnnotationValidatorVisitor(ClassLoader var1) {
      this.classLoader = var1;
      this.errors = new ErrorCollectionException();
   }

   public void visit(AbstractDescriptorBean var1) {
      this.visitLifeCycleCallbackBean(var1);
      this.visitInjectionTargetBean(var1);
      this.visitListenerBean(var1);
   }

   public ErrorCollectionException getErrors() {
      return this.errors;
   }

   private void visitLifeCycleCallbackBean(AbstractDescriptorBean var1) {
      AnnotationValidator var2 = LifecycleCallbackValidatorFactory.getValidator(var1);
      if (var2 != null) {
         try {
            var2.validate(var1, this.classLoader);
         } catch (ErrorCollectionException var4) {
            this.errors.add(var4);
         }
      }

   }

   private void visitInjectionTargetBean(AbstractDescriptorBean var1) {
      AnnotationValidator var2 = InjectionTargetValidatorFactory.getValidator(var1);
      if (var2 != null) {
         try {
            var2.validate(var1, this.classLoader);
         } catch (ErrorCollectionException var4) {
            this.errors.add(var4);
         }
      }

   }

   private void visitListenerBean(AbstractDescriptorBean var1) {
      try {
         ListenerBeanValidator var2 = new ListenerBeanValidator();
         var2.validate(var1, this.classLoader);
      } catch (ErrorCollectionException var3) {
         this.errors.add(var3);
      }

   }
}
