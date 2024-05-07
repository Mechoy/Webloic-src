package weblogic.wsee.addressing;

import java.util.Iterator;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class SetCookieHeader extends MsgHeader {
   public static final QName NAME;
   public static final MsgHeaderType TYPE;
   private MsgHeaders headers = new FreeStandingMsgHeaders();

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public MsgHeaders getCookies() {
      return this.headers;
   }

   public void read(Element var1) throws MsgHeaderException {
      SoapMsgHeaders var2 = new SoapMsgHeaders(var1);
      Iterator var3 = var2.listHeaders();

      while(var3.hasNext()) {
         MsgHeader var4 = (MsgHeader)var3.next();
         this.headers.addHeader(var4);
      }

   }

   public void write(Element var1) throws MsgHeaderException {
      Iterator var2 = this.headers.listHeaders();

      while(var2.hasNext()) {
         MsgHeader var3 = (MsgHeader)var2.next();
         var3.writeToParent(var1);
      }

   }

   static {
      NAME = WSAddressingConstants.WSAX_HEADER_SET_COOKIE;
      TYPE = new MsgHeaderType();
   }
}
