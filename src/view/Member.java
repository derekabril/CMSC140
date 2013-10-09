package view;

public class Member {
	
	private String memberName = "";
	private int score = 0;
	
	public Member( String memberName, int score ) {
		this.memberName = memberName;
		this.score = score;
	}
	
	public void setScore( int score ) {
		this.score = score;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public int getScore() {
		return score;
	}

}
