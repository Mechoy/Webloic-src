package weblogic.jws.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface UserDataConstraint {
   Transport transport() default UserDataConstraint.Transport.NONE;

   public static enum Transport {
      NONE,
      INTEGRAL,
      CONFIDENTIAL;
   }
}
