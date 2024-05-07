package weblogic.wsee.cluster;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class CorrelationHeader extends MsgHeader {
   public static final String CORRELATION_NS = "http://www.bea.com/correlation";
   public static final String CORRELATION_PREFIX = "correlation";
   public static final String XML_TAG_CORRELATION = "Correlation";
   public static final String XML_TAG_CORRELATION_ID = "CorrelationId";
   public static final QName NAME = new QName("http://www.bea.com/correlation", "Correlation", "correlation");
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String correlationId;

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getCorrelationId() {
      return this.correlationId;
   }

   public void setCorrelationId(String var1) {
      this.correlationId = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.correlationId = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.bea.com/correlation", "CorrelationId");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get server name", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addNamespaceDeclaration(var1, "correlation", "http://www.bea.com/correlation");
      if (this.correlationId != null) {
         DOMUtils.addValueNS(var1, "http://www.bea.com/correlation", "correlation:CorrelationId", this.correlationId);
      }

   }
}
