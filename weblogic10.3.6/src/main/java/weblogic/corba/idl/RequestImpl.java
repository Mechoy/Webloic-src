package weblogic.corba.idl;

import java.io.IOException;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnknownUserException;
import org.omg.CORBA.UserException;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.ReplyMessage;
import weblogic.iiop.RequestMessage;
import weblogic.iiop.Utils;

public class RequestImpl extends Request {
   private RemoteDelegateImpl delegate;
   private final NVList arguments;
   private final String operationName;
   private final NamedValue returnValue;
   private final ExceptionList exceptions;
   private final Environment env;
   private RequestMessage deferredRequest;
   private final Object target;
   private TypeCode returnType;
   private static final String RESULT = "result";

   RequestImpl(Object var1, RemoteDelegateImpl var2, String var3, NVList var4, NamedValue var5, ExceptionList var6) {
      this.delegate = var2;
      this.operationName = var3;
      this.target = var1;
      if (var4 == null) {
         this.arguments = new NVListImpl(2);
      } else {
         this.arguments = var4;
      }

      if (var6 == null) {
         this.exceptions = new ExceptionListImpl();
      } else {
         this.exceptions = var6;
      }

      if (var5 == null) {
         this.returnValue = new NamedValueImpl("result", new AnyImpl(), 2);
      } else {
         this.returnValue = var5;
      }

      this.env = new EnvironmentImpl();
   }

   private RequestMessage marshalArgs(boolean var1) throws Bounds {
      IIOPOutputStream var2 = (IIOPOutputStream)this.delegate.request((Object)null, this.operationName, var1);
      int var3 = 0;

      while(var3 < this.arguments.count()) {
         NamedValue var4 = this.arguments.item(var3);
         switch (var4.flags()) {
            case 1:
            case 3:
               var2.write_any(var4.value(), var4.value().type());
            default:
               ++var3;
         }
      }

      return (RequestMessage)var2.getMessage();
   }

   private void unmarshalArgs(IIOPInputStream var1) throws Bounds {
      if (this.returnType != null) {
         this.returnValue.value().type(this.returnType);
         var1.read_any(this.returnValue.value(), this.returnType);
      }

      int var2 = 0;

      while(var2 < this.arguments.count()) {
         NamedValue var3 = this.arguments.item(var2);
         switch (var3.flags()) {
            case 2:
            case 3:
               var1.read_any(var3.value(), var3.value().type());
            default:
               ++var2;
         }
      }

      this.env.clear();
   }

   public void invoke() {
      IIOPInputStream var1 = null;
      if (this.delegate == null) {
         throw new BAD_INV_ORDER("invoke() already called", 1330446346, CompletionStatus.COMPLETED_NO);
      } else {
         try {
            RequestMessage var18 = this.marshalArgs(true);
            var1 = (IIOPInputStream)this.delegate.invoke(this.target, var18);
            this.unmarshalArgs(var1);
         } catch (RemarshalException var13) {
            this.invoke();
         } catch (ApplicationException var14) {
            ApplicationException var2 = var14;

            try {
               for(int var3 = 0; var3 < this.exceptions.count(); ++var3) {
                  if (this.exceptions.item(var3).id().equals(var2.getId())) {
                     AnyImpl var4 = new AnyImpl();
                     var4.read_value(var2.getInputStream(), this.exceptions.item(var3));
                     this.env.exception(new UnknownUserException(var4));
                  }
               }
            } catch (UserException var12) {
            }
         } catch (Bounds var15) {
            throw new AssertionError(var15.toString() + " thrown");
         } catch (SystemException var16) {
            this.env.exception(var16);
            throw var16;
         } finally {
            this.delegate.releaseReply((Object)null, var1);
            this.delegate = null;
         }

      }
   }

   public Object target() {
      return this.target;
   }

   public String operation() {
      return this.operationName;
   }

   public NamedValue result() {
      return this.returnValue;
   }

   public NVList arguments() {
      return this.arguments;
   }

   public Environment env() {
      return this.env;
   }

   public ExceptionList exceptions() {
      return this.exceptions;
   }

   public ContextList contexts() {
      throw new NO_IMPLEMENT();
   }

   public void ctx(Context var1) {
      throw new NO_IMPLEMENT();
   }

   public Context ctx() {
      throw new NO_IMPLEMENT();
   }

   public Any add_in_arg() {
      return this.arguments.add(1).value();
   }

   public Any add_named_in_arg(String var1) {
      return this.arguments.add_item(var1, 1).value();
   }

   public Any add_inout_arg() {
      return this.arguments.add(3).value();
   }

   public Any add_named_inout_arg(String var1) {
      return this.arguments.add_item(var1, 3).value();
   }

   public Any add_out_arg() {
      return this.arguments.add(2).value();
   }

   public Any add_named_out_arg(String var1) {
      return this.arguments.add_item(var1, 2).value();
   }

   public void set_return_type(TypeCode var1) {
      this.returnType = var1;
   }

   public Any return_value() {
      if (this.returnType == null) {
         throw new BAD_INV_ORDER("Cannot access return value before invoke");
      } else {
         return this.returnValue.value();
      }
   }

   public void send_oneway() {
      java.lang.Object var1 = null;
      if (this.delegate == null) {
         throw new BAD_INV_ORDER("send_oneway() already called", 1330446346, CompletionStatus.COMPLETED_NO);
      } else {
         try {
            RequestMessage var2 = this.marshalArgs(false);
            this.delegate.sendOneway(var2);
            this.delegate = null;
         } catch (Bounds var3) {
            throw new AssertionError(var3.toString() + " thrown");
         } catch (SystemException var4) {
            this.env.exception(var4);
            throw var4;
         }
      }
   }

   public void send_deferred() {
      if (this.deferredRequest == null && this.delegate != null) {
         try {
            this.deferredRequest = this.marshalArgs(true);
            this.delegate.sendDeferred(this.deferredRequest);
         } catch (Bounds var2) {
            throw new AssertionError(var2.toString() + " thrown");
         } catch (SystemException var3) {
            this.env.exception(var3);
            throw var3;
         }
      } else {
         throw new BAD_INV_ORDER("send_deferred() already called", 1330446346, CompletionStatus.COMPLETED_NO);
      }
   }

   public boolean poll_response() {
      if (this.delegate == null) {
         throw new BAD_INV_ORDER("poll_response() after invoke()", 1330446349, CompletionStatus.COMPLETED_NO);
      } else if (this.deferredRequest == null) {
         throw new BAD_INV_ORDER("poll_response() without outstanding request", 1330446347, CompletionStatus.COMPLETED_NO);
      } else {
         return this.deferredRequest.pollResponse();
      }
   }

   public void get_response() throws WrongTransaction {
      if (this.delegate == null) {
         throw new BAD_INV_ORDER("get_response() after invoke()", 1330446349, CompletionStatus.COMPLETED_NO);
      } else if (this.deferredRequest == null) {
         throw new BAD_INV_ORDER("get_response() without outstanding request", 1330446347, CompletionStatus.COMPLETED_NO);
      } else {
         IIOPInputStream var1 = null;

         try {
            ReplyMessage var20 = (ReplyMessage)this.deferredRequest.getReply();
            var1 = this.delegate.postInvoke(this.target, this.deferredRequest, var20);
            this.unmarshalArgs(var1);
         } catch (RemarshalException var14) {
            this.env.exception(var14);
         } catch (ApplicationException var15) {
            ApplicationException var2 = var15;

            try {
               for(int var21 = 0; var21 < this.exceptions.count(); ++var21) {
                  if (this.exceptions.item(var21).id().equals(var2.getId())) {
                     AnyImpl var4 = new AnyImpl();
                     var4.read_value(var2.getInputStream(), this.exceptions.item(var21));
                     this.env.exception(new UnknownUserException(var4));
                  }
               }
            } catch (UserException var13) {
            }
         } catch (Bounds var16) {
            throw new AssertionError(var16.toString() + " thrown");
         } catch (IOException var17) {
            SystemException var3 = Utils.mapToCORBAException(var17);
            this.env.exception(var3);
            throw var3;
         } catch (SystemException var18) {
            this.env.exception(var18);
            throw var18;
         } finally {
            this.deferredRequest = null;
            this.delegate.releaseReply((Object)null, var1);
            this.delegate = null;
         }

      }
   }
}
