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
package com.chatzone.controller;

import com.chatzone.db.entity.UserEntity;
import com.chatzone.model.ApiResponse;
import com.chatzone.model.Authen;
import com.chatzone.model.ECode;
import com.chatzone.service.inf.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author duongban
 */
@RestController
@RequestMapping(value = "/api")
public class AuthenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private ApiResponse apiResp;

    @PostMapping(value = "/register")
    public ApiResponse register(@RequestBody Authen auth) {
        ApiResponse resp = apiResp.getApiResponse(ECode.SUCCESS);
        try {
            LOGGER.info(String.format("Register username[%s] pass[%s]",
                    auth.getUsername(),
                    auth.getPassword()));
            Pair<ECode, UserEntity> ret = userService.create(auth);
            if (ECode.isFailed(ret.getFirst())) {
                return apiResp.getApiResponse(ret.getFirst());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resp = apiResp.getApiResponse(ECode.EXCEPTION);
        }
        return resp;
    }

    @PostMapping(value = "/login")
    public ApiResponse login(@RequestBody Authen auth) {
        ApiResponse resp = apiResp.getApiResponse(ECode.SUCCESS);
        try {
            LOGGER.info(String.format("Login username[%s] pass[%s]",
                    auth.getUsername(),
                    auth.getPassword()));
            Pair<ECode, UserEntity> ret = userService.get(auth);
            if (ECode.isFailed(ret.getFirst())) {
                return apiResp.getApiResponse(ret.getFirst());
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resp = apiResp.getApiResponse(ECode.EXCEPTION);
        }
        return resp;
    }
}
