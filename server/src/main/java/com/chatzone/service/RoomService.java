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
package com.chatzone.service;

import com.chatzone.db.entity.RoomEntity;
import com.chatzone.db.repository.RoomRepository;
import com.chatzone.model.ECode;
import com.chatzone.service.inf.IRoomService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

/**
 *
 * @author duongban
 */
@Service
public class RoomService implements IRoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomRepository repo;

    @Override
    public Pair<ECode, RoomEntity> create(String name) {
        Pair<ECode, RoomEntity> ret;
        try {
            RoomEntity data = new RoomEntity();
            data.setName(name);
            data.setCode(UUID.randomUUID().toString());
            ret = Pair.of(ECode.SUCCESS, repo.save(data));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            ret = Pair.of(ECode.EXCEPTION, new RoomEntity());
        }
        return ret;
    }

    @Override
    public Pair<ECode, RoomEntity> findByCode(String code) {
        Pair<ECode, RoomEntity> ret;
        try {
            RoomEntity data = repo.findByCode(code);
            if (data == null) {
                return Pair.of(ECode.NOT_EXSTS_ROOM, new RoomEntity());
            }
            ret = Pair.of(ECode.SUCCESS, data);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            ret = Pair.of(ECode.EXCEPTION, new RoomEntity());
        }
        return ret;
    }

}
