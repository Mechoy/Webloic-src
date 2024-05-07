package weblogic.jms.dotnet.transport.internal;

class ServiceExecuteUnregister extends ServiceExecute {
   ServiceExecuteUnregister(ServiceWrapper var1) {
      super(var1, ServiceExecute.State.UNREGISTER);
   }

   public void execute() {
      this.wrapper.getService().onUnregister();
   }
}
