package weblogic.xml.security.wsu.v200207;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.SOAPElement;
import weblogic.xml.schema.binding.util.StdNamespace;
import weblogic.xml.security.utils.Utils;
import weblogic.xml.security.utils.ValidationException;
import weblogic.xml.security.utils.XMLReader;
import weblogic.xml.security.utils.XMLSerializable;
import weblogic.xml.security.utils.XMLWriter;
import weblogic.xml.security.wsu.Created;
import weblogic.xml.security.wsu.Expires;
import weblogic.xml.security.wsu.Received;
import weblogic.xml.security.wsu.Timestamp;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public class TimestampImpl extends XMLSerializable implements Timestamp, WSUConstants {
   private String id;
   private Created created;
   private Expires expires;
   private List received;
   private static final String XSD_TRUE = "1";
   private boolean mustUnderstand;

   public TimestampImpl() {
      this.id = null;
      this.created = null;
      this.expires = null;
      this.received = null;
      this.created = new CreatedImpl();
   }

   public TimestampImpl(String var1) {
      this.id = null;
      this.created = null;
      this.expires = null;
      this.received = null;
      this.created = new CreatedImpl(var1);
   }

   public TimestampImpl(Calendar var1) {
      this.id = null;
      this.created = null;
      this.expires = null;
      this.received = null;
      this.created = new CreatedImpl(var1);
   }

   public TimestampImpl(String var1, String var2) {
      this(var1);
      this.expires = new ExpiresImpl(var2);
   }

   public TimestampImpl(XMLInputStream var1) throws XMLStreamException {
      this.id = null;
      this.created = null;
      this.expires = null;
      this.received = null;
      this.fromXMLInternal(var1);
   }

   public TimestampImpl(SOAPElement var1) {
      this.id = null;
      this.created = null;
      this.expires = null;
      this.received = null;
      this.fromXMLInternal(var1);
   }

   public void setId(String var1) {
      if (var1 != null) {
         throw new AssertionError("Id for Timestamp already set");
      } else {
         this.id = var1;
      }
   }

   public void setExpires(long var1) {
      if (this.created == null) {
         this.created = new CreatedImpl();
      }

      Calendar var3 = (Calendar)this.created.getTime().clone();
      var3.add(13, (int)(var1 / 1000L));
      this.setExpires(var3);
   }

   public void setExpires(Calendar var1) {
      this.expires = new ExpiresImpl(var1);
   }

   public void setExpires(String var1) {
      this.expires = new ExpiresImpl(var1);
   }

   public void setMustUnderstand(boolean var1) {
      this.mustUnderstand = var1;
   }

   public boolean getMustUnderstand() {
      return this.mustUnderstand;
   }

   public String getId() {
      if (this.id == null) {
         this.id = Utils.generateId("Timestamp");
      }

      return this.id;
   }

   public Created getCreated() {
      return this.created;
   }

   public Expires getExpires() {
      return this.expires;
   }

   public Received[] getReceived() {
      if (this.received != null) {
         Received[] var1 = new Received[this.received.size()];
         var1 = (Received[])((Received[])this.received.toArray(var1));
         return var1;
      } else {
         return new Received[0];
      }
   }

   public void toXML(XMLWriter var1) {
      var1.writeStartElement(WSU_URI, "Timestamp");
      if (this.id != null) {
         var1.writeAttribute(WSU_URI, "Id", this.getId());
      }

      String var2 = StdNamespace.instance().soapEnvelope();
      if (this.mustUnderstand) {
         var1.writeAttribute(var2, "mustUnderstand", "1");
      }

      if (this.created != null) {
         this.created.toXML(var1);
      }

      if (this.expires != null) {
         this.expires.toXML(var1);
      }

      if (this.received != null) {
         Iterator var3 = this.received.iterator();

         while(var3.hasNext()) {
            ReceivedImpl var4 = (ReceivedImpl)var3.next();
            var4.toXML(var1);
         }
      }

      var1.writeEndElement();
   }

   protected void fromXMLInternal(XMLReader var1) throws ValidationException {
      var1.require(2, WSU_URI, "Timestamp");
      this.id = var1.getAttribute(WSU_URI, "Id");
      int var2 = var1.next();
      String var3;
      String var4;
      if (var2 == 2) {
         var3 = var1.getNamespaceURI();
         var4 = var1.getLocalName();
         if (var3.equals(WSU_URI) && var4.equals("Created")) {
            this.created = new CreatedImpl(var1);
            var2 = var1.getEventType();
         }
      }

      if (var2 == 2) {
         var3 = var1.getNamespaceURI();
         var4 = var1.getLocalName();
         if (var3.equals(WSU_URI) && var4.equals("Expires")) {
            this.expires = new ExpiresImpl(var1);
            var2 = var1.getEventType();
         }
      }

      int var6;
      for(; var2 != 4; var2 = var6) {
         if (var2 != 2) {
            switch (var2) {
               case 0:
                  throw new ValidationException("unexpected end of document");
               case 16:
                  throw new ValidationException("unexpected character data");
               default:
                  throw new ValidationException("problem parsing Timestamp");
            }
         }

         var4 = var1.getNamespaceURI();
         String var5 = var1.getLocalName();
         if (WSU_URI.equals(var4) && "Received".equals(var5)) {
            this.getReceivedList().add(new ReceivedImpl(var1));
            var6 = var1.getEventType();
         } else {
            var6 = skip(var1);
         }
      }

      var1.require(4, WSU_URI, "Timestamp");
      var1.next();
   }

   public String toString() {
      return "weblogic.xml.security.wsu.v200207.TimestampImpl{id='" + this.id + "'" + ", created=" + this.created + ", expires=" + this.expires + ", received=" + (this.received == null ? null : "size:" + this.received.size() + this.received) + "}";
   }

   private List getReceivedList() {
      if (this.received == null) {
         this.received = new ArrayList();
      }

      return this.received;
   }
}
