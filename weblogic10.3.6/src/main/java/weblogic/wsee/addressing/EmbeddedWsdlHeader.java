package weblogic.wsee.addressing;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.UnknownMsgHeader;
import weblogic.wsee.policy.framework.DOMUtils;

public class EmbeddedWsdlHeader extends UnknownMsgHeader {
   public EmbeddedWsdlHeader() {
   }

   public EmbeddedWsdlHeader(Element var1) {
      super(var1);
   }

   public EmbeddedWsdlHeader(QName var1) {
      super(var1);
   }

   public void write(Element var1) throws MsgHeaderException {
      Element var2 = this.getElement();
      if (var2 == null) {
         throw new AssertionError("EmbeddedWsdlHeader internal Element can't be null!");
      } else {
         if (var2.hasAttributes()) {
            Map var3 = DOMUtils.getAttributeMap(var2);
            Set var4 = var3.keySet();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               var1.setAttribute(var6, (String)var3.get(var6));
            }
         }

         super.write(var1);
      }
   }
}
