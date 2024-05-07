package weblogic.wsee.jws;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Element;
import weblogic.wsee.jws.util.Logger;

public interface JwsContext extends Serializable {
   boolean isFinished();

   void finishConversation();

   void setMaxAge(Date var1) throws IllegalStateException, IllegalArgumentException;

   void setMaxAge(String var1) throws IllegalStateException, IllegalArgumentException;

   long getMaxAge() throws IllegalStateException;

   long getCurrentAge() throws IllegalStateException;

   void resetIdleTime() throws IllegalStateException;

   void setMaxIdleTime(long var1) throws IllegalStateException, IllegalArgumentException;

   void setMaxIdleTime(String var1) throws IllegalStateException, IllegalArgumentException;

   long getMaxIdleTime() throws IllegalStateException;

   long getCurrentIdleTime() throws IllegalStateException;

   Principal getCallerPrincipal();

   boolean isCallerInRole(String var1);

   ServiceHandle getService();

   /** @deprecated */
   @Deprecated
   Logger getLogger(String var1);

   Element[] getInputHeaders();

   void setUnderstoodInputHeaders(boolean var1);

   boolean getUnderstoodInputHeaders();

   void setOutputHeaders(Element[] var1);

   Protocol getProtocol();

   MessageContext getMessageContext();

   public interface Callback {
      void onCreate() throws Exception;

      void onAgeTimeout(long var1) throws Exception;

      void onIdleTimeout(long var1) throws Exception;

      void onFinish(boolean var1) throws Exception;

      void onException(Exception var1, String var2, Object[] var3) throws Exception;

      void onAsyncFailure(String var1, Object[] var2) throws Exception;
   }
}
