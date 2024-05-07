package weblogic.wsee.wsdl;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WsdlPartnerLinkType implements WsdlExtension, WsdlExtensionParser {
   public static final String KEY = "PartnerLinkType";
   public static final QName PLINK = new QName("http://schemas.xmlsoap.org/ws/2003/05/partner-link/", "partnerLinkType");
   private QName name;
   private String role1name;
   private String role2name;
   private QName role1PortTypeName;
   private QName role2PortTypeName;

   public WsdlPartnerLinkType() {
   }

   public WsdlPartnerLinkType(QName var1) {
      this.name = var1;
   }

   public String getKey() {
      return "PartnerLinkType";
   }

   public QName getName() {
      return this.name;
   }

   public String getRole1Name() {
      return this.role1name;
   }

   public String getRole2Name() {
      return this.role2name;
   }

   public QName getPortTypeName(String var1) throws WsdlException {
      if (var1.equals(this.role1name)) {
         return this.role1PortTypeName;
      } else if (var1.equals(this.role2name)) {
         return this.role2PortTypeName;
      } else {
         throw new WsdlException("role name " + var1 + " is not found.");
      }
   }

   public void addRole(String var1, QName var2) throws WsdlException {
      if (this.role1name == null) {
         this.role1name = var1;
         this.role1PortTypeName = var2;
      } else {
         if (this.role2name != null) {
            throw new WsdlException("Failed to add role " + var1 + " to " + "PaterLinkType. There are already two roles " + this.role1name + " and " + this.role2name + " in the wsdl");
         }

         this.role2name = var1;
         this.role2PortTypeName = var2;
      }

   }

   public void parse(Element var1, String var2) throws WsdlException {
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      NodeList var4 = var1.getChildNodes();
      int var5 = 0;

      for(int var6 = 0; var6 < var4.getLength(); ++var6) {
         Node var7 = var4.item(var6);
         if (!WsdlReader.isWhiteSpace(var7)) {
            WsdlReader.checkDomElement(var7);
            if (!"role".equals(var7.getLocalName())) {
               throw new WsdlException("Unknown element '" + var7 + "' under " + "partnerLinkType " + this);
            }

            this.parseRole((Element)var7);
            ++var5;
            if (var5 > 2) {
               throw new WsdlException("There are more than two roles in partnerLinkType " + this.name);
            }
         }
      }

      if (var5 == 0) {
         throw new WsdlException("There is no role found in partnerLinkType " + this.name);
      }
   }

   private void parseRole(Element var1) throws WsdlException {
      String var2 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      QName var3 = null;
      NodeList var4 = var1.getChildNodes();
      int var5 = 0;

      for(int var6 = 0; var6 < var4.getLength(); ++var6) {
         Node var7 = var4.item(var6);
         if (!WsdlReader.isWhiteSpace(var7)) {
            WsdlReader.checkDomElement(var7);
            Element var8 = (Element)var7;
            if (!"portType".equals(var7.getLocalName())) {
               throw new WsdlException("Unknown element '" + var7 + "' under " + "partnerLinkType's role element " + this);
            }

            String var9 = WsdlReader.getMustAttribute(var8, (String)null, "name");
            var3 = WsdlReader.createQName(var8, var9);
            ++var5;
            if (var5 > 1) {
               throw new WsdlException("There is more one portType in partnerLinkType " + this.name + " role " + var2);
            }
         }
      }

      if (var5 == 0) {
         throw new WsdlException("There is no portType found in partnerLinkType " + this.name + " role " + var2);
      } else {
         if (this.role1name == null) {
            this.role1name = var2;
            this.role1PortTypeName = var3;
         } else {
            this.role2name = var2;
            this.role2PortTypeName = var3;
         }

      }
   }

   public void write(Element var1, WsdlWriter var2) {
      if (var2.isSameNS(this.getName().getNamespaceURI())) {
         if (this.role1name != null || this.role2name != null) {
            Element var3 = var2.addChild(var1, PLINK.getLocalPart(), PLINK.getNamespaceURI());
            var2.setAttribute(var3, "name", (String)null, (String)this.name.getLocalPart());
            this.writeRole(var3, var2, this.role1name, this.role1PortTypeName);
            this.writeRole(var3, var2, this.role2name, this.role2PortTypeName);
         }
      }
   }

   private void writeRole(Element var1, WsdlWriter var2, String var3, QName var4) {
      if (var3 != null) {
         Element var5 = var2.addChild(var1, "role", PLINK.getNamespaceURI());
         var5.setAttribute("name", var3);
         Element var6 = var2.addChild(var5, "portType", PLINK.getNamespaceURI());
         var2.setAttribute(var6, "name", (String)null, (QName)var4);
      }
   }

   public void attach(WsdlDefinitions var1) {
      var1.putExtension(this);
   }

   public WsdlExtension parseMessage(WsdlMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseOperation(WsdlOperation var1, Element var2) {
      return null;
   }

   public WsdlExtension parseBinding(WsdlBinding var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingOperation(WsdlBindingOperation var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseBindingMessage(WsdlBindingMessage var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseService(WsdlService var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parsePort(WsdlPort var1, Element var2) throws WsdlException {
      return null;
   }

   public WsdlExtension parseDefinitions(WsdlDefinitions var1, Element var2) throws WsdlException {
      if (PLINK.getLocalPart().equals(var2.getLocalName()) && PLINK.getNamespaceURI().equals(var2.getNamespaceURI())) {
         WsdlPartnerLinkType var3 = new WsdlPartnerLinkType();
         var3.parse(var2, var1.getTargetNamespace());
         return var3;
      } else {
         return null;
      }
   }

   public void cleanUp() {
   }
}
