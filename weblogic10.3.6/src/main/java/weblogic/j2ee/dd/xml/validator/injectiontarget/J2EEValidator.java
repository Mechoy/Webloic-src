package weblogic.j2ee.dd.xml.validator.injectiontarget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import weblogic.utils.ErrorCollectionException;

class J2EEValidator extends BaseValidator {
   protected void checkModifier(Method var1, ErrorCollectionException var2) {
      if (Modifier.isStatic(var1.getModifiers())) {
         var2.add(this.error(var1, "\"" + var1.getName() + "\" cannot be declared as static method"));
      }

   }

   protected void checkModifier(Field var1, ErrorCollectionException var2) {
      if (Modifier.isStatic(var1.getModifiers())) {
         var2.add(this.error(var1, "\"" + var1.getName() + "\" cannot be declared as static field"));
      }

   }
}
