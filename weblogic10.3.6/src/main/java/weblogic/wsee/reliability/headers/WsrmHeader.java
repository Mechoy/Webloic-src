package weblogic.wsee.reliability.headers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.CommonHeader;
import weblogic.wsee.reliability2.compat.SimpleElement;

public abstract class WsrmHeader extends CommonHeader {
   private static final long serialVersionUID = -2300983151778747402L;
   private WsrmConstants.RMVersion rmVersion;

   protected WsrmHeader(WsrmConstants.RMVersion var1, String var2) {
      super(new QName(var1.getNamespaceUri(), var2, var1.getPrefix()));
      this.setRmVersion(var1);
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this.rmVersion;
   }

   protected void setRmVersion(WsrmConstants.RMVersion var1) {
      this.rmVersion = var1;
      super.setName(new QName(var1.getNamespaceUri(), this.getName().getLocalPart(), var1.getPrefix()));
   }

   public void setNamespaceUri(String var1) {
      WsrmConstants.RMVersion var2 = WsrmConstants.RMVersion.forNamespaceUri(var1);
      this.setRmVersion(var2);
   }

   public static QName getQName(Class var0, WsrmConstants.RMVersion var1) {
      try {
         Field var2 = var0.getField("LOCAL_NAME");
         String var3 = (String)var2.get((Object)null);
         return new QName(var1.getNamespaceUri(), var3);
      } catch (Exception var4) {
         throw new RuntimeException(var4.toString(), var4);
      }
   }

   public static List<QName> getQNames(Class var0) {
      try {
         ArrayList var1 = new ArrayList();
         WsrmConstants.RMVersion[] var2 = WsrmConstants.RMVersion.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WsrmConstants.RMVersion var5 = var2[var4];
            var1.add(getQName(var0, var5));
         }

         return var1;
      } catch (Exception var6) {
         throw new RuntimeException(var6.toString(), var6);
      }
   }

   protected void setRmVersionFromSimpleElement(SimpleElement var1) {
      String var2 = var1.getName().getNamespaceURI();
      WsrmConstants.RMVersion var3 = WsrmConstants.RMVersion.forNamespaceUri(var2);
      this.setRmVersion(var3);
   }

   protected void writeToIdentifierSubElement(ContentHandler var1, String var2) throws SAXException {
      QName var3 = WsrmConstants.Element.IDENTIFIER.getQName(this.getRmVersion());
      String var4 = var3.getNamespaceURI();
      String var5 = var3.getLocalPart();
      var1.startElement(var4, var5, var5, EMPTY_ATTS);
      var1.characters(var2.toCharArray(), 0, var2.length());
      var1.endElement(var4, var5, var5);
   }

   protected void writeToMessageNumberSubElement(ContentHandler var1, long var2) throws SAXException {
      QName var4 = WsrmConstants.Element.MESSAGE_NUMBER.getQName(this.getRmVersion());
      String var5 = var4.getNamespaceURI();
      String var6 = var4.getLocalPart();
      var1.startElement(var5, var6, var6, EMPTY_ATTS);
      String var7 = Long.toString(var2);
      var1.characters(var7.toCharArray(), 0, var7.length());
      var1.endElement(var5, var6, var6);
   }
}
