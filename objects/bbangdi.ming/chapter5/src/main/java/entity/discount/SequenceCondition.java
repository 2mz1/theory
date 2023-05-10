package entity.discount;

import entity.Screening;

public class SequenceCondition implements DiscountCondition {
	private int sequence;

	public SequenceCondition(int sequence) {
		this.sequence = sequence;
	}

	public boolean isSatisfiedBy(Screening screening) {
		return screening.getSequence() == sequence;
	}
}
