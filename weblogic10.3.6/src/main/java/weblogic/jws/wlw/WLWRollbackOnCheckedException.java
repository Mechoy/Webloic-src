package weblogic.jws.wlw;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @deprecated */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Deprecated
public @interface WLWRollbackOnCheckedException {
   String ROLLBACK_ON_CHECKED_EXCEPTION = "weblogic.jws.wlw.rollback_on_checked_exception";
}
