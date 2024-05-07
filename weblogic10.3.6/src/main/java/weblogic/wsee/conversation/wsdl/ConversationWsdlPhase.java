package weblogic.wsee.conversation.wsdl;

import org.w3c.dom.Element;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlExtension;
import weblogic.wsee.wsdl.WsdlWriter;

public class ConversationWsdlPhase implements WsdlExtension {
   public static final String KEY = "cwPhase";
   private final ConversationPhase phase;

   public ConversationWsdlPhase(ConversationPhase var1) {
      this.phase = var1;
   }

   public ConversationWsdlPhase(Element var1) throws WsdlException {
      String var2 = var1.getAttribute("phase");
      this.phase = ConversationPhase.valueOf(var2);
      if (this.phase == null) {
         throw new WsdlException(var2 + "is not a valid conversation phase");
      }
   }

   public ConversationPhase getPhase() {
      return this.phase;
   }

   public String getKey() {
      return "cwPhase";
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "transition", "http://www.openuri.org/2002/04/wsdl/conversation/");

      assert var3 != null;

      var3.setAttribute("phase", this.phase.toString());
   }

   public static final ConversationWsdlPhase narrow(WsdlBindingOperation var0) {
      return (ConversationWsdlPhase)var0.getExtension("cwPhase");
   }

   public static final ConversationWsdlPhase attach(WsdlBindingOperation var0, ConversationPhase var1) {
      ConversationWsdlPhase var2 = new ConversationWsdlPhase(var1);
      var0.putExtension(var2);
      return var2;
   }
}
