package weblogic.wsee.async;

public interface AsyncPostCallContext {
   Object getProperty(String var1);

   String getMessageId();

   String getStubName();
}
