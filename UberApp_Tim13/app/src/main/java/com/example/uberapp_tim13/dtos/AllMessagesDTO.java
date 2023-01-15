package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.util.List;



public class AllMessagesDTO implements Serializable {
	
	private int totalCount;
	private List<MessageReturnedDTO> results;
	
	public AllMessagesDTO() {}

	public AllMessagesDTO(int totalCount, List<MessageReturnedDTO> results) {
		this.totalCount = totalCount;
		this.results = results;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<MessageReturnedDTO> getResults() {
		return results;
	}

	public void setResults(List<MessageReturnedDTO> results) {
		this.results = results;
	}

}
