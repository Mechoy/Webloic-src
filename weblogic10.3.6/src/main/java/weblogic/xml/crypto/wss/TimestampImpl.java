package weblogic.xml.crypto.wss;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.wss.api.Timestamp;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.schema.types.IllegalLexicalValueException;
import weblogic.xml.schema.types.XSDDateTime;

public class TimestampImpl implements Timestamp {
   private String id;
   private Calendar created;
   private boolean useCreated;
   private String serializedCreated;
   private Calendar expires;
   private int expiresSeconds = 0;
   private String serializedExpires;

   public TimestampImpl() {
   }

   public TimestampImpl(String var1, Calendar var2, Calendar var3) {
      this.id = var1;
      this.created = var2;
      this.expires = var3;
   }

   public TimestampImpl(String var1, boolean var2, int var3) {
      this.id = var1;
      this.useCreated = var2;
      this.expiresSeconds = var3;
   }

   public String getId() {
      return this.id;
   }

   public Calendar getCreated() {
      return this.created;
   }

   public Calendar getExpires() {
      return this.expires;
   }

   public List getContent() {
      return null;
   }

   public void marshal(Element var1, Node var2, Map var3) throws MarshalException {
      String var4 = (String)var3.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      Element var5 = DOMUtils.createElement(var1, WSSConstants.TIMESTAMP_QNAME, var4);
      if (this.id != null) {
         DOMUtils.addPrefixedAttribute(var5, WSSConstants.WSU_ID_QNAME, var4, this.id);
      }

      if (this.useCreated) {
         this.createCreated();
      }

      if (this.created != null) {
         this.serializedCreated = this.marshalTime(this.created, var5, WSSConstants.CREATED_QNAME, var4);
      }

      if (this.expiresSeconds != 0) {
         this.createExpires();
      }

      if (this.expires != null) {
         this.serializedExpires = this.marshalTime(this.expires, var5, WSSConstants.EXPIRES_QNAME, var4);
      }

      if (var2 != null) {
         var1.insertBefore(var5, var2);
      } else {
         var1.appendChild(var5);
      }

   }

   private String marshalTime(Calendar var1, Element var2, QName var3, String var4) {
      String var5 = XSDDateTime.getXml(var1);
      Element var6 = DOMUtils.createAndAddElement(var2, var3, var4);
      DOMUtils.addText(var6, var5);
      return var5;
   }

   public void unmarshal(Node var1) throws MarshalException {
      Element var2 = (Element)var1;
      this.id = DOMUtils.getAttributeValue(var2, WSSConstants.WSU_ID_QNAME);
      Element var3 = DOMUtils.getFirstElement(var2);
      if (DOMUtils.is(var3, WSSConstants.CREATED_QNAME)) {
         this.unmarshalCreated(var3);
         Element var4 = DOMUtils.getNextElement(var3);
         if (DOMUtils.is(var4, WSSConstants.EXPIRES_QNAME)) {
            this.unmarshalExpires(var4);
         }

      } else {
         throw new MarshalException(WSSConstants.CREATED_QNAME + " is required in " + WSSConstants.TIMESTAMP_QNAME);
      }
   }

   private void unmarshalCreated(Element var1) throws MarshalException {
      this.serializedCreated = DOMUtils.getText(var1);
      if (this.serializedCreated == null) {
         throw new MarshalException("Element " + WSSConstants.CREATED_QNAME + " is empty.");
      } else {
         this.ensureUTC(this.serializedCreated);

         try {
            this.created = XSDDateTime.convertXml(this.serializedCreated);
         } catch (IllegalLexicalValueException var3) {
            throw new MarshalException("Element " + WSSConstants.CREATED_QNAME + " has invalid value.");
         }
      }
   }

   private void unmarshalExpires(Element var1) throws MarshalException {
      this.serializedExpires = DOMUtils.getText(var1);
      this.ensureUTC(this.serializedExpires);
      this.expires = XSDDateTime.convertXml(this.serializedExpires);
   }

   private void ensureUTC(String var1) throws MarshalException {
      if (!var1.endsWith("-00:00") && !var1.endsWith("+00:00") && !var1.endsWith("Z")) {
         throw new MarshalException("xsd:dateTime element does not have required UTC time zone.");
      }
   }

   private void createCreated() {
      this.created = getCalendar();
   }

   private void createExpires() {
      this.expires = getCalendar();
      if (this.created != null) {
         this.expires.set(1, this.created.get(1));
         this.expires.set(2, this.created.get(2));
         this.expires.set(5, this.created.get(5));
         this.expires.set(10, this.created.get(10));
         this.expires.set(12, this.created.get(12));
         this.expires.set(13, this.created.get(13));
      }

      this.expires.add(13, this.expiresSeconds);
   }

   public static Calendar getCalendar() {
      GregorianCalendar var0 = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
      var0.set(14, 0);
      return var0;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }
}
