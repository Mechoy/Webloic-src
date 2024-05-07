package weblogic.xml.security.specs;

import java.util.ArrayList;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.NSOutputStream;
import weblogic.xml.security.utils.NamespaceAwareXOS;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.wsse.v200207.WSSEConstants;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

/** @deprecated */
public class UsernameTokenSpec extends SpecBase {
   private String passType;
   private String realm;
   private static final UsernameTokenSpec DEFAULT_SPEC;

   public UsernameTokenSpec(String var1) {
      this.passType = null;
      this.realm = null;
      this.passType = var1;
   }

   public UsernameTokenSpec(String var1, String var2) {
      this(var1);
      this.realm = var2;
   }

   public UsernameTokenSpec(XMLInputStream var1, String var2) throws XMLStreamException {
      this.passType = null;
      this.realm = null;
      this.fromXMLInternal(var1, var2);
   }

   public String getPasswordType() {
      return this.passType;
   }

   public String getRealm() {
      return this.realm;
   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      StartElement var3 = (StartElement)StreamUtils.getElement(var1, var2, "UsernameTokenSpec");
      this.passType = StreamUtils.getAttribute(var3, "PasswordType");
      StreamUtils.requiredAttr(this.passType, "UsernameTokenSpec", "PasswordType");
      this.realm = StreamUtils.getAttribute(var3, "Realm");
      StreamUtils.closeScope(var1, var2, "UsernameTokenSpec");
   }

   public void toXML(XMLOutputStream var1) throws XMLStreamException {
      this.toXML(var1, "http://www.openuri.org/2002/11/wsse/spec", 0);
   }

   public void toXML(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      ArrayList var4 = new ArrayList();
      if (var1 instanceof NSOutputStream) {
         NSOutputStream var5 = (NSOutputStream)var1;
      } else {
         NamespaceAwareXOS var7;
         var1 = var7 = new NamespaceAwareXOS((XMLOutputStream)var1);
         var7.addPrefix("http://www.openuri.org/2002/11/wsse/spec", "spec");
         var7.addPrefix(WSSEConstants.WSSE_URI, "wsse");
      }

      Attribute[] var8 = new Attribute[var4.size()];
      var4.toArray(var8);
      Attribute[] var6;
      if (this.realm == null) {
         var6 = new Attribute[1];
      } else {
         var6 = new Attribute[]{null, ElementFactory.createAttribute(var2, "Realm", this.realm)};
      }

      var6[0] = StreamUtils.createAttribute("PasswordType", this.passType);
      StreamUtils.addStart((XMLOutputStream)var1, var2, "UsernameTokenSpec", var6, var8, var3);
      StreamUtils.addEnd((XMLOutputStream)var1, var2, "UsernameTokenSpec");
   }

   public static UsernameTokenSpec getDefaultSpec() {
      return DEFAULT_SPEC;
   }

   public String toString() {
      return "weblogic.xml.security.specs.UsernameTokenSpec{passType=" + this.passType + ", realm='" + this.realm + "'" + "}";
   }

   static {
      DEFAULT_SPEC = new UsernameTokenSpec(WSSEConstants.PASSWORDTYPE_PASSWORDTEXT);
   }
}
