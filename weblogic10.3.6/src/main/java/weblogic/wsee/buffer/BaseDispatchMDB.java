package weblogic.wsee.buffer;

import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.xml.rpc.JAXRPCException;
import weblogic.ejb.spi.JmsMessageDrivenBean;
import weblogic.jms.extensions.WLSession;
import weblogic.wsee.jws.RetryException;
import weblogic.wsee.jws.container.Duration;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;

public abstract class BaseDispatchMDB implements JmsMessageDrivenBean, MessageDrivenBean, MessageListener {
   private static final long serialVersionUID = 8642792753370642882L;
   private WLSession session = null;
   private static final boolean verbose = Verbose.isVerbose(BaseDispatchMDB.class);

   public void setMessageDrivenContext(MessageDrivenContext var1) {
   }

   public void ejbRemove() {
      if (verbose) {
         Verbose.log((Object)"BaseDispatchMDB removed");
      }

   }

   public void ejbCreate() throws CreateException {
      if (verbose) {
         Verbose.log((Object)"BaseDispatchMDB created");
      }

   }

   public void onMessage(Message var1) {
      long var2 = System.nanoTime();
      if (verbose) {
         Verbose.say(var2 + " Entering BaseDispatchMDB.onMessage()");
      }

      boolean var4 = true;
      long var5 = 0L;
      String var7 = null;

      try {
         var7 = var1.getStringProperty("ASYNC_URI");
         if (verbose) {
            Verbose.say(var2 + " Deliver message to " + var7);
         }

         if (verbose) {
            Verbose.say(var2 + " Before calling onMessage(targetURI, message) from within BaseDispatchMDB.onMessage()");
         }

         this.onMessage(var7, var1);
         if (verbose) {
            Verbose.say(var2 + " After calling onMessage(targetURI, message) from within BaseDispatchMDB.onMessage()");
         }

      } catch (NoRetryException var18) {
         assert var18.getCause() != null;

         if (verbose) {
            Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 1\n");
            Verbose.logException(var18.getCause());
         }

         var4 = false;
         if (var18.getCause() instanceof JAXRPCException) {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 2\n");
               Verbose.logException(var18);
            }

            throw (JAXRPCException)var18.getCause();
         } else {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 2.1");
            }

            throw new JAXRPCException(var18.getCause());
         }
      } catch (Exception var19) {
         if (verbose) {
            Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 3\n");
            Verbose.logException(var19);
         }

         if (var19 instanceof JAXRPCException) {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 4\n");
            }

            Throwable var9 = ((JAXRPCException)var19).getLinkedCause();
            if (var9 instanceof WsException && var9.getCause() instanceof RetryException) {
               if (verbose) {
                  Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 5\n");
               }

               RetryException var10 = (RetryException)var9.getCause();
               if (verbose) {
                  Verbose.say(var2 + " Reset the retry delay from user-defined RetryException:" + var10.getRetryDelay());
               }

               Duration var11 = new Duration(var10.getRetryDelay());
               if (verbose) {
                  Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 6\n");
               }

               long var12 = var11.convertToSeconds(new Date());
               if (verbose) {
                  Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 7\n");
               }

               var5 = var12;
               return;
            }
         }

         if (var19 instanceof RuntimeException) {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 8\n");
               Verbose.logException(var19);
            }

            throw (RuntimeException)var19;
         } else {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 9\n");
               Verbose.logException(var19);
            }

            throw new RuntimeException(var19);
         }
      } finally {
         if (var4) {
            if (verbose) {
               Verbose.say(var2 + " Inside BaseDispatchMDB.onMessage() == 10\n");
            }

            this.setRetryDelay(var1, var7, var5);
         }

      }
   }

   protected abstract void onMessage(String var1, Message var2) throws Exception;

   private void setRetryDelay(Message var1, String var2, long var3) {
      try {
         long var5 = BufferManager.instance().getRetryDelay(var2, var1.getJMSMessageID());
         if (var3 > 0L) {
            if (var3 != var5) {
               var5 = var3;
               BufferManager.instance().putRetryDelay(var2, var1.getJMSMessageID(), var3);
            }
         } else if (var1.propertyExists("BEARetryDelay")) {
            var5 = var1.getLongProperty("BEARetryDelay");
            BufferManager.instance().putRetryDelay(var2, var1.getJMSMessageID(), var5);
         }

         if (var5 > 0L) {
            if (verbose) {
               Verbose.log((Object)("Setting retry delay to " + var5));
               Verbose.log((Object)("Old retryDelay=" + this.session.getRedeliveryDelay()));
            }

            this.session.setRedeliveryDelay(var5 * 1000L);
         }
      } catch (JMSException var7) {
         if (verbose) {
            Verbose.log((Object)"Could not set retry delay");
            Verbose.logException(var7);
         }
      }

   }

   public void setSession(Session var1) {
      this.session = (WLSession)var1;
   }
}
