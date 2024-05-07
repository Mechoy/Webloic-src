package weblogic.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface WSDL {
   boolean exposed() default true;
}
