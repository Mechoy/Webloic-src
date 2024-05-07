package weblogic.j2ee.dd.xml.validator.lifecyclecallback;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import weblogic.utils.ErrorCollectionException;

class J2EEValidator extends BaseValidator {
   protected void checkModifier(Method var1, ErrorCollectionException var2) {
      if (Modifier.isStatic(var1.getModifiers())) {
         var2.add(this.error(var1, "it cannot be declared as static"));
      }

   }
}
