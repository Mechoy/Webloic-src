package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public abstract class ArrayAttachmentBase extends AttachmentBase {
   private static final boolean verbose = Verbose.isVerbose(ArrayAttachmentBase.class);

   abstract AttachmentPart createAttachmentPart(String var1, SOAPMessage var2, Object var3, String var4) throws XmlException;

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   public void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, String var7) throws XmlException {
      if (verbose) {
         Verbose.log((Object)("name = " + var5));
      }

      SOAPElement var8 = this.addChildElement(var1, var5);
      if (var2 != null) {
         int var9 = Array.getLength(var2);

         for(int var10 = 0; var10 < var9; ++var10) {
            Object var11 = Array.get(var2, var10);
            String var12 = var5.getLocalPart() + "[" + var10 + "]";
            this.serializeArrayItem(var8, var11, var6, var12, var7);
         }
      }

   }

   private void serializeArrayItem(SOAPElement var1, Object var2, SerializerContext var3, String var4, String var5) throws XmlException {
      SOAPElement var6 = this.addChildElement(var1, new QName("attachment-item"));
      if (var2 != null) {
         this.writeHref(var3.getMessage(), var6, var4);
         SOAPMessage var7 = var3.getMessage();
         AttachmentPart var8 = this.createAttachmentPart(var4, var7, var2, var5);
         var7.addAttachmentPart(var8);
      }

   }

   public Object deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4) throws XmlException {
      Iterator var5 = var1.getChildElements();
      if (!var5.hasNext()) {
         return null;
      } else {
         ArrayList var6 = new ArrayList();

         while(var5.hasNext()) {
            Object var7 = var5.next();
            if (var7 instanceof SOAPElement) {
               SOAPElement var8 = (SOAPElement)var7;
               Object var9 = this.deserializeArrayItem(var8, var4);
               var6.add(var9);
            }
         }

         return this.toArray(var6);
      }
   }

   private Object deserializeArrayItem(SOAPElement var1, DeserializerContext var2) throws XmlException {
      Object var3 = null;
      SOAPMessage var4 = var2.getMessage();
      Name var5 = null;

      try {
         var5 = var4.getSOAPPart().getEnvelope().createName("href");
      } catch (SOAPException var7) {
         throw new XmlException("Failed to get href from SOAP envelope", var7);
      }

      String var6 = var1.getAttributeValue(var5);
      if (var6 != null && !"".equals(var6)) {
         var3 = this.deserializeItem(var6, var4, var3);
      }

      return var3;
   }

   private Object deserializeItem(String var1, SOAPMessage var2, Object var3) throws XmlException {
      var1 = this.cleanCID(var1);
      MimeHeaders var4 = new MimeHeaders();
      var4.addHeader("Content-Id", var1);
      Iterator var5 = var2.getAttachments(var4);
      if (var5.hasNext()) {
         var3 = this.deserializeAttachmentPart((AttachmentPart)var5.next());
      }

      if (var5.hasNext()) {
         throw new XmlException("Found more than one attachment part");
      } else {
         return var3;
      }
   }

   private String cleanCID(String var1) {
      return var1.startsWith("cid:") ? var1.substring("cid:".length()) : var1;
   }

   abstract Object[] toArray(ArrayList var1);
}
