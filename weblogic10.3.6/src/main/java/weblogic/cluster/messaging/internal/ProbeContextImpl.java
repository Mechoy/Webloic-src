package weblogic.cluster.messaging.internal;

public class ProbeContextImpl implements ProbeContext {
   private SuspectedMemberInfo suspectedMember;
   private int nextAction = 0;
   private String message;
   private int result = 1;

   public ProbeContextImpl(SuspectedMemberInfo var1) {
      this.suspectedMember = var1;
   }

   public SuspectedMemberInfo getSuspectedMemberInfo() {
      return this.suspectedMember;
   }

   public int getNextAction() {
      return this.nextAction;
   }

   public void setNextAction(int var1) {
      this.nextAction = var1;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public int getResult() {
      return this.result;
   }

   public void setResult(int var1) {
      this.result = var1;
   }
}
