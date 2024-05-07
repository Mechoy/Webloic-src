package weblogic.wsee.wsdl.internal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.policy.deployment.PolicyURIs;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlReader;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;

public final class WsdlPortTypeImpl extends WsdlBase implements WsdlPortTypeBuilder {
   private Map<QName, WsdlOperationBuilder> operationList;
   private QName name;
   private WsdlDefinitionsBuilder definitions;
   private PolicyURIs policyUris;

   WsdlPortTypeImpl(WsdlDefinitionsBuilder var1) {
      this.operationList = new LinkedHashMap();
      this.definitions = var1;
   }

   WsdlPortTypeImpl(QName var1, WsdlDefinitionsBuilder var2) {
      this(var2);
      this.name = var1;
   }

   public QName getName() {
      return this.name;
   }

   public Map<QName, WsdlOperationBuilder> getOperations() {
      return this.operationList;
   }

   public WsdlOperationBuilder getOperation(QName var1) {
      return (WsdlOperationBuilder)this.operationList.get(var1);
   }

   public WsdlOperationBuilder addOperation(QName var1) {
      WsdlOperationImpl var2 = new WsdlOperationImpl(var1, this.definitions);
      this.operationList.put(var1, var2);
      return var2;
   }

   public PolicyURIs getPolicyUris() {
      return this.policyUris;
   }

   public void setPolicyUris(PolicyURIs var1) {
      this.policyUris = var1;
   }

   public void parse(Element var1, String var2) throws WsdlException {
      this.addDocumentation(var1);
      String var3 = WsdlReader.getMustAttribute(var1, (String)null, "name");
      this.name = new QName(var2, var3);
      PolicyURIs var4 = this.getPolicyUri(var1);
      if (null != var4) {
         this.policyUris = var4;
      }

      NodeList var5 = var1.getChildNodes();

      for(int var6 = 0; var6 < var5.getLength(); ++var6) {
         Node var7 = var5.item(var6);
         if (!WsdlReader.isWhiteSpace(var7) && !WsdlReader.isDocumentation(var7)) {
            WsdlReader.checkDomElement(var7);
            Element var8 = (Element)var7;
            if (WsdlReader.tagEquals(var8, "operation", WsdlConstants.wsdlNS)) {
               this.parseOperation(var8, var2);
            }
         }
      }

   }

   private void parseOperation(Element var1, String var2) throws WsdlException {
      WsdlOperationImpl var3 = new WsdlOperationImpl(this.definitions);
      var3.parse(var1, var2);
      this.operationList.put(var3.getName(), var3);
   }

   public void write(Element var1, WsdlWriter var2) {
      if (var2.isSameNS(this.getName().getNamespaceURI())) {
         Element var3 = var2.addChild(var1, "portType", WsdlConstants.wsdlNS);
         var2.setAttribute(var3, "name", WsdlConstants.wsdlNS, this.name.getLocalPart());
         if (this.policyUris != null) {
            this.policyUris.write(var3, var2);
         }

         this.writeDocumentation(var3, var2);
         Iterator var4 = this.getOperations().values().iterator();

         while(var4.hasNext()) {
            WsdlOperationBuilder var5 = (WsdlOperationBuilder)var4.next();
            var5.write(var3, var2);
         }

      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("name", this.name);
      var1.writeArray("operationList", this.operationList.values().iterator());
      var1.end();
   }
}
