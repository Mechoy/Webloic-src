package weblogic.j2ee.dd.xml.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.ErrorCollectionException;

public abstract class AbstractAnnotationValidator implements AnnotationValidator {
   public void validate(DescriptorBean var1, ClassLoader var2) throws ErrorCollectionException {
      ErrorCollectionException var3 = new ErrorCollectionException();
      Class var4 = null;
      Field var5 = null;
      Method var6 = null;

      try {
         this.checkBean(var1, var3);
         var4 = this.getClass(var1, var2);
         var5 = this.getField(var1, var4);
         if (var5 != null) {
            this.checkField(var5, var3);
         }

         var6 = this.getMethod(var1, var4);
         if (var6 != null) {
            this.checkMethod(var6, var3);
         }

         if (var5 != null && var6 != null) {
            this.checkAnnotation(var6, var5, var3);
         }

         if (var5 == null || var6 == null) {
            this.checkUndefinedMethodField(var1, var5, var6, var3);
         }
      } catch (Throwable var8) {
         var3.add(var8);
      }

      if (var3.size() != 0) {
         throw var3;
      }
   }

   protected void checkBean(DescriptorBean var1, ErrorCollectionException var2) {
   }

   protected void checkField(Field var1, ErrorCollectionException var2) {
      this.checkAnnotation(var1, var2);
      this.checkModifier(var1, var2);
   }

   protected void checkMethod(Method var1, ErrorCollectionException var2) {
      this.checkAnnotation(var1, var2);
      this.checkReturnType(var1, var2);
      this.checkException(var1, var2);
      this.checkParameters(var1, var2);
      this.checkModifier(var1, var2);
   }

   protected Method getMethod(DescriptorBean var1, Class var2) {
      return null;
   }

   protected Field getField(DescriptorBean var1, Class var2) {
      return null;
   }

   protected Class getClass(DescriptorBean var1, ClassLoader var2) throws ClassNotFoundException {
      return null;
   }

   protected void checkReturnType(Method var1, ErrorCollectionException var2) {
   }

   protected void checkException(Method var1, ErrorCollectionException var2) {
   }

   protected void checkParameters(Method var1, ErrorCollectionException var2) {
   }

   protected void checkModifier(Method var1, ErrorCollectionException var2) {
   }

   protected void checkModifier(Field var1, ErrorCollectionException var2) {
   }

   protected void checkAnnotation(Method var1, ErrorCollectionException var2) {
   }

   protected void checkAnnotation(Field var1, ErrorCollectionException var2) {
   }

   protected void checkAnnotation(Method var1, Field var2, ErrorCollectionException var3) {
   }

   protected void checkUndefinedMethodField(DescriptorBean var1, Field var2, Method var3, ErrorCollectionException var4) {
   }
}
