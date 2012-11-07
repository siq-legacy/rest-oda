package oda.datatools.connectivity.rest.impl;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTParameterMetaData
  implements IParameterMetaData
{
  private int parametercount = 0;
  public RESTParameterMetaData() {
   }

  public int getParameterCount() throws OdaException
  {
    return this.parametercount;
  }

  public void setParametercount(int parametercount) {
	this.parametercount = parametercount;
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