package weblogic.xml.security.assertion;

import javax.security.auth.Subject;

/** @deprecated */
public class ServerHelper {
   public static final Subject getSubject(IntegrityAssertion var0) {
      return (Subject)var0.getSubject();
   }

   public static final void setSubject(IntegrityAssertion var0, Subject var1) {
      var0.setSubject(var1);
   }
}
