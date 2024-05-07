package weblogic.wsee.reliability;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SAFServerHeader extends MsgHeader {
   static final long serialVersionUID = 9156688460997770947L;
   public static final String SAFSERVER_NS = "http://www.bea.com/safserver";
   public static final String XML_TAG_SAFSERVER = "SAFServer";
   private static final String SAFSERVER_PREFIX = "safserver";
   private static final String XML_TAG_SERVER_NAME = "ServerName";
   private static final String XML_TAG_CONVERSATION_KEY = "ConversationKey";
   public static final QName NAME = new QName("http://www.bea.com/safserver", "SAFServer", "safserver");
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String serverName;
   private String conversationKey;

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getServerName() {
      return this.serverName;
   }

   public void setServerName(String var1) {
      this.serverName = var1;
   }

   public String getConversationKey() {
      return this.conversationKey;
   }

   public void setConversationKey(String var1) {
      this.conversationKey = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.serverName = DOMUtils.getValueByTagNameNS(var1, "http://www.bea.com/safserver", "ServerName");
         this.conversationKey = DOMUtils.getValueByTagNameNS(var1, "http://www.bea.com/safserver", "ConversationKey");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not read SAFServerHeader", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.serverName == null) {
         throw new MsgHeaderException("Server name is null");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, "safserver", "http://www.bea.com/safserver");
         DOMUtils.addValueNS(var1, "http://www.bea.com/safserver", "safserver:ServerName", this.serverName);
         DOMUtils.addValueNS(var1, "http://www.bea.com/safserver", "safserver:ConversationKey", this.conversationKey);
      }
   }
}
