package weblogic.wsee.conversation;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ContinueHeader extends ConversationHeader {
   static final long serialVersionUID = -6280278919348660347L;
   private String serverName;
   private String appVersionId;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public ContinueHeader(String var1) {
      super(var1);
   }

   public ContinueHeader(String var1, String var2) {
      super(var1);
      this.serverName = var2;
   }

   public ContinueHeader(String var1, String var2, String var3) {
      super(var1);
      this.serverName = var2;
      this.appVersionId = var3;
   }

   public ContinueHeader() {
   }

   public QName getName() {
      return ConversationConstants.CONV_HEADER_CONTINUE;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public boolean isServerAssigned() {
      return this.serverName != null;
   }

   public String getServerName() {
      return this.serverName;
   }

   public void setServerName(String var1) {
      this.serverName = var1;
   }

   public String getAppVersionId() {
      return this.appVersionId;
   }

   public void setAppVersionId(String var1) {
      this.appVersionId = var1;
   }

   public void write(Element var1) throws MsgHeaderException {
      super.write(var1);
      if (this.serverName != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conv:serverName", this.serverName);
      }

      if (this.appVersionId != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conv:appVersionId", this.appVersionId);
      }

   }

   public void read(Element var1) throws MsgHeaderException {
      super.read(var1);

      try {
         this.serverName = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "serverName");
         this.appVersionId = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "appVersionId");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not read server name", var3);
      }
   }

   public void toString(ToStringWriter var1) {
      super.toString(var1);
      var1.writeField("serverName", this.serverName);
      var1.writeField("appVersionId", this.appVersionId);
   }

   public String convertToWlw81StringForm() {
      StringBuffer var1 = new StringBuffer();
      if (this.conversationId == null) {
         throw new JAXRPCException("No conversation ID found");
      } else {
         var1.append("cid=" + this.conversationId + ";");
         if (this.serverName != null) {
            var1.append("sn=" + this.serverName + ";");
         }

         return var1.toString();
      }
   }

   public void parseFromWlw81StringForm(String var1) {
      if (var1 == null) {
         throw new JAXRPCException("Input callback information is null");
      } else {
         int var2 = var1.indexOf("cid=");
         if (var2 < 0) {
            throw new JAXRPCException("No conversation ID found in callback information " + var1);
         } else {
            var2 += "cid=".length();
            int var3 = var1.indexOf(";", var2);
            if (var3 <= var2) {
               throw new JAXRPCException("No conversation ID value found in callback information " + var1);
            } else {
               this.conversationId = var1.substring(var2, var3);
               var2 = var1.indexOf("sn=", var3);
               if (var2 >= 0) {
                  var2 += "sn=".length();
                  var3 = var1.indexOf(";", var2);
                  if (var3 <= var2) {
                     throw new JAXRPCException("No server name value found in callback information " + var1);
                  }

                  this.serverName = var1.substring(var2, var3);
               }

            }
         }
      }
   }

   static {
      NAME = ConversationConstants.CONV_HEADER_CONTINUE;
      TYPE = new MsgHeaderType();
   }
}
