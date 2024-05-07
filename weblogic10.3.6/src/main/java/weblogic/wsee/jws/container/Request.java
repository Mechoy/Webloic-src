package weblogic.wsee.jws.container;

import java.lang.reflect.Method;

public class Request {
   private Class _targetClass;
   private final Class[] _argTypes;
   private final Object[] _argValues;
   private Method _method;
   private final String _targetClassName;
   private final String _methodName;

   public Request(Class var1, String var2, Class[] var3, Object[] var4) {
      this._targetClass = var1;
      this._argTypes = var3;
      this._argValues = var4;
      this._targetClassName = var1.getName();
      this._methodName = var2;

      try {
         this._method = var1.getMethod(var2, var3);
      } catch (NoSuchMethodException var6) {
         throw new IllegalArgumentException("method not found on target.", var6);
      }
   }

   public Request(String var1, String var2, Class[] var3, Object[] var4) {
      this._argTypes = var3;
      this._argValues = var4;
      this._targetClassName = var1;
      this._methodName = var2;
   }

   public Class getTargetClass() {
      if (this._targetClass == null) {
         try {
            this._targetClass = Thread.currentThread().getContextClassLoader().loadClass(this._targetClassName);
         } catch (ClassNotFoundException var2) {
            throw new IllegalArgumentException("Class not found", var2);
         }
      }

      return this._targetClass;
   }

   public Method getMethod() {
      if (this._method == null) {
         try {
            this._method = this.getTargetClass().getMethod(this._methodName, this._argTypes);
         } catch (NoSuchMethodException var2) {
            throw new IllegalArgumentException("method not found on target.", var2);
         }
      }

      return this._method;
   }

   public Object[] getMethodArgValues() {
      return this._argValues;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("Request\n");
      var1.append("_targetClass    = " + this._targetClass + "\n");
      var1.append("_argTypes       = " + this._argTypes + "\n");

      int var2;
      for(var2 = 0; var2 < this._argTypes.length; ++var2) {
         var1.append("\t_argTypes[" + var2 + "] = " + this._argTypes[var2].toString() + "\n");
      }

      var1.append("_argValues      = " + this._argValues + "\n");

      for(var2 = 0; var2 < this._argValues.length; ++var2) {
         var1.append("\t_argValues[" + var2 + "] = " + this._argValues[var2].toString() + "\n");
      }

      var1.append("_method         = " + this._method + "\n");
      return var1.toString();
   }
}
