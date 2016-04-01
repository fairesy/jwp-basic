package next.controller.qna;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class ShowController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();
	private Question question;
	private List<Answer> answers;

//	인스턴스는 힙에. 메서드는 스택에.
//	쓰레드는 힙에 저장하는 것을 공유한다. 
//	상태를 공유하지 않는 자원은 
//	private은 인스턴스 변수에만 들어가는 접근 제어자 
//	메서드 안은 로컬 변수 
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse response) throws Exception {
		Long questionId = Long.parseLong(req.getParameter("questionId"));
		
		question = questionDao.findById(questionId);
		answers = answerDao.findAllByQuestionId(questionId);
		
		ModelAndView mav = jspView("/qna/show.jsp");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		return mav;
	}
}
