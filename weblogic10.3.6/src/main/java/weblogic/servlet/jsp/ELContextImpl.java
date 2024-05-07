package weblogic.servlet.jsp;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class ELContextImpl extends ELContext {
   private ELResolver resolver;
   private FunctionMapper functionMapper;
   private VariableMapper variableMapper;

   ELContextImpl() {
   }

   ELContextImpl(ELResolver var1, VariableMapper var2, FunctionMapper var3) {
      this.resolver = var1;
      this.variableMapper = var2;
      this.functionMapper = var3;
   }

   public ELResolver getELResolver() {
      return this.resolver;
   }

   public FunctionMapper getFunctionMapper() {
      return this.functionMapper;
   }

   public VariableMapper getVariableMapper() {
      return this.variableMapper;
   }

   void setELResolver(ELResolver var1) {
      this.resolver = var1;
   }

   void setFunctionMapper(FunctionMapper var1) {
      this.functionMapper = var1;
   }

   void setVariableMapper(VariableMapper var1) {
      this.variableMapper = var1;
   }
}
