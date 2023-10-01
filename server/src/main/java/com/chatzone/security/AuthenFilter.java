/*
 * The MIT License
 *
 * Copyright 2023 duongban.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.chatzone.security;

import com.chatzone.model.ApiResponse;
import com.chatzone.model.ECode;
import com.chatzone.util.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author duongban
 */
@Component
public class AuthenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenFilter.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApiResponse apiResp;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException {
        try {
            String path = request.getRequestURI();
            if (!(path.endsWith("/login")
                    || path.endsWith("register"))) {
                String session = request.getHeader("Session");
                String username = cacheManager
                        .getCache("session")
                        .get(session, String.class);
                if (username == null) {
                    ApiResponse resp = apiResp.getApiResponse(ECode.INVALID_SESSION);
                    response.getWriter().write(jsonUtil.toJsonString(resp));
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            ApiResponse resp = apiResp.getApiResponse(ECode.EXCEPTION);
            response.getWriter().write(jsonUtil.toJsonString(resp));
        }
    }

}
