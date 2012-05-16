package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.UUID;

import org.jage.address.IAgentAddress;

public class MoveAgentTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UUID senderUUID;
	private IAgentAddress sourceWorkplace;
	private IAgentAddress destinationWorkplace;
	
	public MoveAgentTask(UUID senderUUID) {
		this.senderUUID = senderUUID;
	}
	
	public IAgentAddress getSourceWorkplace() {
		return sourceWorkplace;
	}
	
	public void setSourceWorkplace(IAgentAddress sourceWorkplace) {
		this.sourceWorkplace = sourceWorkplace;
	}
	
	public IAgentAddress getDestinationWorkplace() {
		return destinationWorkplace;
	}
	
	public void setDestinationWorkplace(IAgentAddress destinationWorkplace) {
		this.destinationWorkplace = destinationWorkplace;
	}
	
	public UUID getSenderUUID() {
		return senderUUID;
	}
	
}
