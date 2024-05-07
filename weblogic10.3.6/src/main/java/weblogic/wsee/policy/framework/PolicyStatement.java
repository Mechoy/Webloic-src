package weblogic.wsee.policy.framework;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyStatement extends PolicyExpression implements Externalizable {
   private static final long serialVersionUID = -3722431287020676872L;
   private static final boolean verbose = Verbose.isVerbose(PolicyStatement.class);
   protected String id;

   public PolicyStatement() {
   }

   protected PolicyStatement(String var1) {
      super(OperatorType.ALL);
      this.id = var1;
   }

   public PolicyStatement(String var1, String var2) {
      this(var1);
      this.policyNamespaceUri = var2;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public static PolicyStatement createPolicyStatement(String var0) {
      return new PolicyStatement(var0);
   }

   public Element toXML(Document var1) throws PolicyException {
      Element var2;
      if ("http://schemas.xmlsoap.org/ws/2004/09/policy".equals(this.policyNamespaceUri)) {
         var2 = DOMUtils.createElement(PolicyConstants.POLICY_STATEMENT_ELEMENT, var1, "wsp");
      } else {
         var2 = DOMUtils.createElement(PolicyConstants.POLICY_STATEMENT_ELEMENT_15, var1, "wsp");
      }

      if (this.id != null && this.id.length() > 0) {
         DOMUtils.addAttribute(var2, PolicyConstants.POLICY_STATEMENT_ID_ATTRIBUTE, this.id);
      }

      Iterator var3 = this.expressions.iterator();

      while(var3.hasNext()) {
         PolicyExpression var4 = (PolicyExpression)var3.next();
         var2.appendChild(var4.toXML(var1));
      }

      return var2;
   }

   public void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "Policy", this.policyNamespaceUri);
      if (this.id != null) {
         var2.setAttribute(var3, PolicyConstants.POLICY_STATEMENT_ID_ATTRIBUTE.getLocalPart(), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", this.id);
      }

      Iterator var4 = this.expressions.iterator();

      while(var4.hasNext()) {
         PolicyExpression var5 = (PolicyExpression)var4.next();
         var5.write(var3, var2);
      }

   }

   public Element toXML() throws PolicyException {
      return this.toXML(DOMUtils.getParser().newDocument());
   }

   public String toString() {
      DocumentBuilder var1 = DOMUtils.getParser();
      Document var2 = var1.newDocument();
      Element var3 = null;

      try {
         var3 = this.toXML(var2);
      } catch (PolicyException var5) {
         if (verbose) {
            Verbose.log((Object)var5);
         }

         return null;
      }

      var2.appendChild(var3);
      return DOMUtils.toXMLString(var2);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.id = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.id);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof PolicyStatement) {
            PolicyStatement var2 = (PolicyStatement)var1;
            if (var2.getId() != null && this.getId() != null) {
               return var2.getId().equals(this.getId());
            }
         }

         return false;
      }
   }
}
