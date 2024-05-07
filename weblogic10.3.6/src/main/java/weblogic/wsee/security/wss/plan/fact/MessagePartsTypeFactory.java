package weblogic.wsee.security.wss.plan.fact;

import java.util.Arrays;
import weblogic.wsee.security.policy.MessagePartsEvaluator;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.util.Verbose;

public class MessagePartsTypeFactory {
   private static final boolean verbose = Verbose.isVerbose(MessagePartsTypeFactory.class);
   private static final boolean debug = false;

   public static MessagePartsType newInstance(String var0, String var1) {
      MessagePartsType var2 = MessagePartsType.Factory.newInstance();
      if (var0 == null || var0.equals("")) {
         var0 = "http://www.w3.org/TR/1999/REC-xpath-19991116";
      }

      if (!Arrays.asList(MessagePartsEvaluator.ALL_DIALECTS).contains(var0)) {
         var0 = "http://www.w3.org/TR/1999/REC-xpath-19991116";
      }

      var2.setDialect(var0);
      var2.set(var1);
      return var2;
   }
}
