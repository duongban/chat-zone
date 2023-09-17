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

import com.chatzone.db.entity.UserEntity;
import com.chatzone.db.repository.UserRepository;
import com.chatzone.model.Authen;
import com.chatzone.model.ECode;
import com.chatzone.service.inf.IUserService;
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
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repo;

    @Override
    public Pair<ECode, UserEntity> create(Authen user) {
        Pair<ECode, UserEntity> ret;
        try {
            UserEntity data = repo.findByUsername(user.getUsername());
            if (data != null) {
                return Pair.of(ECode.ALREADY_EXISTS_USERNAME, data);
            }
            data = new UserEntity();
            data.setUsername(user.getUsername());
            data.setPassword(user.getPassword());
            ret = Pair.of(ECode.SUCCESS, repo.save(data));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            ret = Pair.of(ECode.EXCEPTION, new UserEntity());
        }
        return ret;
    }

    @Override
    public Pair<ECode, UserEntity> get(Authen user) {
        UserEntity ret = repo.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (ret == null) {
            return Pair.of(ECode.INVALID_USERNAME_OR_PASSWORD, new UserEntity());
        }
        return Pair.of(ECode.SUCCESS, ret);
    }

}
