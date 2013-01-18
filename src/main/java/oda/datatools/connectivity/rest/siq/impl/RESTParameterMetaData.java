package oda.datatools.connectivity.rest.siq.impl;

import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTParameterMetaData
  implements IParameterMetaData
{
  private int parametercount = 0;
  private   Map<Integer,Object> Param;
  public RESTParameterMetaData( Map<Integer,Object> Param) {
	  this.Param=Param;
	  this.parametercount=Param.size();
   }

  public int getParameterCount() throws OdaException
  {
    return this.parametercount;
  }

 

public int getParameterMode(int param)
    throws OdaException
  {
	
    return 1;
  }

  public String getParameterName(int param)
    throws OdaException
  {
	 
    int nativeTypeCode = getParameterType(param);
    return RESTDriver.getNativeDataTypeName(nativeTypeCode);
  }

  public int getParameterType(int param)
    throws OdaException
  {
	 
    return 11;
  }

  public String getParameterTypeName(int param) throws OdaException
  {
	
      return "JavaObject";
  }

  public int getPrecision(int param)
    throws OdaException
  {
    return -1;
  }

  public int getScale(int param)
    throws OdaException
  {
    return -1;
  }

  public int isNullable(int param)
    throws OdaException
  {
    return 0;
  }
}