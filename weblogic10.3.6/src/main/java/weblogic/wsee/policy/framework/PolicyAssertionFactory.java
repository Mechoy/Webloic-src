package weblogic.wsee.policy.framework;

import javax.xml.namespace.QName;
import org.w3c.dom.Node;

public abstract class PolicyAssertionFactory {
   public abstract PolicyAssertion createAssertion(Node var1) throws PolicyException;

   public static void registerAssertion(QName var0, String var1) {
      ExternalizationUtils.registerExternalizable(var0, var1);
   }
}
