package weblogic.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Conversation {
   Phase value() default Conversation.Phase.CONTINUE;

   public static enum Phase {
      START,
      CONTINUE,
      FINISH,
      /** @deprecated */
      @Deprecated
      NONE;
   }
}
