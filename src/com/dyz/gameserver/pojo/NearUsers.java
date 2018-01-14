package com.dyz.gameserver.pojo;

import java.util.ArrayList;
import java.util.List;

public class NearUsers {
	private int status  ; 
	private int total   ; 
	private int size    ; 
	private List<NearUser> contents=new ArrayList<NearUser>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<NearUser> getContents() {
		return contents;
	}
	public void setContents(List<NearUser> contents) {
		this.contents = contents;
	} 
	
}
