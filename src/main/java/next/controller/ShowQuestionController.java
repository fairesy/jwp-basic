package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import core.mvc.Controller;

public class ShowQuestionController implements Controller {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int questionId = Integer.parseInt(req.getParameter("questionId"));
		QuestionDao questionDao = new QuestionDao();
		Question question = questionDao.findByQuestionId(questionId);
		
		AnswerDao answerDao = new AnswerDao();
		
		req.setAttribute("question", question);
		req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));
        return "/qna/show.jsp";
	}
}
