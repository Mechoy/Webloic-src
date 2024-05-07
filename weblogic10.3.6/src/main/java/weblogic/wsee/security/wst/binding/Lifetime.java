package weblogic.wsee.security.wst.binding;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.marshal.MarshalException;
import weblogic.xml.schema.types.IllegalLexicalValueException;
import weblogic.xml.schema.types.XSDDateTime;

public class Lifetime extends TrustDOMStructure {
   public static final String NAME = "Lifetime";
   private Created created;
   private Expires expires;

   public Lifetime() {
      this.namespaceUri = "source of undefined namespace: " + Thread.currentThread().getStackTrace();
   }

   public Lifetime(String var1) {
      if (var1 != null) {
         this.namespaceUri = var1;
      }

   }

   public void setCreated(Calendar var1, String var2) {
      this.created = new Created(var2);
      this.created.setTime(var1);
   }

   public void setExpires(Calendar var1, String var2) {
      this.expires = new Expires(var2);
      this.expires.setTime(var1);
   }

   public void setPeriod(long var1) {
      this.setPeriod(var1, (String)null);
   }

   public void setPeriod(long var1, String var3) {
      Date var4 = new Date();
      GregorianCalendar var5 = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
      var5.setTime(var4);
      this.setCreated(var5, (String)null);
      GregorianCalendar var6 = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
      var6.setTimeInMillis(var4.getTime() + var1);
      this.setExpires(var6, (String)null);
   }

   public Calendar getCreated() {
      return this.created != null ? this.created.getTime() : null;
   }

   public Calendar getExpires() {
      return this.expires != null ? this.expires.getTime() : null;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (this.created != null) {
         this.created.marshal(var1, (Node)null, var2);
      }

      if (this.expires != null) {
         this.expires.marshal(var1, (Node)null, var2);
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      Element var2 = getElementByTagName(var1, "Created", true);
      if (var2 != null) {
         this.created = new Created(var2.getNamespaceURI());
         this.created.unmarshal(var2);
      }

      Element var3 = getElementByTagName(var1, "Expires", true);
      if (var3 != null) {
         this.expires = new Expires(var3.getNamespaceURI());
         this.expires.unmarshal(var3);
      }

   }

   public String getName() {
      return "Lifetime";
   }

   private abstract static class Time extends TrustDOMStructure {
      protected Calendar time;

      private Time() {
      }

      protected void setTime(Calendar var1) {
         this.time = var1;
      }

      protected Calendar getTime() {
         return this.time;
      }

      protected void initNamespaceUriAndPrefix(String var1) {
         if (var1 != null) {
            this.namespaceUri = var1;
         } else {
            this.namespaceUri = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
         }

         this.prefix = "wsu";
      }

      public void marshalContents(Element var1, Map var2) throws MarshalException {
         addTextContent(var1, XSDDateTime.getXml(this.time));
      }

      public void unmarshalContents(Element var1) throws MarshalException {
         try {
            this.time = XSDDateTime.convertXml(getTextContent(var1));
         } catch (IllegalLexicalValueException var3) {
            throw new MarshalException("Lifetime is invalid.");
         }
      }

      // $FF: synthetic method
      Time(Object var1) {
         this();
      }
   }

   private static final class Expires extends Time {
      public static final String NAME = "Expires";

      public Expires() {
         super(null);
         this.initNamespaceUriAndPrefix((String)null);
      }

      public Expires(String var1) {
         super(null);
         this.initNamespaceUriAndPrefix(var1);
      }

      public String getName() {
         return "Expires";
      }
   }

   private static final class Created extends Time {
      public static final String NAME = "Created";

      public Created() {
         super(null);
         this.initNamespaceUriAndPrefix((String)null);
      }

      public Created(String var1) {
         super(null);
         this.initNamespaceUriAndPrefix(var1);
      }

      public String getName() {
         return "Created";
      }
   }
}
