package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTransactionHelper;
import com.bea.core.jatmi.internal.TuxedoXid;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import weblogic.application.AppClassLoaderManager;
import weblogic.kernel.ExecuteRequest;
import weblogic.kernel.ExecuteThread;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.wtc.WTCLogger;
import weblogic.wtc.jatmi.InvokeInfo;
import weblogic.wtc.jatmi.Objrecv;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.SessionAcallDescriptor;
import weblogic.wtc.jatmi.TGIOPUtil;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TPRequestAsyncReply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TdomTcb;
import weblogic.wtc.jatmi.TdomTranTcb;
import weblogic.wtc.jatmi.TuxedoService;
import weblogic.wtc.jatmi.TuxedoServiceHome;
import weblogic.wtc.jatmi.TypedBuffer;
import weblogic.wtc.jatmi.UserTcb;
import weblogic.wtc.jatmi.dsession;
import weblogic.wtc.jatmi.rdsession;
import weblogic.wtc.jatmi.tcm;
import weblogic.wtc.jatmi.tfmh;

public class InboundEJBRequestKEQ implements ExecuteRequest {
   private ServiceParameters myParam;
   private OatmialServices myServices;
   private TDMLocal myLocalDomain;
   private TDMRemote myRemoteDomain;
   private TuxedoXid myXid;
   private SessionAcallDescriptor myConvDesc;
   private XAResource wlsXaResource;
   private String myName;

   InboundEJBRequestKEQ(ServiceParameters var1, TDMLocal var2, TDMRemote var3) {
      this.myParam = var1;
      this.myServices = WTCService.getOatmialServices();
      this.myLocalDomain = var2;
      this.myRemoteDomain = var3;
      this.myXid = null;
      this.myConvDesc = null;
      this.wlsXaResource = null;
   }

   public void execute(ExecuteThread var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/InboundEJBRequestKEQ/execute/" + var1);
      }

      TDMExport var7 = null;
      TypedBuffer var14 = null;
      int var15 = 0;
      int var16 = 0;
      int var20 = -1;
      rdsession var21 = null;
      TdomTranTcb var22 = null;
      Object[] var23 = null;
      if (this.myParam == null) {
         if (var2) {
            ntrace.doTrace("]/InboundEJBRequestKEQ/execute/10");
         }

      } else {
         InvokeInfo var3;
         if ((var3 = this.myParam.get_invokeInfo()) == null) {
            if (var2) {
               ntrace.doTrace("]/InboundEJBRequestKEQ/execute/20");
            }

         } else {
            dsession var4;
            if ((var4 = (dsession)this.myParam.get_gwatmi()) == null) {
               if (var2) {
                  ntrace.doTrace("]/InboundEJBRequestKEQ/execute/30");
               }

            } else {
               Serializable var5;
               if ((var5 = var3.getReqid()) == null) {
                  if (var2) {
                     ntrace.doTrace("]/InboundEJBRequestKEQ/execute/40");
                  }

               } else {
                  tfmh var18;
                  if ((var18 = var3.getServiceMessage()) == null) {
                     var4.send_failure_return(var5, new TPException(4), var20);
                     if (var2) {
                        ntrace.doTrace("]/InboundEJBRequestKEQ/execute/50");
                     }

                  } else {
                     TdomTcb var19;
                     if (var18.tdom != null && (var19 = (TdomTcb)var18.tdom.body) != null) {
                        if ((var20 = var19.get_convid()) != -1) {
                           this.myConvDesc = new SessionAcallDescriptor(var20, true);
                           var21 = var4.get_rcv_place();
                        }

                        String var6;
                        if ((var6 = var3.getServiceName()) != null && !var6.equals("")) {
                           TPException var63;
                           if (var18.route != null) {
                              if (var2) {
                                 ntrace.doTrace("]/InboundEJBRequestKEQ/execute/75");
                              }

                              if (var18.user == null) {
                                 if (var20 == -1) {
                                    var4.send_failure_return(var5, new TPException(12), var20);
                                 } else {
                                    var21.remove_rplyObj(this.myConvDesc);
                                 }

                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/80");
                                 }
                              } else {
                                 if (var18.tdomtran != null && (var22 = (TdomTranTcb)var18.tdomtran.body) != null) {
                                    if (var22 == null) {
                                       var63 = new TPException(4, "NULL transaction");
                                       var4.send_failure_return(var5, var63, var20);
                                       if (var2) {
                                          ntrace.doTrace("*]/InboundEJBRequestKEQ/execute/81");
                                       }

                                       return;
                                    }

                                    try {
                                       this.myXid = new TuxedoXid(var22);
                                    } catch (TPException var49) {
                                       var4.send_failure_return(var5, var49, var20);
                                       if (var2) {
                                          ntrace.doTrace("*]/InboundEJBRequestKEQ/execute/81.5");
                                       }

                                       return;
                                    }

                                    if ((this.wlsXaResource = TCTransactionHelper.getXAResource()) == null) {
                                       var4.send_failure_return(var5, new TPException(14), var20);
                                       if (var2) {
                                          ntrace.doTrace("]/InboundEJBRequestKEQ/execute/82");
                                       }

                                       return;
                                    }

                                    try {
                                       this.wlsXaResource.start(this.myXid, 0);
                                    } catch (XAException var39) {
                                       var4.send_failure_return(var5, new TPException(14), var20);
                                       if (var2) {
                                          ntrace.doTrace("]/InboundEJBRequestKEQ/execute/83/XAException" + var39);
                                       }

                                       return;
                                    }

                                    if (var2) {
                                       ntrace.doTrace("]/InboundEJBRequestKEQ/transaction started/");
                                    }

                                    this.myServices.addInboundRdomToXid(this.myXid, this.myRemoteDomain);
                                    var23 = new Object[]{this.myXid, this.wlsXaResource, this.myRemoteDomain};
                                 }

                                 Objrecv var64 = new Objrecv(var18);
                                 MethodParameters var65 = new MethodParameters(this.myParam, var64, var23, var4);

                                 try {
                                    TGIOPUtil.injectMsgIntoRMI(var18, var65);
                                 } catch (IOException var37) {
                                    throw new RuntimeException(var37);
                                 }

                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/85");
                                 }
                              }

                           } else if ((var7 = WTCService.getWTCService().getExportedService(var6, this.myLocalDomain.getAccessPoint())) == null) {
                              if (var20 == -1) {
                                 var4.send_failure_return(var5, new TPException(6), var20);
                              } else {
                                 var21.remove_rplyObj(this.myConvDesc);
                              }

                              if (var2) {
                                 ntrace.doTrace("]/InboundEJBRequestKEQ/execute/90");
                              }

                           } else {
                              this.myParam.setUser();
                              if (var18.tdomtran != null && (var22 = (TdomTranTcb)var18.tdomtran.body) != null) {
                                 if (var22 == null) {
                                    var63 = new TPException(4, "NULL transaction");
                                    this.myParam.removeUser();
                                    var4.send_failure_return(var5, var63, var20);
                                    if (var2) {
                                       ntrace.doTrace("*]/InboundEJBRequestKEQ/execute/150");
                                    }

                                    return;
                                 }

                                 try {
                                    this.myXid = new TuxedoXid(var22);
                                 } catch (TPException var40) {
                                    this.myParam.removeUser();
                                    var4.send_failure_return(var5, var40, var20);
                                    if (var2) {
                                       ntrace.doTrace("*]/InboundEJBRequestKEQ/execute/150.5");
                                    }

                                    return;
                                 }

                                 if ((this.wlsXaResource = TCTransactionHelper.getXAResource()) == null) {
                                    var4.send_failure_return(var5, new TPException(14), var20);
                                    this.myParam.removeUser();
                                    if (var2) {
                                       ntrace.doTrace("]/InboundEJBRequestKEQ/execute/160");
                                    }

                                    return;
                                 }

                                 try {
                                    this.wlsXaResource.start(this.myXid, 0);
                                 } catch (XAException var44) {
                                    var4.send_failure_return(var5, new TPException(14), var20);
                                    this.myParam.removeUser();
                                    if (var2) {
                                       ntrace.doTrace("*]/InboundEJBRequestKEQ/execute/180/" + var44);
                                    }

                                    return;
                                 }

                                 if (var2) {
                                    ntrace.doTrace("transaction started");
                                 }

                                 this.myServices.addInboundRdomToXid(this.myXid, this.myRemoteDomain);
                              }

                              String var8 = var7.getEJBName();
                              if (var8 == null || "".equals(var8)) {
                                 String var9;
                                 if ((var9 = var7.getTargetClass()) != null) {
                                    TPRequestAsyncReplyImpl var62 = new TPRequestAsyncReplyImpl(this.myParam, this.myRemoteDomain, this.myXid);
                                    if (var2) {
                                       ntrace.doTrace("/InboundEJBRequestKEQ/run/looking up POJO " + var9);
                                    }

                                    Object var25 = this.getClass().getClassLoader();
                                    String var10;
                                    if ((var10 = var7.getTargetJar()) != null) {
                                       if (var10.endsWith(".jar")) {
                                          try {
                                             var25 = new POJOClassLoader(var10, (ClassLoader)var25);
                                          } catch (IOException var51) {
                                             if (var2) {
                                                ntrace.doTrace("]/InboundEJBRequestKEQ/run/problem loading jar " + var10 + ": " + var51);
                                             }

                                             var25 = this.getClass().getClassLoader();
                                          }
                                       } else {
                                          var25 = AppClassLoaderManager.getAppClassLoaderManager().findModuleLoader(var10, (String)null);
                                          if (var2) {
                                             ntrace.doTrace("/InboundEJBRequestKEQ/run/using Application Class Loader for " + var10 + " " + var25);
                                          }

                                          if (var25 == null) {
                                             var25 = this.getClass().getClassLoader();
                                          }
                                       }
                                    }

                                    Class var26 = null;
                                    Method var27 = null;
                                    Class[] var28 = new Class[]{TPServiceInformation.class, TPRequestAsyncReply.class};

                                    try {
                                       var26 = Class.forName(var9, true, (ClassLoader)var25);
                                    } catch (ClassNotFoundException var45) {
                                       this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6, "Could not find " + var9 + " : " + var45));
                                       if (var2) {
                                          ntrace.doTrace("*]/InboundEJBRequestKEQ/run/190/" + var45);
                                       }

                                       return;
                                    }

                                    try {
                                       var27 = var26.getMethod(var7.getResourceName(), var28);
                                    } catch (NoSuchMethodException var38) {
                                       this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6, "Method " + var7.getResourceName() + " not found:" + var38));
                                       if (var2) {
                                          ntrace.doTrace("]/InboundEJBRequestKEQ/run/200/" + var38);
                                       }

                                       return;
                                    }

                                    long var29 = this.myLocalDomain.getBlockTime();
                                    if (var22 != null) {
                                       var29 = (long)(var22.getTransactionTimeout() * 1000);
                                    }

                                    InboundTimer var32 = null;
                                    if (var29 > 0L) {
                                       Timer var31 = this.myServices.getTimeService();
                                       var32 = new InboundTimer(var4, var5, var20, var62);
                                       var62.setTimeoutTask(var32);

                                       try {
                                          if (var2) {
                                             ntrace.doTrace("/InboundEJBRequestKEQ/run/Set up timer: " + var29 + " milliseconds");
                                          }

                                          var31.schedule(var32, var29);
                                       } catch (IllegalArgumentException var42) {
                                          this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(12, "Cannot schedule timer for " + var9 + ": " + var42));
                                          if (var2) {
                                             ntrace.doTrace("]/InboundEJBRequestKEQ/run/204/" + var42);
                                          }

                                          return;
                                       } catch (IllegalStateException var43) {
                                          this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(12, "Cannot schedule timer for " + var9 + ": " + var43));
                                          if (var2) {
                                             ntrace.doTrace("]/InboundEJBRequestKEQ/run/205/" + var43);
                                          }

                                          return;
                                       }
                                    }

                                    try {
                                       Object[] var33 = new Object[]{var3, var62};
                                       var27.invoke(var26.newInstance(), var33);
                                    } catch (InstantiationException var46) {
                                       this.cleanUp(var20, var4, var5, var32, new TPException(12, "Exception in POJO service " + var9 + ": " + var46));
                                       if (var2) {
                                          ntrace.doTrace("]/InboundEJBRequestKEQ/run/210/" + var46);
                                       }

                                       return;
                                    } catch (IllegalAccessException var47) {
                                       this.cleanUp(var20, var4, var5, var32, new TPException(12, "Exception in POJO service " + var9 + ": " + var47));
                                       if (var2) {
                                          ntrace.doTrace("]/InboundEJBRequestKEQ/run/220/" + var47);
                                       }

                                       return;
                                    } catch (InvocationTargetException var48) {
                                       Throwable var34 = var48.getTargetException();
                                       if (var34 instanceof TPException) {
                                          this.cleanUp(var20, var4, var5, var32, (TPException)var34);
                                          if (var2) {
                                             ntrace.doTrace("]/InboundEJBRequestKEQ/run/230/" + var34);
                                          }
                                       } else {
                                          this.cleanUp(var20, var4, var5, var32, new TPException(12, "Exception in POJO service " + var9 + ": " + (Exception)var34));
                                          if (var2) {
                                             ntrace.doTrace("]/InboundEJBRequestKEQ/run/240/" + var34);
                                          }
                                       }

                                       return;
                                    }

                                    if (this.wlsXaResource != null) {
                                       try {
                                          this.wlsXaResource.end(this.myXid, 67108864);
                                       } catch (XAException var41) {
                                          XAException var66 = var41;
                                          WTCLogger.logErrorXAEnd(var41);
                                          this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                          if (var20 == -1) {
                                             if (var62 != null) {
                                                synchronized(var62) {
                                                   if (!var62.getCalled()) {
                                                      var4.send_failure_return(var5, new TPException(10, "Exception " + var66 + " in service: " + var3.getServiceName()), var20);
                                                      var62.setCalled(true);
                                                   }
                                                }
                                             }
                                          } else {
                                             var21.remove_rplyObj(this.myConvDesc);
                                          }

                                          this.myParam.removeUser();
                                          if (var2) {
                                             ntrace.doTrace("]/TPRequestAsyncReplyImpl/internalReply/80/" + var41);
                                          }

                                          return;
                                       }
                                    }

                                    if (var2) {
                                       ntrace.doTrace("/InboundEJBRequestKEQ/run/success POJO " + var9);
                                    }

                                    return;
                                 }

                                 var8 = new String("tuxedo.services." + var7.getResourceName() + "Home");
                              }

                              Reply var13;
                              try {
                                 if (var2) {
                                    ntrace.doTrace("/InboundEJBRequestKEQ/execute/looking up " + var8);
                                 }

                                 Object var24 = this.myServices.getNameService().lookup(var8);
                                 TuxedoServiceHome var11 = (TuxedoServiceHome)PortableRemoteObject.narrow(var24, TuxedoServiceHome.class);
                                 TuxedoService var12 = (TuxedoService)PortableRemoteObject.narrow(var11.create(), TuxedoService.class);
                                 if (var2) {
                                    ntrace.doTrace("/InboundEJBRequestKEQ/execute/invoking EJB " + var8);
                                 }

                                 var13 = var12.service(var3);
                                 if (var2) {
                                    ntrace.doTrace("/InboundEJBRequestKEQ/execute/success EJB " + var8);
                                 }

                                 if (var13 != null) {
                                    var14 = var13.getReplyBuffer();
                                    var15 = var13.gettpurcode();
                                 }
                              } catch (NamingException var54) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6));
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/250");
                                 }

                                 return;
                              } catch (CreateException var55) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6, "Could not create " + var8 + ": " + var55));
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/280");
                                 }

                                 return;
                              } catch (AccessException var56) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6, var8 + " is not accessible: " + var56));
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/310");
                                 }

                                 return;
                              } catch (RemoteException var57) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(6, var8 + " is not available: " + var57));
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/340");
                                 }

                                 return;
                              } catch (TPReplyException var58) {
                                 if (this.wlsXaResource != null) {
                                    try {
                                       this.wlsXaResource.end(this.myXid, 536870912);
                                       this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                       this.wlsXaResource.rollback(this.myXid);
                                    } catch (XAException var53) {
                                       if (var2) {
                                          ntrace.doTrace("/InboundEJBRequestKEQ/error ending transaction/" + var53);
                                       }
                                    }

                                    this.wlsXaResource = null;
                                 }

                                 if (var2) {
                                    ntrace.doTrace("/InboundEJBRequestKEQ/execute/tpReplyerro " + var58);
                                 }

                                 var16 = var58.gettperrno();
                                 var13 = var58.getExceptionReply();
                                 if (var13 != null) {
                                    var14 = var13.getReplyBuffer();
                                    var15 = var13.gettpurcode();
                                 }
                              } catch (TPException var59) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, var59);
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/390/" + var59);
                                 }

                                 return;
                              } catch (Exception var60) {
                                 this.cleanUp(var20, var4, var5, (TimerTask)null, new TPException(12, "Exception in service: " + var60));
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/420/" + var60);
                                 }

                                 return;
                              }

                              if (this.wlsXaResource != null) {
                                 try {
                                    this.wlsXaResource.end(this.myXid, 67108864);
                                 } catch (XAException var50) {
                                    WTCLogger.logErrorXAEnd(var50);
                                    this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
                                    if (var20 == -1) {
                                       var4.send_failure_return(var5, new TPException(10, "Exception " + var50 + " in service: " + var6), var20);
                                    } else {
                                       var21.remove_rplyObj(this.myConvDesc);
                                    }

                                    this.myParam.removeUser();
                                    if (var2) {
                                       ntrace.doTrace("]/InboundEJBRequestKEQ/execute/430/" + var50);
                                    }

                                    return;
                                 }
                              }

                              if ((var19.get_flag() & 4) != 0) {
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/TPNOREPLY set");
                                 }

                              } else {
                                 tfmh var17;
                                 if (var14 == null) {
                                    var17 = new tfmh(1);
                                 } else {
                                    tcm var61 = new tcm((short)0, new UserTcb(var14));
                                    var17 = new tfmh(var14.getHintIndex(), var61, 1);
                                 }

                                 try {
                                    if (var2) {
                                       ntrace.doTrace("]/InboundEJBRequestKEQ/execute/sending success " + var5);
                                    }

                                    var4.send_success_return(var5, var17, var16, var15, var20);
                                 } catch (TPException var52) {
                                    if (var20 == -1) {
                                       var4.send_failure_return(var3.getReqid(), var52, var20);
                                    } else {
                                       var21.remove_rplyObj(this.myConvDesc);
                                    }

                                    this.myParam.removeUser();
                                    if (var2) {
                                       ntrace.doTrace("]/InboundEJBRequestKEQ/execute/460/" + var52);
                                    }

                                    return;
                                 }

                                 if (var20 != -1) {
                                    var21.remove_rplyObj(this.myConvDesc);
                                 }

                                 this.myParam.removeUser();
                                 if (var2) {
                                    ntrace.doTrace("]/InboundEJBRequestKEQ/execute/480");
                                 }

                              }
                           }
                        } else {
                           if (var20 == -1) {
                              var4.send_failure_return(var5, new TPException(4), var20);
                           } else {
                              var21.remove_rplyObj(this.myConvDesc);
                           }

                           if (var2) {
                              ntrace.doTrace("]/InboundEJBRequestKEQ/execute/70");
                           }

                        }
                     } else {
                        var4.send_failure_return(var5, new TPException(4), var20);
                        if (var2) {
                           ntrace.doTrace("]/InboundEJBRequestKEQ/execute/60");
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private void cleanUp(int var1, dsession var2, Serializable var3, TimerTask var4, TPException var5) {
      boolean var6 = ntrace.isTraceEnabled(2);
      if (var4 != null) {
         var4.cancel();
      }

      if (this.wlsXaResource != null) {
         try {
            this.wlsXaResource.end(this.myXid, 536870912);
            this.myServices.removeInboundRdomFromXid(this.myRemoteDomain, this.myXid);
            this.wlsXaResource.rollback(this.myXid);
         } catch (XAException var11) {
            if (var6) {
               ntrace.doTrace("/InboundEJBRequestKEQ/cleanUp/error ending transaction/" + var11);
            }
         } finally {
            this.wlsXaResource = null;
         }
      }

      if (var1 == -1) {
         var2.send_failure_return(var3, var5, var1);
      } else {
         var2.get_rcv_place().remove_rplyObj(this.myConvDesc);
      }

      this.myParam.removeUser();
   }

   public void setTaskName(String var1) {
      this.myName = new String("InboundEJBRequestKEQ$" + var1);
   }

   public String getTaskName() {
      return this.myName == null ? "InboundEJBRequestKEQ$unknown" : this.myName;
   }

   private class InboundTimer extends TimerTask {
      dsession _rplyObj;
      Serializable _rd;
      int _convid = -1;
      TPRequestAsyncReplyImpl _aReply;

      InboundTimer(dsession var2, Serializable var3, int var4, TPRequestAsyncReplyImpl var5) {
         this._rplyObj = var2;
         this._rd = var3;
         this._convid = var4;
         this._aReply = var5;
      }

      public void run() {
         boolean var1 = ntrace.isTraceEnabled(2);
         if (var1) {
            ntrace.doTrace("/InboundEJBRequestKEQ$InboundTimer/send timeout");
         }

         if (this._aReply != null && !this._aReply.isDone()) {
            synchronized(this._aReply) {
               if (!this._aReply.getCalled()) {
                  InboundEJBRequestKEQ.this.cleanUp(this._convid, this._rplyObj, this._rd, (TimerTask)null, new TPException(13));
                  this._aReply.setCalled(true);
               }
            }
         } else {
            InboundEJBRequestKEQ.this.cleanUp(this._convid, this._rplyObj, this._rd, (TimerTask)null, new TPException(13));
         }

      }
   }
}
