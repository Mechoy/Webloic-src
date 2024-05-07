package weblogic.ejb;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

public abstract class GenericMessageDrivenBean extends GenericEnterpriseBean implements MessageDrivenBean {
   private static final long serialVersionUID = 1356540552192329476L;
   private MessageDrivenContext ctx;

   public void setMessageDrivenContext(MessageDrivenContext var1) {
      if (this.isTracingEnabled()) {
         this.trace("setMessageDrivenContext");
      }

      this.ctx = var1;
   }

   protected MessageDrivenContext getMessageDriveContext() {
      return this.getMessageDrivenContext();
   }

   protected MessageDrivenContext getMessageDrivenContext() {
      return this.ctx;
   }

   public void ejbCreate() throws CreateException {
      if (this.isTracingEnabled()) {
         this.trace("ejbCreate");
      }

   }

   public void ejbRemove() {
      if (this.isTracingEnabled()) {
         this.trace("ejbRemove");
      }

   }
}
