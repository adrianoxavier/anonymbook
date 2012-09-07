package com.anonymbook.server;

import static java.lang.String.format;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

import com.anonymbook.server.domain.Answer;
import com.anonymbook.server.domain.Person;
import com.anonymbook.server.domain.PersonMessage;
import com.anonymbook.server.domain.Question;
import com.anonymbook.server.domain.QuestionsMessage;
import com.anonymbook.server.domain.StatusMessage;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Resource
@Path("/service")
public class Service {

	private static Logger logger = LoggerFactory.getLogger(Service.class);

	public static final String appId = "100923886651872";

	private final Result result;

	private final HttpServletRequest request;

	private Persistence persistence;

	private Map<String, String> sessionData;

	public Service(Result result, HttpServletRequest request) {
		this.result = result;
		this.request = request;
		this.persistence = new Persistence();
		this.sessionData = new HashMap<String, String>();
	}

	@Path("/person")
	public void getPersonInfo() {

		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Person person = persistence.getPerson(userId);
			PersonMessage message = new PersonMessage();

			if (person == null) {
				person = new Person(userId);
				persistence.save(person);
				message.setFirstAccess(true);
			}

			logger.info(format("Acesso aos dados do usuario '%s'.", userId));

			result.use(Results.json()).from(message).serialize();

		} catch (Exception exception) {
			sendError(format("while get person info: %s.", exception));
		}
	}

	@Path("/friends")
	public void getFriends() {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Set<String> friends = persistence.getFriends(userId);

			logger.info(format("Usuário %s acessando a lista com %d amigos.",
					userId, friends.size()));

			result.use(Results.json()).from(friends).serialize();

		} catch (Exception exception) {
			sendError(format("while get friend list: %s.", exception));
		}
	}

	@Path("/friends/get/{uid}")
	public void getFriends(String uid) {

		try {
			Set<String> friends = persistence.getFriends(uid);

			logger.info(format("Usuário %s acessando a lista com %d amigos.",
					uid, friends.size()));

			result.use(Results.json()).from(friends).serialize();

		} catch (Exception exception) {
			sendError(format("while get friend list: %s.", exception));
		}
	}

	@Path("/friends/anonym")
	public void getAnonymFriends() {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		List<String> anonym = new LinkedList<String>();

		try {

			Set<String> friends = persistence.getFriends(userId);
			List<String> friendsOnAnonym = Facebook.getFriendsOnAnonym(userId,
					sessionData.get("access_token"));

			for (String uid : friendsOnAnonym) {
				if (!friends.contains(uid)) {
					anonym.add(uid);
				}
			}

			logger.info(format("Usuário %s acessando a lista com %d amigos que"
					+ " estão no anonymbook e que não o adicionaram.", userId,
					anonym.size()));

			result.use(Results.json()).from(anonym).serialize();

		} catch (Exception exception) {
			sendError(format("while get friends on anonymbook: %s.", exception));
		}
	}

	@Path("/friend/remove/{friend}")
	public void removeFriend(String friend) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			persistence.removeFriend(userId, friend);

			logger.info(format("Desfeita a amizade entre %s e %s.", userId,
					friend));

			sendSuccess("friend removed");

		} catch (Exception exception) {
			sendError(format("while remove a friend: %s.", exception));
		}
	}

	@Path("/question/{withTimeAgo}/{key}")
	public void getQuestion(String key, boolean withTimeAgo) {

		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Key k = KeyFactory.stringToKey(key);
			Question question = persistence.getQuestion(k, withTimeAgo, userId);

			logger.info(format("Acesso a pergunta '%s'.", key));

			result.use(Results.json()).from(question).exclude("author")
					.serialize();

		} catch (Exception exception) {
			sendError(format("while get question.", exception));
		}
	}

	@Path("/question/{withTimeAgo}/{timestamp}/{limit}")
	public void getQuestion(long timestamp, int limit, boolean withTimeAgo) {

		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			QuestionsMessage message = persistence.getQuestions(userId, limit,
					withTimeAgo, timestamp, userId);

			logger.info(format(
					"Acesso a '%d' perguntas feitas ou recebidas por "
							+ "'%s'.", message.getQuestions().size(), userId));

			result.use(Results.json()).from(message).exclude("author")
					.serialize();

		} catch (Exception exception) {
			sendError(format("while get questions list: %s.", exception));
		}
	}

	public boolean checkFriendship(String userId, String user) {

		if (userId.equals(user)) {
			return true;
		}

		if (!persistence.getFriends(userId).contains(user)) {
			sendError("Invalid request. Not friends!");
			return false;
		}

		return true;
	}

	@Path("/question/made/{withTimeAgo}/{timestamp}/{limit}")
	public void getMadeQuestion(long timestamp, int limit, boolean withTimeAgo) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			QuestionsMessage message = persistence.getQuestionsByAuthor(userId,
					limit, withTimeAgo, timestamp, userId);

			logger.info(format("Acesso a '%d' perguntas feitas por '%s'.",
					message.getQuestions().size(), userId));

			result.use(Results.json()).from(message).exclude("author")
					.serialize();

		} catch (Exception exception) {
			sendError(format("while get made questions: %s.", exception));
		}
	}

	@Path("/question/made/{recipient}/{withTimeAgo}/{timestamp}/{limit}")
	public void getMadeQuestion(String recipient, long timestamp, int limit,
			boolean withTimeAgo) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			QuestionsMessage message = persistence.getQuestions(userId,
					recipient, limit, withTimeAgo, timestamp, userId);

			logger.info(format(
					"Acesso a '%d' perguntas feitas por '%s' para '%s'.",
					message.getQuestions().size(), userId, recipient));

			result.use(Results.json()).from(message).exclude("author")
					.serialize();

		} catch (Exception exception) {
			sendError(format("while get made questions: %s.", exception));
		}
	}

	@Path("/question/received/{user}/{withTimeAgo}/{timestamp}/{limit}")
	public void getReceivedQuestion(String user, long timestamp, int limit,
			boolean withTimeAgo) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			QuestionsMessage message = persistence.getQuestionsByRecipient(
					user, limit, withTimeAgo, timestamp, userId);

			logger.info(format("Acesso a '%d' perguntas recebidas por '%s'.",
					message.getQuestions().size(), user));

			result.use(Results.json()).from(message).exclude("author")
					.serialize();

		} catch (Exception exception) {
			sendError(format("while get received questions: %s.", exception));
		}
	}

	@Post("/question/make")
	public void makeQuestion(String question, String recipient) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Question q = new Question(userId, question, recipient);
			if (recipient == null) {
				q.setHasAnswer(true);
			}

			persistence.save(q);

			logger.info(format("Nova pergunta, de '%s' para '%s'.", userId,
					recipient));

			// Notifica o usuario que tem uma nova pergunta.
			if (recipient != null) {
				Facebook.sendAppNotify(recipient,
						"New anonym question for you.");

				sendSuccess("Question made.");
			} else {
				result.use(Results.json()).from(q).exclude("author")
						.serialize();
			}

		} catch (Exception exception) {
			sendError(format("while make a question: %s.", exception));
		}
	}

	@Post("/question/answer")
	public void answerQuestion(String key, String answer) {
		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Key k = KeyFactory.stringToKey(key);
			Question q = persistence.getQuestion(k, false, userId);

			if (q == null) {
				sendError("Invalid request,");
				return;
			}

			Answer newAnswer = null;

			if (q.getAnswer() == null && q.hasAnswer()) {
				newAnswer = q.newAnswer(answer, userId);
			} else {
				// apenas o destinatario pode responder a pergunta anonima.
				if (!userId.equals(q.getRecipient())) {
					sendError("Invalid request: this question is not for you.");
					return;
				}

				q.setAnswer(answer);
				q.setAnswerTimestamp(new Date().getTime());
			}

			persistence.update(q);

			// Notifica o autor da pergunta que houve uma resposta.
			if (newAnswer == null || !q.getAuthor().equals(userId)) {
				Facebook.sendAppNotify(q.getAuthor(),
						"Your question has been answered");
			}

			logger.info(format("Respondendo a pergunta '%s'.", key));

			if (newAnswer == null) {
				sendSuccess("Question answered.");
			} else {
				newAnswer.setAuthor(true);
				result.use(Results.json()).from(newAnswer).exclude("author")
						.serialize();
			}

		} catch (Exception exception) {
			sendError(format("while answer question: %s.", exception));
		}
	}

	@Path("/question/remove/{key}")
	public void removeQuestion(String key) {

		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Key k = KeyFactory.stringToKey(key);
			Question q = persistence.getQuestion(k, false, userId);

			// apenas o autor ou o destinatario podem remover a pergunta.
			if (q == null
					|| (!userId.equals(q.getRecipient()) && !userId.equals(q
							.getAuthor()))) {
				sendError("Invalid request: this question is not for you.");
				return;
			}

			logger.info(format("Removendo a pergunta '%s'.", key));
			persistence.removeQuestion(k);

			sendSuccess("removed");

		} catch (Exception exception) {
			sendError(format("while remove question: %s.", exception));
		}
	}

	@Path("/answer/remove/{key}")
	public void removeAnswer(String key) {

		String userId = getUserId();

		if (userId == null) {
			return;
		}

		try {
			Key k = KeyFactory.stringToKey(key);
			persistence.removeAnswer(k, userId);

			sendSuccess("removed");

		} catch (Exception exception) {
			sendError(format("while remove question: %s.", exception));
		}
	}

	private void sendError(String message) {
		result.use(Results.json()).from(new StatusMessage(message, false))
				.serialize();
	}

	private void sendSuccess(String message) {
		result.use(Results.json()).from(new StatusMessage(message, true))
				.serialize();
	}

	@Path("/active")
	public void makeActive() {
		result.use(Results.nothing());
	}

	@Path("/session")
	public void getSessionInfo() {
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies == null) {
				sendError("cookies not found.");
				return;
			}

			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("fbs_" + appId)) {
					sessionData = extractDataFromCookie(cookie.getValue());
				}
			}

			logger.info(format("Acesso aos dados da sessão."));
			result.use(Results.json()).from(sessionData).serialize();

		} catch (Exception exception) {
			sendError(format("while get session info: %s.", exception));
		}
	}

	/**
	 * Método para acesso ao identificador do usuário.
	 * 
	 * @return o identificador do usuário.
	 */
	private String getUserId() {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("fbs_" + appId)) {
					sessionData = extractDataFromCookie(cookie.getValue());
				}
			}
		}
		String uid = sessionData.get("uid");
		if (uid == null) {
			sendError("not logged user.");
		}
		return uid;
	}

	/**
	 * Método para extrair os dados de um cookie.
	 * 
	 * @param cookieValue
	 *            o cookie da sessão
	 * @return um mapa com os dados do cookie.
	 */
	private Map<String, String> extractDataFromCookie(String cookieValue) {
		HashMap<String, String> data = new HashMap<String, String>();
		String[] fields = cookieValue.split("\\&");
		for (String field : fields) {
			String[] keyValue = field.split("\\=");
			data.put(keyValue[0], keyValue[1]);
		}
		return data;
	}

}
