package com.anonymbook.server;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Resource
public class RootService {

	public static Logger logger = LoggerFactory.getLogger(RootService.class);

	protected final HttpServletRequest request;

	private final Result result;

	public RootService(Result result, HttpServletRequest request) {
		this.result = result;
		this.request = request;
	}

	@Path("/")
	public void root(String request_ids, String ref, String count) {

		if (request_ids != null && ref == null) {
			handleRequest(request_ids);
		}

		handleAppNotify();

		result.use(Results.page()).forwardTo("Anonymbook.html");
	}

	private void handleRequest(String request_ids) {

		String requests[] = request_ids.split(",");

		try {
			for (int i = 0; i < requests.length; i++) {
				JsonElement request = Facebook.getAppRequestInfo(requests[i]);

				if (request == null) {
					continue;
				}

				// Grava a mizade no banco.
				// JsonObject from =  request.getAsJsonObject().get("from").getAsJsonObject();
				// JsonObject to = request.getAsJsonObject().get("to").getAsJsonObject();
				// persistence.addFriend(from.get("id").getAsString(), to.get("id").getAsString());

				Facebook.removeAppRequest(requests[i]);
			}
		} catch (Exception exception) {
			logger.error(format(
					"Durante o tratamento da app request '%s'. Ocorreu o erro '%s'. ",
					request_ids, exception));
		}

	}

	private void handleAppNotify() {

		String uid = getUserId();

		if (uid == null) {
			return;
		}
		try {
			JsonElement requestsInfo = Facebook.getAppRequests(uid);

			if (requestsInfo == null
					|| requestsInfo.getAsJsonObject().get("data")
							.getAsJsonArray().size() == 0) {
				return;
			}

			JsonArray requests = requestsInfo.getAsJsonObject().get("data")
					.getAsJsonArray();

			for (int i = 0; i < requests.size(); i++) {
				JsonObject request = requests.get(i).getAsJsonObject();

				// verifica se é uma requisição da aplicação.
				if (request.get("from") == null) {
					Facebook.removeAppRequest(request.get("id").getAsString());
				}
			}
		} catch (Exception exception) {
			logger.error(format(
					"Durante o tratamento das notificações. Ocorreu o erro '%s'. ",
					exception));
		}

	}

	/**
	 * Método para acesso ao identificador do usuário.
	 * 
	 * @return o identificador do usuário.
	 */
	private String getUserId() {

		Map<String, String> sessionData = new HashMap<String, String>();

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("fbs_" + Service.appId)) {
					sessionData = extractDataFromCookie(cookie.getValue());
				}
			}
		}

		return sessionData.get("uid");
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
