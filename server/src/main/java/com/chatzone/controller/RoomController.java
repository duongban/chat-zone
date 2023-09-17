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

import com.chatzone.db.entity.RoomEntity;
import com.chatzone.model.ApiResponse;
import com.chatzone.model.ECode;
import com.chatzone.model.Room;
import com.chatzone.service.inf.IRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private IRoomService roomService;

    @Autowired
    private ApiResponse apiResp;

    @PostMapping(value = "/room")
    public ApiResponse create(@RequestBody Room room) {
        ApiResponse resp = apiResp.getApiResponse(ECode.SUCCESS);
        try {
            Pair<ECode, RoomEntity> ret = roomService.create(room.getName());
            if (ECode.isFailed(ret.getFirst())) {
                return apiResp.getApiResponse(ret.getFirst());
            }
            Room data = new Room();
            data.setName(ret.getSecond().getName());
            data.setCode(ret.getSecond().getCode());
            resp.setData(data);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resp = apiResp.getApiResponse(ECode.EXCEPTION);
        }
        return resp;
    }

    @GetMapping(value = "/room/{code}")
    public ApiResponse find(@PathVariable String code) {
        ApiResponse resp = apiResp.getApiResponse(ECode.SUCCESS);
        try {
            Pair<ECode, RoomEntity> ret = roomService.findByCode(code);
            if (ECode.isFailed(ret.getFirst())) {
                return apiResp.getApiResponse(ret.getFirst());
            }
            Room data = new Room();
            data.setName(ret.getSecond().getName());
            data.setCode(ret.getSecond().getCode());
            resp.setData(data);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resp = apiResp.getApiResponse(ECode.EXCEPTION);
        }
        return resp;
    }
}
