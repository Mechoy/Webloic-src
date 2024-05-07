package weblogic.security.spi;

import java.util.Vector;
import javax.security.auth.Subject;

/** @deprecated */
public interface CredentialMapper {
   String USER_PASSWORD_TYPE = "weblogic.UserPassword";

   Vector getCredentials(Subject var1, Subject var2, Resource var3, String[] var4);

   Object getCredentials(Subject var1, String var2, Resource var3, String[] var4);
}
