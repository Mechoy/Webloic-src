package weblogic.wsee.cluster;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ServiceIdentityHeader extends MsgHeader {
   public static final String SERVICEIDENTITY_NS = "http://www.bea.com/ServiceIdentity";
   public static final String SERVICEIDENTITY_PREFIX = "serviceidentity";
   public static final String XML_TAG_SERVICEIDENTITY = "ServiceIdentity";
   public static final String XML_TAG_SERVICE_NAME = "ServiceName";
   public static final String XML_TAG_SERVER_NAME = "ServerName";
   public static final QName NAME = new QName("http://www.bea.com/ServiceIdentity", "ServiceIdentity", "serviceidentity");
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String serviceName;
   private String serverName;

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

   public String getServiceName() {
      return this.serviceName;
   }

   public void setServiceName(String var1) {
      this.serviceName = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.serverName = DOMUtils.getValueByTagNameNS(var1, "http://www.bea.com/ServiceIdentity", "ServerName");
         this.serviceName = DOMUtils.getValueByTagNameNS(var1, "http://www.bea.com/ServiceIdentity", "ServiceName");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not read service identity header", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.serverName == null) {
         throw new MsgHeaderException("Server name is null");
      } else if (this.serviceName == null) {
         throw new MsgHeaderException("Service name is null");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, "serviceidentity", "http://www.bea.com/ServiceIdentity");
         DOMUtils.addValueNS(var1, "http://www.bea.com/ServiceIdentity", "serviceidentity:ServerName", this.serverName);
         DOMUtils.addValueNS(var1, "http://www.bea.com/ServiceIdentity", "serviceidentity:ServiceName", this.serviceName);
      }
   }
}
