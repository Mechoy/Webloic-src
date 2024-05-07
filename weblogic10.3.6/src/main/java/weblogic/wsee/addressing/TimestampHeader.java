package weblogic.wsee.addressing;

import com.bea.xml.XmlDateTime;
import com.bea.xml.XmlException;
import com.bea.xml.XmlDateTime.Factory;
import java.util.Date;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsutility.WSUtilityConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class TimestampHeader extends MsgHeader {
   public static final QName NAME;
   public static final MsgHeaderType TYPE;
   private Date creationTime;
   private Date expirationTime;

   public TimestampHeader(Date var1) {
      this.creationTime = var1;
   }

   public TimestampHeader(Date var1, Date var2) {
      this.creationTime = var1;
      this.expirationTime = var2;
   }

   public TimestampHeader() {
   }

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public Date getCreationTime() {
      return this.creationTime;
   }

   public void setCreationTime(Date var1) {
      this.creationTime = var1;
   }

   public Date getExpirationTime() {
      return this.expirationTime;
   }

   public void setExpirationTime(Date var1) {
      this.expirationTime = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.creationTime = this.readDateTime(var1, "Created");
         this.expirationTime = this.readDateTime(var1, "Expires");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not parse the timestamp header", var3);
      } catch (XmlException var4) {
         throw new MsgHeaderException("Ill-formed dateTime in timestamp", var4);
      }
   }

   private Date readDateTime(Element var1, String var2) throws DOMProcessingException, XmlException {
      String var3 = DOMUtils.getOptionalValueByTagNameNS(var1, "http://schemas.xmlsoap.org/ws/2002/07/utility", var2);
      if (var3 != null) {
         XmlDateTime var4 = Factory.newInstance();
         var4.setStringValue(var3);
         return var4.getDateValue();
      } else {
         return null;
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.creationTime != null) {
         this.writeDateTime(var1, "Created", this.creationTime);
      }

      if (this.expirationTime != null) {
         this.writeDateTime(var1, "Expires", this.expirationTime);
      }

   }

   private void writeDateTime(Element var1, String var2, Date var3) {
      XmlDateTime var4 = Factory.newInstance();
      var4.setDateValue(var3);
      DOMUtils.addValueNS(var1, "http://schemas.xmlsoap.org/ws/2002/07/utility", var2, var4.getStringValue());
   }

   static {
      NAME = WSUtilityConstants.WSU_HEADER_TIMESTAMP;
      TYPE = new MsgHeaderType();
   }
}
