package weblogic.rmi.internal.wls;

import java.io.IOException;
import weblogic.kernel.QueueThrottleException;
import weblogic.rmi.RMILogger;
import weblogic.rmi.extensions.NotImplementedException;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.internal.ReplyOnError;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.InvokeHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.work.ServerWorkAdapter;
import weblogic.work.WorkRejectedException;

public class WLSExecuteRequest extends ServerWorkAdapter {
   private final BasicServerRef ref;
   protected final InboundRequest request;
   private final RuntimeMethodDescriptor md;
   private final InvokeHandler invoker;
   private final AuthenticatedSubject as;
   private boolean underExecution;

   public WLSExecuteRequest(BasicServerRef var1, InboundRequest var2, RuntimeMethodDescriptor var3, InvokeHandler var4, AuthenticatedSubject var5) {
      super(var5);
      this.ref = var1;
      this.as = var5;
      this.request = var2;
      this.md = var3;
      this.invoker = var4;
   }

   public String toString() {
      return this.ref.getImplementationClassName();
   }

   private boolean isAdminRequest() {
      return this.as != null && SubjectUtils.doesUserHaveAnyAdminRoles(this.as);
   }

   public boolean isTransactional() {
      return this.request.getTxContext() != null;
   }

   public Runnable overloadAction(final String var1) {
      return this.ref.getObjectID() > 256 && !this.md.isOneway() && !this.isTransactional() ? new Runnable() {
         public void run() {
            try {
               new ReplyOnError(WLSExecuteRequest.this.request, WLSExecuteRequest.this.request.getOutboundResponse(), new QueueThrottleException(var1));
            } catch (IOException var2) {
               RMILogger.logException("Unable to send error response to client", var2);
            }

         }
      } : null;
   }

   public Runnable cancel(final String var1) {
      if (this.underExecution) {
         return this.disconnectEndPointTask();
      } else {
         return this.isAdminRequest() ? null : new Runnable() {
            public void run() {
               try {
                  new ReplyOnError(WLSExecuteRequest.this.request, WLSExecuteRequest.this.request.getOutboundResponse(), new WorkRejectedException(var1));
               } catch (IOException var2) {
                  RMILogger.logException("Unable to send error response to client", var2);
               }

            }
         };
      }
   }

   private Runnable disconnectEndPointTask() {
      return new Runnable() {
         public void run() {
            try {
               EndPoint var1 = WLSExecuteRequest.this.request.getEndPoint();
               if (var1 != null) {
                  var1.disconnect();
               }
            } catch (NotImplementedException var2) {
            }

         }
      };
   }

   public void run() {
      this.underExecution = true;
      this.ref.handleRequest(this.request, this.invoker);
      this.underExecution = false;
   }
}
