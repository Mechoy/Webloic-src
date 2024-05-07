package weblogic.wsee.tools.clientgen.stubgen;

import java.util.Date;
import weblogic.wsee.tools.source.JsFault;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.tools.source.JsParameterType;
import weblogic.wsee.util.jspgen.ScriptException;

public class StubInterface extends StubBase {
   public void generate() throws ScriptException {
      this.out.print("package ");
      this.out.print(this.packageName);
      this.out.print(";");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("/**");
      this.out.print("\n");
      this.out.print(" * Generated interface, do not edit.");
      this.out.print("\n");
      this.out.print(" *");
      this.out.print("\n");
      this.out.print(" * This stub interface was generated by weblogic");
      this.out.print("\n");
      this.out.print(" * webservice stub gen on ");
      this.out.print(new Date());
      this.out.print("  ");
      this.out.print("\n");
      this.out.print(" */");
      this.out.print("\n");
      this.out.print("\n");
      this.out.print("public interface ");
      this.out.print(this.className);
      this.out.print(" extends java.rmi.Remote {");
      this.out.print("\n");
      boolean var1 = true;
      JsMethod[] var2 = this.endpoint.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JsMethod var4 = var2[var3];
         this.out.print("     ");
         this.out.print("\n");
         this.out.print("  /**");
         this.out.print("\n");
         this.out.print("   * Operation Name : ");
         this.out.print(var4.getOperationName());
         this.out.print("   */");
         this.out.print("\n");
         this.out.print("\n");
         this.out.print("  public ");
         this.out.print(this.getTypeFromPart(var4.getReturnType()));
         this.out.print(" ");
         this.out.print(var4.getMethodName());
         this.out.print("(");
         this.out.print(this.argumentString(var4));
         this.out.print(")");
         this.out.print(this.throwException(var4));
         JsParameterType[] var5;
         int var6;
         JsParameterType var7;
         if (var1) {
            this.out.print(";");
            this.out.print("\n");
            this.out.print("  ");
         } else {
            this.out.print(" {");
            this.out.print("\n");
            this.out.print("\n");
            this.out.print("    java.util.ArrayList _args = new java.util.ArrayList();");
            this.out.print("\n");
            var5 = var4.getArguments();

            for(var6 = 0; var6 < var5.length; ++var6) {
               var7 = var5[var6];
               this.out.print("    _args.add(");
               this.out.print(this.wrap(var7));
               this.out.print(");");
               this.out.print("\n");
               this.out.print("    ");
            }

            this.out.print("\n");
            this.out.print("    try {");
            this.out.print("\n");
            this.out.print("      ");
            this.out.print(this.finishCallbackStubConversation(var4));
            this.out.print("      java.lang.Object _result = _invoke(\"");
            this.out.print(var4.getOperationName().getLocalPart());
            this.out.print("\", _args.toArray());");
            this.out.print("\n");
            this.out.print("      ");
            this.out.print(this.getReturnStatement(var4.getReturnType()));
            this.out.print("    } catch (javax.xml.rpc.JAXRPCException _wls_exception) {");
            this.out.print("\n");
            this.out.print("      throw new ");
            this.out.print(this.getRemoteExceptionClass().getName());
            this.out.print("(_wls_exception.getMessage(), _wls_exception.getLinkedCause());");
            this.out.print("\n");
            this.out.print("    } catch (javax.xml.rpc.soap.SOAPFaultException _wls_exception) {");
            this.out.print("\n");
            this.out.print("      throw new ");
            this.out.print(this.getRemoteExceptionClass().getName());
            this.out.print("(_soapFault2String(_wls_exception), _wls_exception);");
            this.out.print("\n");
            this.out.print("    } catch (java.lang.Throwable _wls_exception) {");
            this.out.print("\n");
            this.out.print("\n");
            this.out.print("      ");
            JsFault[] var9 = var4.getFaults();

            for(int var10 = 0; var10 < var9.length; ++var10) {
               JsFault var8 = var9[var10];
               this.out.print("      if (_wls_exception instanceof ");
               this.out.print(var8.getJsr109MappingFileExceptionClass());
               this.out.print(") throw (");
               this.out.print(var8.getJsr109MappingFileExceptionClass());
               this.out.print(")_wls_exception;");
               this.out.print("\n");
               this.out.print("      ");
            }

            this.out.print("\n");
            this.out.print("      throw new ");
            this.out.print(this.getRemoteExceptionClass().getName());
            this.out.print("(_wls_exception.getMessage(), _wls_exception);");
            this.out.print("\n");
            this.out.print("    }");
            this.out.print("\n");
            this.out.print("  }");
         }

         if (!var4.isOneWay() && var4.isGenerateAsync()) {
            this.out.print("  public void ");
            this.out.print(var4.getMethodName());
            this.out.print("Async(weblogic.wsee.async.AsyncPreCallContext apc");
            if (var4.getArguments().length > 0) {
               this.out.print(",");
            }

            this.out.print(" ");
            this.out.print(this.argumentString(var4));
            this.out.print(") throws ");
            this.out.print(this.getRemoteExceptionClass().getName());
            this.out.print(" ");
            if (var1) {
               this.out.print(";");
               this.out.print("\n");
               this.out.print("  ");
            } else {
               this.out.print(" {");
               this.out.print("\n");
               this.out.print("    java.util.ArrayList _args = new java.util.ArrayList();");
               this.out.print("\n");
               this.out.print("\n");
               this.out.print("    _setProperty(weblogic.wsee.async.AsyncConstants.ASYNC_INVOKE_PROPERTY, \"true\");");
               this.out.print("\n");
               this.out.print("    _setProperty(weblogic.wsee.async.AsyncConstants.ASYNC_PRE_CALL_CONTEXT_PROPERTY, apc);");
               this.out.print("\n");
               this.out.print("    _setProperty(weblogic.wsee.async.AsyncConstants.METHOD_NAME_PROPERTY, \"");
               this.out.print(var4.getMethodName());
               this.out.print("\");");
               this.out.print("\n");
               this.out.print("    _setProperty(weblogic.wsee.async.AsyncConstants.OPERATION_NAME_PROPERTY,");
               this.out.print("\n");
               this.out.print("                 \"");
               this.out.print(var4.getOperationName().getLocalPart());
               this.out.print("\");");
               this.out.print("\n");
               this.out.print("    _setProperty(weblogic.wsee.async.AsyncConstants.RETURN_TYPE_PROPERTY, \"");
               this.out.print(this.getTypeFromPart(var4.getReturnType()));
               this.out.print("\");");
               this.out.print("\n");
               var5 = var4.getArguments();

               for(var6 = 0; var6 < var5.length; ++var6) {
                  var7 = var5[var6];
                  this.out.print("    _args.add(");
                  this.out.print(this.wrap(var7));
                  this.out.print(");");
                  this.out.print("\n");
                  this.out.print("    ");
               }

               this.out.print("\n");
               this.out.print("    try {");
               this.out.print("\n");
               this.out.print("      java.lang.Object _result = _invoke(\"");
               this.out.print(var4.getOperationName().getLocalPart());
               this.out.print("\", _args.toArray());");
               this.out.print("\n");
               this.out.print("    } catch (javax.xml.rpc.JAXRPCException _wls_exception) {");
               this.out.print("\n");
               this.out.print("      throw new ");
               this.out.print(this.getRemoteExceptionClass().getName());
               this.out.print("(_wls_exception.getMessage(), _wls_exception.getLinkedCause());");
               this.out.print("\n");
               this.out.print("    } catch (javax.xml.rpc.soap.SOAPFaultException _wls_exception) {");
               this.out.print("\n");
               this.out.print("      throw new ");
               this.out.print(this.getRemoteExceptionClass().getName());
               this.out.print("(_soapFault2String(_wls_exception), _wls_exception);");
               this.out.print("\n");
               this.out.print("    } catch (java.lang.Throwable _wls_exception) {");
               this.out.print("\n");
               this.out.print("      throw new ");
               this.out.print(this.getRemoteExceptionClass().getName());
               this.out.print("(_wls_exception.getMessage(), _wls_exception);");
               this.out.print("\n");
               this.out.print("    }");
               this.out.print("\n");
               this.out.print("    finally {");
               this.out.print("\n");
               this.out.print("      _removeProperty(weblogic.wsee.async.AsyncConstants.ASYNC_INVOKE_PROPERTY);");
               this.out.print("\n");
               this.out.print("      _removeProperty(weblogic.wsee.async.AsyncConstants.ASYNC_PRE_CALL_CONTEXT_PROPERTY);");
               this.out.print("\n");
               this.out.print("      _removeProperty(weblogic.wsee.async.AsyncConstants.METHOD_NAME_PROPERTY);");
               this.out.print("\n");
               this.out.print("      _removeProperty(weblogic.wsee.async.AsyncConstants.RETURN_TYPE_PROPERTY);");
               this.out.print("\n");
               this.out.print("\n");
               this.out.print("    }");
               this.out.print("\n");
               this.out.print("  }");
               this.out.print("\n");
               this.out.print("  ");
            }
         }
      }

      this.out.print("\n");
      this.out.print("}");
   }
}
