package personal.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

//
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private static final String USERNAME_KEY="email";
    private static final String PASSWORD_KEY="password";
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";  // /login/oauth2/ + ????? 로 오는 요청을 처리할 것이다
    private static final String HTTP_METHOD = "POST";    //HTTP 메서드의 방식은 POST 이다.
    private static final String CONTENT_TYPE = "application/json";//json 타입의 데이터로만 로그인을 진행한다.

    //AntPathRequestMatcher : SpringSecurity에서 URL 패턴을 기반으로 요청을 매칭. Ant 스타일의 패턴을 사용
    //Ant 스타일 패턴
    //? : 하나의 문자와 일치
    //* : 임의의 문자열과 일치
    //** : 임의의 경로와 일치
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); //=>   /login 의 요청에, POST로 온 요청에 매칭


    //AbstractAuthenticationProcessingFilter는 기본 생성자가 없기 때문에 상속 받을 경우 반드시 부모 생성자를 호출하는 생성자를 정의해야 함
    //    protected AbstractAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
    //        this.setFilterProcessesUrl(defaultFilterProcessesUrl);
    //    }
    //위의 생성자를 호출함
    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/login/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //getContentType(): HTTP 요청의 Content-Type 헤더 값을 나타내는 문자열
        // -> application/json, text/html, multipart/form-data
        //Content-Type이 application/json 일 경우만
        String contentType = request.getContentType();
        if(contentType == null || !contentType.equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + contentType);
        }

        //StreamUtils : Spring Framework의 유틸리티 클래스
        //copyToString(InputStream in, Charset charset) : 입력 스트림에서 데이터를 문자열로 변환. 주로 HTTP 요청의 body를 문자열로 반환 처리
        // + urf-8로 인코딩
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

                                                                        //변환할 JSON 문자열, 변환할 대상 객체의 클래스 타입
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        //pricipal과 credential 전달
        /**
         * principal : 인증된 사용자 또는 시스템 엔티티 ... username
         *  - 사용자가 인증을 성공적으로 마칠 경우 해당 사용자의 정보를 담고 있는 객체인 principal이 생성됨
         *  - java.security.Principal 인터페이스의 구현 객체이자 org.springframework.security.core.Authentication 객체의 일부
         *  - 접근법(1)
         *      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         *      String username = authentication.getName();
         *  - 접근법(2)
         *      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         *      String username = userDetails.getUsername();
         * credential : 사용자 인증 정보 ... password
         *  - 보안상의 이유로 인증이 완료된 후에는 credential 정보를 더 이상 저장하지 않는 것이 일반적 -> 사용자 권한 정보를 포함
         *  - 접근법
         *      UsernamePasswordAuthenticationToken authenticationToken =
         *          new UsernamePasswordAuthenticationToken(username, password);
         */
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
