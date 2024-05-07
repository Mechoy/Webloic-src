package weblogic.wsee.security.wss.policy;

public interface TimestampPolicy {
   short getMessageAgeSeconds();

   boolean isIncludeTimestamp();

   void setIncludeTimestamp(boolean var1);

   void setMessageAgeSeconds(short var1);

   boolean isSignTimestampRequired();

   void setSignTimestampRequired(boolean var1);

   void setSignTimestampRequired();
}
