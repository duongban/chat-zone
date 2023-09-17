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
package com.chatzone.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * @author duongban
 */
@Getter
@Setter
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private int err_code;
    private String err_msg;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(int err_code, String err_msg) {
        this.err_code = err_code;
        this.err_msg = err_msg;
    }

    public ApiResponse getApiResponse(ECode ecode) {
        ApiResponse ret = new ApiResponse(
                ECode.NOT_DEFINED.getValue(),
                "Error");
        switch (ecode) {
            case SUCCESS:
                ret = new ApiResponse(
                        ECode.SUCCESS.getValue(),
                        "Success");
                break;
            case FAILED:
                ret = new ApiResponse(
                        ECode.SUCCESS.getValue(),
                        "Failed");
                break;
            case EXCEPTION:
                ret = new ApiResponse(
                        ECode.EXCEPTION.getValue(),
                        "Exception");
                break;
            case ALREADY_EXISTS_USERNAME:
                ret = new ApiResponse(
                        ECode.ALREADY_EXISTS_USERNAME.getValue(),
                        "Already exists username");
                break;
            case INVALID_USERNAME_OR_PASSWORD:
                ret = new ApiResponse(
                        ECode.INVALID_USERNAME_OR_PASSWORD.getValue(),
                        "Invalid username or password");
                break;
            case NOT_EXSTS_ROOM:
                ret = new ApiResponse(
                        ECode.NOT_EXSTS_ROOM.getValue(),
                        "Not exists room");
                break;
        }
        return ret;
    }
}
