package oda.datatools.connectivity.rest.siq.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
public class RESTList
{
  List<String> columnList;
  HashMap<String,List<String>> complicatedcolumns;
  
  public HashMap<String,List<String>> getComplicatedcolumns() {
	return complicatedcolumns;
}
public void setComplicatedcolumns(HashMap<String,List<String>> complicatedcolumns) {
	this.complicatedcolumns = complicatedcolumns;
}
public void setColumnlist(List<String> columnList) {
	this.columnList = columnList;
}

  private List<String> datatype;
  public void setDatatype(List<String> datatype) {
	this.datatype = datatype;
}

private List<Object> row;
  public List<Object> getRow() {
	return row;
}

private List<List<Object>> rowList;

  public List<String> getDatatype()
  {
    return this.datatype;
  }
  public List<String> getColumnlist() {
    return this.columnList;
  }

  public String toString() {
    return this.rowList.toString();
  }

  public RESTList()
  {
    
  }

 

  
  public void addObj(Object obj,int position) {
    this.row.set(position, obj);
  }

  public void createRow() {
	  this.row =  Arrays.asList(new Object[this.columnList.size()]);
	  
  }
  public void reset()
  {
	  
	  this.rowList = new LinkedList<List<Object>>();
  }

  public void addtoRowList()
  {
    this.rowList.add(this.row);
  }

  public List<List<Object>> getRows() {
    return this.rowList;
  }
}