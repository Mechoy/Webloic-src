package weblogic.wsee.server.servlet;

import java.util.ArrayList;
import java.util.List;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.deploy.DeployInfoUtil;
import weblogic.wsee.util.ToStringWriter;

class ProcessorFactory {
   private static ProcessorFactory factory = new ProcessorFactory();
   private List<Processor> processors = new ArrayList();
   private List<Processor> processorsNoWsdl = new ArrayList();

   private ProcessorFactory() {
      this.processorsNoWsdl.add(new SoapProcessor());
      this.processorsNoWsdl.add(new IndexPageProcessor());
      this.processorsNoWsdl.add(new TestPageProcessor());
      this.processorsNoWsdl.add(new ConsolePageProcessor());
      this.processorsNoWsdl.add(new UnknownProcessor());
      this.processors.addAll(this.processorsNoWsdl);
      this.processors.add(2, new WsdlRequestProcessor());
      this.processors.add(5, new ServiceInfoProcessor());
   }

   static ProcessorFactory instance() {
      return factory;
   }

   List<Processor> getProcessorList(DeployInfo var1) {
      return DeployInfoUtil.exposeWsdl(var1) ? this.processors : this.processorsNoWsdl;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
