package next.model;

import java.sql.Timestamp;

public class Answer {
	private int answerId;
	private String writer;
	private String contents;
	private Timestamp createdDate;
	private int questionId;
	
	public Answer(int answerId, String writer, String contents,
			Timestamp createdDate, int questionId) {
		super();
		this.answerId = answerId;
		this.writer = writer;
		this.contents = contents;
		this.createdDate = createdDate;
		this.questionId = questionId;
	}
	public int getAnswerId() {
		return answerId;
	}
	public String getWriter() {
		return writer;
	}
	public String getContents() {
		return contents;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public int getQuestionId() {
		return questionId;
	}
}
