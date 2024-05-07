package weblogic.jms.dotnet.transport.internal;

class ServiceExecuteShutdown extends ServiceExecute {
   ServiceExecuteShutdown(ServiceWrapper var1) {
      super(var1, ServiceExecute.State.SHUTDOWN);
   }

   public void execute() {
      this.wrapper.getService().onShutdown();
   }
}
