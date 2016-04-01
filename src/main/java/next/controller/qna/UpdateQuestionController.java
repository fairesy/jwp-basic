package next.controller.qna;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class UpdateQuestionController extends AbstractController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateQuestionController.class);
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest req, 
			HttpServletResponse response) throws Exception {
		long questionId = Long.parseLong(req.getParameter("questionId"));
		Question originalQuestion = questionDao.findById(questionId);
		int countOfAnswer = originalQuestion.getCountOfComment();
		String writer = req.getParameter("writer");
		String title = req.getParameter("title");
		String contents = req.getParameter("contents");
		Date createdDate = originalQuestion.getCreatedDate();
		Question updateQuestion = new Question(questionId, writer, title, contents, createdDate, countOfAnswer);
		
		questionDao.update(updateQuestion);
		return jspView("redirect:/qna/show?questionId="+questionId);
	}
}