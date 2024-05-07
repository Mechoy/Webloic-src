package weblogic.wsee.policy.runtime;

import java.io.InputStream;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;

public class LibraryPolicyFinder extends PolicyFinder {
   public PolicyStatement findPolicy(String var1, String var2) throws PolicyException {
      var1 = checkFileExtension(var1);
      String var3 = "policies/" + var1;
      InputStream var4 = Thread.currentThread().getContextClassLoader().getResourceAsStream(var3);
      return var4 != null ? readPolicyFromStream(var1, var4, true) : null;
   }
}
