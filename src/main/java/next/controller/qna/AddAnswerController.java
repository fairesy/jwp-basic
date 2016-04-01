package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

public class AddAnswerController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

	private AnswerDao answerDao = new AnswerDao();
	private QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
		Answer answer = new Answer(req.getParameter("writer"), 
				req.getParameter("contents"), 
				Long.parseLong(req.getParameter("questionId")));
		log.debug("answer : {}", answer);
		
		Answer savedAnswer = answerDao.insert(answer);
		Question question = questionDao.findById(savedAnswer.getQuestionId());
		int currentAnswerCount = question.getCountOfComment();
		question.setCountOfComment(currentAnswerCount+1);
		questionDao.update(question); //질문에 달린 답변 개수 업데이트 
		return jsonView().addObject("answer", savedAnswer);
	}
}
