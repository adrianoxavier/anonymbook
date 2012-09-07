package com.anonymbook.server;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.anonymbook.server.domain.Answer;
import com.anonymbook.server.domain.Friend;
import com.anonymbook.server.domain.Person;
import com.anonymbook.server.domain.Question;
import com.anonymbook.server.domain.QuestionsMessage;
import com.anonymbook.server.util.AnswerComparator;
import com.anonymbook.server.util.QuestionComparator;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class Persistence {

	private static EntityManagerFactory factory = javax.persistence.Persistence
			.createEntityManagerFactory("anonym-persistence");

	private DatastoreService datastore;

	public Persistence() {
		this.datastore = DatastoreServiceFactory.getDatastoreService();
	}

	public void save(Serializable s) {

		EntityManager em = factory.createEntityManager();

		try {
			em.persist(s);
		} finally {
			em.close();
		}
	}

	public void update(Serializable s) {

		EntityManager em = factory.createEntityManager();

		try {
			em.merge(s);
		} finally {
			em.close();
		}
	}

	private int getCount(Query query) {
		PreparedQuery pq = datastore.prepare(query);

		return pq.countEntities(FetchOptions.Builder.withDefaults());
	}

	public Person getPerson(String uid) {

		Query query = new Query("Person");
		query.addFilter("id", Query.FilterOperator.EQUAL, uid);

		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();

		if (entity == null || entity.getKey() == null) {
			return null;
		}

		return new Person(entity);
	}

	public void removePerson(String uid) {

		Query query = new Query("Person");
		query.addFilter("id", Query.FilterOperator.EQUAL, uid);

		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();

		if (entity != null && entity.getKey() != null) {
			datastore.delete(entity.getKey());
		}
	}

	public List<Person> getPersons(int offset, int limit) {

		Query query = new Query("Person");

		PreparedQuery pq = datastore.prepare(query);

		List<Person> personList = new LinkedList<Person>();
		List<Entity> entityList = pq.asList(FetchOptions.Builder.withOffset(
				offset).limit(limit));

		for (Entity entity : entityList) {
			personList.add(new Person(entity));
		}

		return personList;
	}

	public Question getQuestion(Key key, boolean withTimeAgo, String currentUser) {

		Query query = new Query("Question", key);

		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();

		if (entity == null || entity.getKey() == null) {
			return null;
		}
		Question question = handleQuestion(entity, withTimeAgo, currentUser);

		return question;
	}

	public void removeQuestion(Key key) {

		Query query = new Query("Question", key);

		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();

		if (entity != null && entity.getKey() != null) {
			datastore.delete(entity.getKey());
		}
	}

	private List<Question> getQuestionsList(boolean withTimeAgo, Query query,
			int limit, long timestamp, QuestionsMessage message,
			String currentUser) {

		PreparedQuery pq = datastore.prepare(query);

		List<Entity> entityList;

		if (limit >= 0 || timestamp > 0) {
			entityList = pq.asList(FetchOptions.Builder.withDefaults().limit(
					limit));
		} else {
			entityList = pq.asList(FetchOptions.Builder.withDefaults());
		}

		if (message != null) {
			setExtraParameter(message, entityList);
		}

		List<Question> questions = new LinkedList<Question>();

		for (Entity entity : entityList) {
			Question question = handleQuestion(entity, withTimeAgo, currentUser);
			questions.add(question);
		}

		return questions;
	}

	private void handleTimeAgo(QuestionsMessage message,
			List<Question> questions, boolean withTimeAgo) {

		if (questions.size() == 0) {
			return;
		}

		Question first = questions.get(0);
		message.setFirstTimestamp(first.getQuestionTimestamp());

		Question last = questions.get(questions.size() - 1);
		message.setLastTimestamp(last.getQuestionTimestamp());

		if (withTimeAgo) {
			Long now = System.currentTimeMillis();
			for (Question question : questions) {
				handleQuestionTimeAgo(question, now, withTimeAgo);

				List<Answer> answers = question.getAnswers();
				if (answers != null) {
					for (Answer answer : answers) {
						handleAnswerTimeAgo(answer, now, withTimeAgo);
					}
					sortAnswers(true, answers);
				}
			}
			sortQuestions(true, questions);

		}
	}

	private void checkQuestionTimestampUsage(long timestamp, Query query) {
		if (timestamp > 0) {
			query.addFilter("questionTimestamp",
					Query.FilterOperator.LESS_THAN, timestamp);
		}
	}

	public QuestionsMessage getQuestions(String author, String recipient,
			int limit, boolean withTimeAgo, long timestamp, String currentUser) {

		Query query = new Query("Question");
		query.addFilter("author", Query.FilterOperator.EQUAL, author);
		query.addFilter("recipient", Query.FilterOperator.EQUAL, recipient);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);
		checkQuestionTimestampUsage(timestamp, query);

		QuestionsMessage message = new QuestionsMessage();
		message.setQuestions(getQuestionsList(withTimeAgo, query, limit,
				timestamp, message, currentUser));
		message.setAmount(getIdentifiedQuestionsCount(author, recipient));

		return message;
	}

	public QuestionsMessage getQuestions(String user, int limit,
			boolean withTimeAgo, long timestamp, String currentUser) {

		QuestionsMessage byAuthor = getQuestionsByAuthor(user, limit, false,
				timestamp, currentUser);

		QuestionsMessage byRecipient = getQuestionsByRecipient(user, limit,
				false, timestamp, currentUser);

		List<Question> result = new LinkedList<Question>();
		result.addAll(byAuthor.getQuestions());
		result.addAll(byRecipient.getQuestions());

		List<Question> intercalated = new LinkedList<Question>(result);

		int size = intercalated.size();
		sortQuestions(false, intercalated);

		List<Question> questions = intercalated.subList(0,
				((limit <= size) ? limit : size));

		QuestionsMessage message = new QuestionsMessage();

		handleTimeAgo(message, questions, withTimeAgo);

		message.setQuestions(questions);
		message.setAmount(byAuthor.getAmount() + byRecipient.getAmount());

		return message;
	}

	public QuestionsMessage getQuestionsByAuthor(String author, int limit,
			boolean withTimeAgo, long timestamp, String currentUser) {

		Query query = new Query("Question");
		query.addFilter("author", Query.FilterOperator.EQUAL, author);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);
		checkQuestionTimestampUsage(timestamp, query);

		QuestionsMessage message = new QuestionsMessage();
		message.setQuestions(getQuestionsList(withTimeAgo, query, limit,
				timestamp, message, currentUser));
		message.setAmount(getQuestionsByAuthorCount(author));

		return message;
	}

	public QuestionsMessage getQuestionsByRecipient(String recipient,
			int limit, boolean withTimeAgo, long timestamp, String currentUser) {

		if (recipient.equals(currentUser)) {
			return getQuestionsByRecipient(recipient, limit, withTimeAgo,
					false, timestamp, currentUser);
		} else {
			return getPublicQuestionsByRecipient(recipient, limit, withTimeAgo,
					timestamp, currentUser);
		}
	}

	private QuestionsMessage getQuestionsByRecipient(String recipient,
			int limit, boolean withTimeAgo, boolean onlyWithAnswer,
			long timestamp, String currentUser) {

		Query query = new Query("Question");
		query.addFilter("recipient", Query.FilterOperator.EQUAL, recipient);
		checkQuestionTimestampUsage(timestamp, query);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);

		if (onlyWithAnswer) {
			query.addFilter("hasAnswer", Query.FilterOperator.EQUAL, true);
		}

		QuestionsMessage message = new QuestionsMessage();
		message.setQuestions(getQuestionsList(withTimeAgo, query, limit,
				timestamp, message, currentUser));
		message.setAmount(getQuestionsByRecipientCount(recipient,
				onlyWithAnswer));

		return message;
	}

	private QuestionsMessage getPublicQuestionsByRecipient(String recipient,
			int limit, boolean withTimeAgo, long timestamp, String currentUser) {

		QuestionsMessage anonymQuestions = getQuestionsByRecipient(recipient,
				limit, false, true, timestamp, currentUser);

		QuestionsMessage publicQuestions = getQuestions(recipient, null, limit,
				false, timestamp, currentUser);

		List<Question> result = new LinkedList<Question>();
		result.addAll(anonymQuestions.getQuestions());
		result.addAll(publicQuestions.getQuestions());

		List<Question> intercalated = new LinkedList<Question>(result);

		int size = intercalated.size();
		sortQuestions(false, intercalated);

		List<Question> questions = intercalated.subList(0,
				((limit <= size) ? limit : size));

		QuestionsMessage message = new QuestionsMessage();

		handleTimeAgo(message, questions, withTimeAgo);

		message.setQuestions(questions);
		message.setAmount(anonymQuestions.getAmount()
				+ publicQuestions.getAmount());

		return message;
	}

	public int getIdentifiedQuestionsCount(String author, String recipient) {

		Query query = new Query("Question");
		query.addFilter("author", Query.FilterOperator.EQUAL, author);
		query.addFilter("recipient", Query.FilterOperator.EQUAL, recipient);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);

		return getCount(query);
	}

	public int getQuestionsByAuthorCount(String author) {

		Query query = new Query("Question");
		query.addFilter("author", Query.FilterOperator.EQUAL, author);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);

		return getCount(query);
	}

	public int getQuestionsByRecipientCount(String recipient,
			boolean onlyWithAnswer) {

		Query query = new Query("Question");
		query.addFilter("recipient", Query.FilterOperator.EQUAL, recipient);
		query.addSort("questionTimestamp", SortDirection.DESCENDING);

		if (onlyWithAnswer) {
			query.addFilter("hasAnswer", Query.FilterOperator.EQUAL, true);
		}

		return getCount(query);
	}

	private void setExtraParameter(QuestionsMessage message, List<Entity> list) {
		if (list.size() <= 0) {
			return;
		}

		Question first = new Question(list.get(0));
		message.setFirstTimestamp(first.getQuestionTimestamp());

		Question last = new Question(list.get(list.size() - 1));
		message.setLastTimestamp(last.getQuestionTimestamp());
	}

	public Set<String> getFriends(String userId) {

		Query query = new Query("Friend");
		query.addFilter("user1", Query.FilterOperator.EQUAL, userId);

		PreparedQuery pq = datastore.prepare(query);

		Set<String> friendList = new HashSet<String>();
		List<Entity> entityList = pq
				.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : entityList) {
			friendList.add((String) entity.getProperty("user2"));
		}

		return friendList;
	}

	public void addFriend(String user1, String user2) {
		save(new Friend(user1, user2));
		save(new Friend(user2, user1));
	}

	public void removeFriend(String user1, String user2) {
		removeFriendship(user1, user2);
		removeFriendship(user2, user1);
	}

	private void removeFriendship(String user1, String user2) {

		Query query = new Query("Friend");
		query.addFilter("user1", Query.FilterOperator.EQUAL, user1);
		query.addFilter("user2", Query.FilterOperator.EQUAL, user2);

		PreparedQuery pq = datastore.prepare(query);
		List<Entity> entityList = pq
				.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : entityList) {
			if (entity != null && entity.getKey() != null) {
				datastore.delete(entity.getKey());
			}
		}
	}

	private Question handleQuestion(Entity entity, boolean withTimeAgo,
			String currentUser) {
		Long now = new Date().getTime();

		Question question = new Question(entity);
		handleQuestionTimeAgo(question, now, withTimeAgo);

		if (question.getRecipient() == null) {
			List<Entity> entities = getAnswers(question.getKey());

			if (entities != null) {

				LinkedList<Answer> list = new LinkedList<Answer>();

				for (Entity answerEntity : entities) {
					Answer answer = new Answer(answerEntity);
					answer.setAuthor(answer.getAuthor().equals(currentUser));
					handleAnswerTimeAgo(answer, now, withTimeAgo);
					list.add(answer);
				}
				sortAnswers(withTimeAgo, list);
				question.setAnswers(list);
			}
			question.setRecipient(null);
		}

		return question;
	}

	private List<Entity> getAnswers(Key key) {
		Query query = new Query("Answer");
		query.addFilter("questionKey", FilterOperator.EQUAL, key);
		query.addSort("timestamp", SortDirection.DESCENDING);

		PreparedQuery pq = datastore.prepare(query);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withDefaults());

		if (entities == null) {
			return null;
		}

		return entities;
	}

	public void removeAnswer(Key key, String user) {
		Query query = new Query("Answer", key);
		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();

		if (entity == null || entity.getKey() == null) {
			return;
		}

		Answer answer = new Answer(entity);

		query = new Query("Question", answer.getQuestionKey());
		pq = datastore.prepare(query);
		entity = pq.asSingleEntity();

		Question question = new Question(entity);

		if (user.equals(question.getAuthor())
				|| user.equals(answer.getAuthor())) {
			datastore.delete(answer.getKey());
		}
	}

	private void sortQuestions(boolean withTimeAgo, List<Question> questions) {
		if (withTimeAgo) {
			// Com 'time ago' a ordem dos timestamp é crescente.
			Collections.sort(questions, new QuestionComparator(false));
		} else {
			Collections.sort(questions, new QuestionComparator(true));
		}
	}

	private void handleQuestionTimeAgo(Question q, Long now, boolean withTimeAgo) {

		if (withTimeAgo) {
			if (q.getQuestionTimestamp() != null) {
				q.setQuestionTimestamp(now - q.getQuestionTimestamp());
			}
			if (q.getAnswerTimestamp() != null) {
				q.setAnswerTimestamp(now - q.getAnswerTimestamp());
			}
		}
	}

	private void sortAnswers(boolean withTimeAgo, List<Answer> answers) {
		if (withTimeAgo) {
			// Com 'time ago' a ordem dos timestamp é crescente.
			Collections.sort(answers, new AnswerComparator(false));
		} else {
			Collections.sort(answers, new AnswerComparator(true));
		}
	}

	private void handleAnswerTimeAgo(Answer answer, Long now,
			boolean withTimeAgo) {

		if (withTimeAgo) {
			if (answer.getTimestamp() != null) {
				answer.setTimestamp(now - answer.getTimestamp());
			}
		}
	}

}
