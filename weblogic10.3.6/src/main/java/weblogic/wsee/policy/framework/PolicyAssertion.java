package weblogic.wsee.policy.framework;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.wsdl.WsdlWriter;

public abstract class PolicyAssertion implements Externalizable {
   private static final long serialVersionUID = 6865802012902454676L;
   private byte version;
   protected boolean optional = false;
   private PolicySubject subject;
   protected String policyNamespaceUri;

   public PolicyAssertion() {
      this.subject = PolicyAssertion.PolicySubject.undefined;
      this.policyNamespaceUri = "http://schemas.xmlsoap.org/ws/2004/09/policy";
   }

   public boolean getOptional() {
      return this.optional;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void setOptional(boolean var1) {
      this.optional = var1;
   }

   public void setPolicySubject(PolicySubject var1) {
      this.subject = var1;
   }

   public String getPolicyNamespaceUri() {
      return this.policyNamespaceUri;
   }

   public void setPolicyNamespaceUri(String var1) {
      this.policyNamespaceUri = var1;
   }

   public PolicySubject getPolicySubject() {
      return this.subject;
   }

   public abstract Element serialize(Document var1) throws PolicyException;

   public abstract QName getName();

   protected void write(Element var1, WsdlWriter var2) {
      try {
         var1.appendChild(this.serialize(var1.getOwnerDocument()));
      } catch (PolicyException var4) {
         throw new AssertionError(var4);
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.version = ExternalizationUtils.getExtVersion(var1);
      ExternalizationUtils.checkVersion(var1, this.version);
      this.optional = var1.readBoolean();
      this.subject = (PolicySubject)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ExternalizationUtils.writeExtVersion(var1);
      var1.writeBoolean(this.optional);
      var1.writeObject(this.subject);
   }

   public static enum PolicySubject {
      undefined,
      message,
      operation,
      endpoint;
   }
}
