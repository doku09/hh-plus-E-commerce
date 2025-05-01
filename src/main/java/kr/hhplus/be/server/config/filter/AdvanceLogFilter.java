package kr.hhplus.be.server.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
public class AdvanceLogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(res);

		String uuid = UUID.randomUUID().toString();
		String requestURI = req.getRequestURI();
		LocalDateTime requestTime = LocalDateTime.now();
		long startTime = System.currentTimeMillis();

		try {
			chain.doFilter(wrappedRequest, wrappedResponse);
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			LocalDateTime responseTime = LocalDateTime.now();

			logRequest(wrappedRequest, uuid, requestURI, requestTime);
			logResponse(wrappedResponse, uuid, requestURI, responseTime, duration);
			wrappedResponse.copyBodyToResponse();
		}
	}

	private void logRequest(ContentCachingRequestWrapper request, String uuid, String uri, LocalDateTime time) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n====================== REQUEST START ======================\n");
		sb.append("UUID        : ").append(uuid).append("\n");
		sb.append("Timestamp   : ").append(time).append("\n");
		sb.append("URI         : ").append(request.getMethod()).append(" ").append(uri).append("\n");
		sb.append("Headers     :\n");
		Collections.list(request.getHeaderNames()).forEach(name -> {
			sb.append("    ").append(name).append(": ").append(request.getHeader(name)).append("\n");
		});
		sb.append("Body        :\n");
		sb.append(getBodyAsString(request.getContentAsByteArray(), request.getCharacterEncoding())).append("\n");
		sb.append("======================= REQUEST END =======================\n");

		log.info(sb.toString());
	}

	private void logResponse(ContentCachingResponseWrapper response, String uuid, String uri, LocalDateTime time, long duration) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n====================== RESPONSE START =====================\n");
		sb.append("UUID        : ").append(uuid).append("\n");
		sb.append("Timestamp   : ").append(time).append("\n");
		sb.append("URI         : ").append(uri).append("\n");
		sb.append("Status      : ").append(response.getStatus()).append("\n");
		sb.append("Duration    : ").append(duration).append(" ms\n");
		sb.append("Headers     :\n");
		response.getHeaderNames().forEach(name -> {
			sb.append("    ").append(name).append(": ").append(response.getHeader(name)).append("\n");
		});
		sb.append("Body        :\n");
		sb.append(getBodyAsString(response.getContentAsByteArray(), response.getCharacterEncoding())).append("\n");
		sb.append("======================= RESPONSE END ======================\n");

		log.info(sb.toString());
	}

	private String getBodyAsString(byte[] content, String encoding) throws UnsupportedEncodingException {
		if (content.length == 0) return "[No Body]";
		return new String(content, encoding);
	}
}
