package group.filter;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    // 导入的必须是servlet的包

    // 必须重写三个方法

    // 初始化: Web服务器启动时就会初始化
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("CharacterEncodingFilter已经初始化");
    }

    // Chain: 链,放行的作用
    // 1.过滤所有代码,在过滤特定请求的时候都会执行
    // 2.必须要让过滤器继续通行
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 过滤器: 统一处理乱码问题
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/javascript;charset=utf-8");
        // 放行!!!
        filterChain.doFilter(servletRequest, servletResponse);  // 让我们的请求继续走,如果不写就被拦截停止
    }

    // 销毁: Web服务器关闭的时候过滤器会销毁
    @Override
    public void destroy() {
        System.out.println("CharacterEncodingFilter销毁");
    }
}
