package weblogic.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Conversational {
   String maxIdleTime() default "0 seconds";

   String maxAge() default "1 day";

   boolean runAsStartUser() default false;

   boolean singlePrincipal() default false;
}
