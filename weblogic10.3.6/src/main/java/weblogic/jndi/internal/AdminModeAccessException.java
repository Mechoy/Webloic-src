package weblogic.jndi.internal;

import javax.naming.NameNotFoundException;

public final class AdminModeAccessException extends NameNotFoundException {
   AdminModeAccessException(String var1) {
      super(var1);
   }
}
