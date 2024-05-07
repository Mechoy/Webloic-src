package weblogic.wsee.jaxws.framework.policy;

import java.util.List;

public class PolicySubjectBindingImpl implements PolicySubjectBinding {
   private List<PolicyReference> references = null;
   private PolicySubjectMetadata metadata = null;

   public PolicySubjectBindingImpl(List<PolicyReference> var1, PolicySubjectMetadata var2) {
      this.references = var1;
      this.metadata = var2;
   }

   public List<PolicyReference> getPolicyReferences() {
      return this.references;
   }

   public PolicySubjectMetadata getPolicySubjectMetadata() {
      return this.metadata;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName());
      var1.append(", references=").append(this.references != null ? this.references.toString() : "null");
      var1.append(", metadata=").append(this.metadata);
      return var1.toString();
   }
}
