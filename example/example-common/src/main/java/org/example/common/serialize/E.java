package org.example.common.serialize;

import java.io.Serializable;
import java.util.List;

public class E implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<D> d;

	public List<D> getD() {
		return d;
	}

	public void setD(List<D> d) {
		this.d = d;
	}
}
