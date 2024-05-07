package weblogic.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Binding {
   Type value() default Binding.Type.SOAP11;

   public static enum Type {
      SOAP11,
      SOAP12;
   }
}
