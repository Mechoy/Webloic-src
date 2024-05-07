package weblogic.xml.security.wsu.v200207;

import java.util.Calendar;
import javax.xml.namespace.QName;
import weblogic.xml.schema.types.XSDDateTime;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.utils.XMLSerializable;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.security.wsu.AttributedDateTime;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public abstract class AttributedDateTimeBase extends XMLSerializable implements WSUConstants, AttributedDateTime {
   private String id;
   private QName valueType;
   private XSDDateTime time;

   public AttributedDateTimeBase() {
      this(Calendar.getInstance(TZ_ZULU));
   }

   public AttributedDateTimeBase(Calendar var1) {
      this.id = null;
      this.valueType = null;
      this.time = null;
      this.time = XSDDateTime.createFromCalendar(var1);
   }

   public AttributedDateTimeBase(String var1) {
      this.id = null;
      this.valueType = null;
      this.time = null;
      this.time = XSDDateTime.createFromXml(var1);
   }

   public AttributedDateTimeBase(XMLInputStream var1) throws XMLStreamException {
      this.id = null;
      this.valueType = null;
      this.time = null;
      this.fromXMLInternal(var1);
   }

   public AttributedDateTimeBase(XMLReader var1) throws ValidationException {
      this.id = null;
      this.valueType = null;
      this.time = null;
      this.fromXMLInternal(var1);
   }

   protected abstract String getLocalName();

   public void setId(String var1) {
      if (var1 != null) {
         throw new AssertionError("Id for AttributedDateTimeBase already set");
      } else {
         this.id = var1;
      }
   }

   public String getId() {
      if (this.id == null) {
         this.id = Utils.generateId(this.getLocalName());
      }

      return this.id;
   }

   public String getTimeString() {
      return this.time.getXml();
   }

   public Calendar getTime() {
      return this.time.getCalendar();
   }

   public QName getValueType() {
      return this.valueType;
   }

   public void toXML(XMLWriter var1) {
      var1.writeStartElement(WSU_URI, this.getLocalName());
      this.writeAttributes(var1);
      var1.writeCharacters(this.getTimeString());
      var1.writeEndElement();
   }

   protected void writeAttributes(XMLWriter var1) {
      if (this.id != null) {
         var1.writeAttribute(WSU_URI, "Id", this.getId());
      }

      if (this.valueType != null) {
         var1.writeAttribute(WSU_URI, "ValueType", this.valueType);
      }

   }

   protected void fromXMLInternal(XMLReader var1) throws ValidationException {
      String var2 = this.getLocalName();
      var1.require(2, WSU_URI, var2);
      this.readAttributes(var1);
      var1.next();
      var1.require(16, (String)null, (String)null);
      String var3 = var1.getText();

      try {
         this.time = XSDDateTime.createFromXml(var3);
      } catch (IllegalArgumentException var5) {
         throw new ValidationException("Failed to deserialize time -" + var3, var5);
      }

      var1.next();
      var1.require(4, WSU_URI, var2);
      var1.next();
   }

   protected void readAttributes(XMLReader var1) {
      this.id = var1.getAttribute(WSU_URI, "Id");
      if (this.id == null) {
         this.id = var1.getAttribute((String)null, "Id");
      }

      this.valueType = var1.getQNameAttribute(WSU_URI, "ValueType");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getLocalName()).append("\n   Time:      ").append(this.getTimeString()).append("\n   Id:        ").append(this.getId()).append("\n   ValueType: ").append(this.getValueType());
      return var1.toString();
   }
}
